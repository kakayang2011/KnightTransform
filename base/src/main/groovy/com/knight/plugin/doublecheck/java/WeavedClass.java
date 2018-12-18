package com.knight.plugin.doublecheck.java;


import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class WeavedClass implements Serializable {

    private String className;

    private Set<String> doubleCheckMethods = new LinkedHashSet<>();

    WeavedClass(String className) {
        this.className = className;
    }

    public void addDoubleCheckMethod(String methodSignature) {
        doubleCheckMethods.add(methodSignature);
    }

    public String getClassName() {
        return className;
    }

    public Set<String> getDoubleCheckMethods() {
        return doubleCheckMethods;
    }

    public boolean hasDoubleCheckMethod() {
        return doubleCheckMethods.size() > 0;
    }


}
