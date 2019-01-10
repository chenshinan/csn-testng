package com.chenshinan.testng;

import org.springframework.util.StringUtils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @author shinan.chen
 * @since 2019/1/10
 */
public class GroupTest {

    @Test(groups = {"windows", "linux"})
    public void groupTest1() {
        assert StringUtils.isEmpty(null);
    }

    @Test(groups = {"windows"})
    public void groupTest2() {
        assert StringUtils.isEmpty(null);
    }

    @Parameters("db")
    @Test(groups = {"linux"})
    public void groupTest3(@Optional("oracle") String db) {
        assert db.equals("mysql");
    }

}
