package com.chenshinan.testng.listener;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * @author shinan.chen
 * @since 2019/1/10
 */
public class LogTestListener extends TestListenerAdapter {
    @Override
    public void onTestFailure(ITestResult tr) {
        System.out.println("执行失败");
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        System.out.println("执行跳过");
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        System.out.println("执行成功");
    }

}
