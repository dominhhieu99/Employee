package com.dohieu.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultCreat {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("salary")
    @Expose
    private int salary;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("id")
    @Expose
    private int id;

    public ResultCreat(String name, int salary, int age, int id) {
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

