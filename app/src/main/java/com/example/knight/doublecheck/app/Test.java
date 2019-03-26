package com.example.knight.doublecheck.app;

public class Test {

    private int a = 5;


    private String test() {
        return "aaa";
    }

    public class TTT {
        private String TTTTTT() {
            String liyachao = test() + a;
            return liyachao;
        }

        private int b = 5;
    }

}
