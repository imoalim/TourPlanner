package com.tourplanner.backend.service.util;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Util {

    public static String joinStrings(String[] strings) {
        StringJoiner joiner = new StringJoiner(",");
        for (String string : strings) {
            joiner.add(string);
        }
        return joiner.toString();
    }

    public static double[] stringArrayToDoubleArray(String[] stringArray) {
        double[] doubleArray = new double[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            doubleArray[i] = Double.parseDouble(stringArray[i]);
        }
        return doubleArray;
    }

    public static double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static double roundToNearestInt(double value) {
        return Math.round(value);
    }

    public static String formatRouteForJs(List<double[]> route) {
        return route.stream()
                .map(coords -> "[" + coords[0] + ", " + coords[1] + "]")
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
