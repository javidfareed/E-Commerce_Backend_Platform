package com.scaler.productcatalogservice.UtilsTests;

import com.scaler.productcatalogservice.util.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ArrayUtilsTests {

    @Test
    @DisplayName(value = "Middle Index Check")
    void getElement_middleIndex_thenReturnElement()
    {
        int shelf[] = {10, 20 , 30};
        int actual = ArrayUtils.getElementByIndex(shelf,1);
        int expected = 20;
        assertEquals(expected,actual);
    }

    @Test
    @DisplayName(value = "Negative Index Check")
    void getElement_NegativeIndex_thenThrowException()
    {
        int shelf[] = {10, 20 , 30};
        assertThrows(IllegalArgumentException.class,() -> ArrayUtils.getElementByIndex(shelf,-1));
    }

    @ParameterizedTest
    @DisplayName(value = "All Index Check")
    @CsvSource({"0,10","1,20","2,30"})
    void getElement_validIndex_thenReturnValue(int idx, int expected)
    {
        int shelf[] = {10, 20 , 30};
        int actual = ArrayUtils.getElementByIndex(shelf,idx);
        assertEquals(expected,actual);
    }
}
