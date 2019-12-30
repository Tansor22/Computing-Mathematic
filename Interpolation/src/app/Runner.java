package app;

import tools.CppImpl;
import tools.InterpolationTool;

public class Runner {
    public static void main(String[] args) {
        InterpolationTool cpp = new CppImpl();
        cpp.interpolate(null, null, 10, 0);
    }
}
