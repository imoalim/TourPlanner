//package com.tourplanner.backend.map;
//
//import com.tourplanner.backend.service.util.Util;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.ui.Model;
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class MapController {
//
//    private final MapApi mapApi;
//
//    @GetMapping("/map")
//    public String showMap(@RequestParam String address1, @RequestParam String address2, Model model) {
//        String startCoordinates = mapApi.searchAddress(address1);
//        String endCoordinates = mapApi.searchAddress(address2);
//
//        List<double[]> route = mapApi.searchDirection(startCoordinates, endCoordinates);
//        double[] center = calculateMapCenter(route);
//
//        model.addAttribute("startLat", route.get(0)[0]);
//        model.addAttribute("startLng", route.get(0)[1]);
//        model.addAttribute("endLat", route.get(route.size() - 1)[0]);
//        model.addAttribute("endLng", route.get(route.size() - 1)[1]);
//        model.addAttribute("centerLat", center[0]);
//        model.addAttribute("centerLng", center[1]);
//        model.addAttribute("route", Util.formatRouteForJs(route));
//
//        System.out.println("startLat: " + route.get(0)[0]);
//        System.out.println("startLng: " + route.get(0)[1]);
//        System.out.println("endLat: " + route.get(route.size() - 1)[0]);
//        System.out.println("endLng: " + route.get(route.size() - 1)[1]);
//        System.out.println("centerLat: " + center[0]);
//        System.out.println("centerLng: " + center[1]);
//        System.out.println("route: " + Util.formatRouteForJs(route));
//
//        return "map";
//    }
//
//    private double[] calculateMapCenter(List<double[]> routeCoordinates) {
//        double minLat = Double.MAX_VALUE;
//        double maxLat = Double.MIN_VALUE;
//        double minLng = Double.MAX_VALUE;
//        double maxLng = Double.MIN_VALUE;
//
//        for (double[] coords : routeCoordinates) {
//            double lat = coords[0];
//            double lng = coords[1];
//
//            if (lat < minLat) minLat = lat;
//            if (lat > maxLat) maxLat = lat;
//            if (lng < minLng) minLng = lng;
//            if (lng > maxLng) maxLng = lng;
//        }
//
//        double centerLat = (minLat + maxLat) / 2;
//        double centerLng = (minLng + maxLng) / 2;
//
//        return new double[]{centerLat, centerLng};
//    }
//}
