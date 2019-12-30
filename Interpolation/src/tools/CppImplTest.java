package tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CppImplTest {

    @Test
    void interpolate() {
        InterpolationTool cpp = new CppImpl();
        cpp.interpolate(null, null, 10, 0);
    }
}