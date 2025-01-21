package com.loficostudios.com.lofiCoffeeCore.utils;

public class FileUtils {
    public static String removeExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return fileName; // Return as is for null or empty input
        }

        int lastDotIndex = fileName.lastIndexOf('.'); // Find the last period
        if (lastDotIndex > 0) { // Ensure period isn't the first character
            return fileName.substring(0, lastDotIndex); // Extract substring
        }

        return fileName; // Return original string if no period is found
    }

}
