package com.scaler.productcatalogservice.util;

public class ArrayUtils {

    public static int getElementByIndex(int a[], int idx) {
        if(idx < 0 || idx > a.length) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return a[idx];
    }
}
