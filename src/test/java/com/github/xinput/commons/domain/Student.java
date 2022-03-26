package com.github.xinput.commons.domain;

import java.io.Serializable;

/**
 * @author yuan.lai
 * @version 1.0
 * @date 2021/7/2 08:19
 * @description
 */
public class Student implements Serializable {

  private int id;

  private String name;

  private int age;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "Student{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", age=" + age +
        '}';
  }
}
