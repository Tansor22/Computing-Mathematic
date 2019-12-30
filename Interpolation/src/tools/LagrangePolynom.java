package tools;

public class LagrangePolynom implements InterpolationTool {
    @Override
    public double interpolate(double[] arrX, double[] arrY, int length, double x) {
        double lagrange_pol = 0;
        double basics_pol;

        for (int i = 0; i < length; i++) {
            basics_pol = 1;
            for (int j = 0; j < length; j++) {
                if (j == i) continue;
                basics_pol *= (x - arrX[j]) / (arrX[i] - arrX[j]);
            }
            lagrange_pol += basics_pol * arrY[i];
        }
        return lagrange_pol;
    }
}
