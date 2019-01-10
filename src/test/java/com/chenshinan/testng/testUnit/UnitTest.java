package com.chenshinan.testng.testUnit;

import org.springframework.util.StringUtils;
import org.testng.annotations.Test;

/**
 * @author shinan.chen
 * @since 2019/1/8
 */
public class UnitTest {
    @Test
    public void isEmpty() {
        assert StringUtils.isEmpty(null);
        assert StringUtils.isEmpty("");
    }

    @Test
    public void trim() {
        assert "foo".equals(StringUtils.trimAllWhitespace("  foo   "));
    }
}
