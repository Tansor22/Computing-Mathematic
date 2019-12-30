package controllers;

import java.util.Locale;

public class PointView {
    double x;
    double y;

    PointView(double x, double y) {
        this.x = x;
        this.y = y;
    }
    PointView(String str) {
        String[] nums = str.split(";");
        x = Double.valueOf(nums[0]);
        y = Double.valueOf(nums[1]);
    }
    @Override
    public String toString() {
        return  String.format(Locale.ROOT,"%.3f", x) + " ; " + String.format(Locale.ROOT,"%.3f", y);
    }
    public static boolean isPointView(String str) {
        if (str == null) return  false;
        String[] nums = str.split(";");
        if (nums.length !=2 ) return false;
        try {
            Double.valueOf(nums[0]);
            Double.valueOf(nums[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
