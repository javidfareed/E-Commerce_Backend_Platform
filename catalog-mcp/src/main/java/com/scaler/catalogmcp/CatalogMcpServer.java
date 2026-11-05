package com.scaler.catalogmcp;

import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class CatalogMcpServer {

    private static final String BASE = System.getenv().getOrDefault("CATALOG_URL", "http://localhost:8080");
    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final JsonMapper JSON = new JsonMapper();

    interface Action {
        String run() throws Exception;
    }

    //to build and handle get calls
    private static String get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE + path)).GET().build();
        HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) return "Not found.";
        if (response.statusCode() != 200) throw new IllegalStateException("catalog returned HTTP " + response.statusCode());
        return response.body();
    }

    // One place that turns any failure into a message the model can read
    private static McpSchema.CallToolResult reply(Action action) {
        try {
            return McpSchema.CallToolResult.builder().addTextContent(action.run()).build();
        } catch (Exception e) {
            return McpSchema.CallToolResult.builder().addTextContent("Tool failed: " + e).isError(true).build();
        }
    }

    public static void main(String[] args) {
        // stdout belongs to the protocol, so we log to System.err only
        System.err.println("catalog-mcp talking to " + BASE);

        //build tools
        McpSchema.Tool listCategories = McpSchema.Tool.builder()
                .name("list_categories")
                .description("List every product category in the ShopNest catalog, with the number of products in each.")
                .inputSchema(Map.of("type", "object", "properties", Map.of()))
                .build();

        McpSchema.Tool searchProducts = McpSchema.Tool.builder()
                .name("search_products")
                .description("Search ShopNest products by price range, cheapest first unless sort is 'price,desc'. "
                        + "Returns 20 products per page with id, name, price (in dollars) and category. "
                        + "It cannot filter by category or name.")
                .inputSchema(Map.of("type", "object", "properties", Map.of(
                        "minPrice", Map.of("type", "number", "description", "Lowest price in dollars, e.g. 10"),
                        "maxPrice", Map.of("type", "number", "description", "Highest price in dollars, e.g. 50"),
                        "sort", Map.of("type", "string", "description", "Either 'price,asc' or 'price,desc'"),
                        "page", Map.of("type", "integer", "description", "Page number, counting from 0"))))
                .build();

        McpSchema.Tool getProduct = McpSchema.Tool.builder()
                .name("get_product")
                .description("Get one ShopNest product by its numeric id: name, description, price (in dollars) and category.")
                .inputSchema(Map.of("type", "object",
                        "properties", Map.of("id", Map.of("type", "integer", "description", "Product id, 1 or higher")),
                        "required", List.of("id")))
                .build();

        //Call the required functions to execute the tool
        McpServer.sync(new StdioServerTransportProvider(McpJsonDefaults.getMapper()))
                .serverInfo("catalog-mcp", "1.0.0")
                .immediateExecution(true) // answer one call at a time: parallel calls lost replies in our test
                .capabilities(McpSchema.ServerCapabilities.builder().tools(true).build())
                // for each card: the code that runs when the model uses it
                .toolCall(listCategories, (exchange, request) -> reply(() -> get("/categories")))
                .toolCall(searchProducts, (exchange, request) -> reply(() -> searchCatalog(request.arguments())))
                .toolCall(getProduct, (exchange, request) -> reply(() -> findProduct(request.arguments())))
                .build();
    }
    private static String searchCatalog(Map<String, Object> input) throws Exception {
        // 1. Read what the model sent
        Map<String, Object> a = input == null ? Map.of() : input;
        int page = Math.max(0, Integer.parseInt(a.getOrDefault("page", 0).toString()));
        String sort = "price,desc".equals(a.get("sort")) ? "price,desc" : "price,asc";

        // 2. Build the request for our catalog service
        BigDecimal min = new BigDecimal(a.getOrDefault("minPrice", 0).toString());
        BigDecimal max = new BigDecimal(a.getOrDefault("maxPrice", 1000000).toString());
        String query = "/products/search?size=20&page=" + page + "&sort=" + sort
                + "&minPrice=" + min + "&maxPrice=" + max;

        // 3. Call the service, and turn the big JSON answer into short lines for the model
        JsonNode result = JSON.readTree(get(query));
        long total = result.path("page").path("totalElements").asLong();
        StringBuilder out = new StringBuilder(total + " products match. Page " + page + " (counting from 0):\n");
        for (JsonNode p : result.path("content")) {
            long id = p.path("id").asLong();
            String name = p.path("name").asString();
            String price = p.path("price").asString();
            String category = p.path("category").path("name").asString();
            out.append("#" + id + " " + name + " | $" + price + " | " + category + "\n");
        }
        return out.toString();
    }

    // The code behind the "get_product" card
    private static String findProduct(Map<String, Object> input) throws Exception {
        long id = Long.parseLong(input.get("id").toString());
        return get("/products/" + id);
    }

    //User → OpenCode → AI Model → OpenCode (MCP Client) → MCP Server (Java) → Spring Boot REST API →
    //Database → Spring Boot → MCP Server → OpenCode (MCP Client) → AI Model → OpenCode → User.
}
