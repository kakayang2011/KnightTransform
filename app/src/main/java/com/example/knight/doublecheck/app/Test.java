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

    public enum Status {
        SCUUESS("1", "成功"), FAILED("2", "失败");

        private String value;
        private String desc;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private Status(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

}
