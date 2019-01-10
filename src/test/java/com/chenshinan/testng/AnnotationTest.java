package com.chenshinan.testng;

import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author shinan.chen
 * @since 2019/1/10
 */
public class AnnotationTest {

    @DataProvider(name = "dataProvider")
    public Object[][] dataProvider1() {
        return new Object[][]{
                {"Cedric", new Integer(36)},
                {"Anne", new Integer(37)},
                {"Jay", new Integer(30)},
                {"Chen", new Integer(25)},
        };
    }

    /**
     * 先后顺序
     */
    @Test(dependsOnMethods = "dataProviderTest")
    public void dependsOnMethodsTest() {
        System.out.println("dependsOnMethodsTest");
    }

    @Test(dataProvider = "dataProvider")
    public void dataProviderTest(String n1, Integer n2) {
        System.out.println(n1 + " " + n2);
    }

    @AfterClass
    public void afterClass() {
        System.out.println("AnnotationTest:afterClass");
    }

}
