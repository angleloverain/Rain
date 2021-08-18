package com.example.rain.objectbox.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class UserBean {

    @Id
    public Long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    int age = 0;
    String name = "";

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
