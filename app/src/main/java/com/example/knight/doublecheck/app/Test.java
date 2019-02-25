package com.example.knight.doublecheck.app;

public class Test {

    private int abc = 3;
    private int a = 5;

    public Test() {

    }

    public String getToast() {
        Test1 test1 = new Test1();
        return test1.test1();
    }

    private String test() {
        Test1 test1 = new Test1();
        int b = test1.b;
        return "aaa" + b;
    }

    public class Test1 {
        private String test1() {
            String liyachao = test() + abc + a;
            return liyachao;
        }

        private int b = 5;
    }
}
