package com.dohieu.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request_Creat_Update {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("salary")
    @Expose
    private int salary;

    @SerializedName("age")
    @Expose
    private int age;

    public Request_Creat_Update(String name, int salary, int age) {
        this.name = name;
        this.salary = salary;
        this.age = age;
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
}
