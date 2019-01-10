package com.chenshinan.testng.testUnit;

import org.springframework.util.StringUtils;
import org.testng.annotations.*;

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
        System.out.println("123123");
        assert db.equals("mysql");
    }

    @BeforeGroups
    public void beforeGroupsTest() {
        System.out.println("GroupTest:beforeGroupsTest");
    }

    @AfterGroups(value = "linux")
    public void afterGroupsTest() {
        System.out.println("GroupTest:afterGroupsTest");
    }
}
