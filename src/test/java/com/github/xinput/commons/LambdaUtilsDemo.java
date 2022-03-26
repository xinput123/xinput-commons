package com.github.xinput.commons;

import com.github.xinput.commons.domain.Student;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LambdaUtilsDemo {

  private static List<Student> students = new ArrayList();

  private static List<String> ss = new ArrayList();

  @Test
  public void collectColumn() {
    print(students);
    print(LambdaUtils.collectColumn(students, Student::getName));
    print(LambdaUtils.collectColumn(students, s -> s.getAge() > 20, Student::getName));
    print(LambdaUtils.collectDistinctColumn(students, Student::getAge));
    print(LambdaUtils.collectDistinctColumn(students, s -> s.getAge() < 23, Student::getAge));
  }

  @Test
  public void group() {
    print(students);
    print(LambdaUtils.group(students, Student::getId));
    print(LambdaUtils.group(students, Student::getAge));
    print(LambdaUtils.group(students, s -> s.getAge() < 22, Student::getAge));
    print2(LambdaUtils.toMap(students, s -> s.getId(), s -> s.getName(), (k1, k2) -> k1));
    print2(LambdaUtils.toMap(students, s -> s.getAge() < 22, s -> s.getId(), s -> s.getName(), (k1, k2) -> k1));
    print2(LambdaUtils.toMap(students, s -> s.getAge() > 20, s -> s.getAge() < 22, s -> s.getId(), s -> s.getName(), (k1, k2) -> k1));
  }

  @Test
  public void union() {
    print(ss);
    print(LambdaUtils.union(ss));
    print(LambdaUtils.union(ss, s -> !StringUtils.equalsIgnoreCase(s, "5")));
    print(LambdaUtils.union(ss, "="));
    print(LambdaUtils.union(ss, s -> Lists.newArrayList("1", "2", "3", "4").contains(s), "="));
    print(LambdaUtils.union(ss, "=", "begin", "end"));
  }


  @Before
  public void init() {
    for (int i = 1; i < 11; i++) {
      Student s = new Student();
      s.setId(i);
      s.setName("xinput-" + i);
      if (i > 5) {
        s.setAge(15 + i);
      } else {
        s.setAge(15);
      }
      students.add(s);
      ss.add(String.valueOf(i));
    }
  }

  private static void print(List<? extends Object> objs) {
    System.out.println("===List========================");
    objs.stream().forEach(System.out::println);
  }

  private static void print(Map<Object, List<Student>> map) {
    System.out.println("===Map========================");
    map.forEach((key, value) -> System.out.println(key + ": " + value));
  }

  private static void printMap(Map<Integer, Student> map) {
    System.out.println("===Map========================");
    map.forEach((key, value) -> System.out.println(key + ": " + value));
  }

  private static void print2(Map<Object, Object> map) {
    System.out.println("===Map2========================");
    map.forEach((key, value) -> System.out.println(key + ": " + value));
  }

  private static void print(String text) {
    System.out.println("===Text========================");
    System.out.println(text);
  }

  @Test
  public void sub() {
    students.stream().forEach(student -> {
      System.out.println(JsonHelper.toJsonString(student));
    });

    final List<Student> collect = LambdaUtils.sub(students, -3, -3);
    print(collect);
  }

  @Test
  public void map() {
    Map<Integer, Student> studentMap = LambdaUtils.toMap(students, s -> s.getId(), s -> s, (k1, k2) -> k1);
    printMap(studentMap);

    Map<Integer, Student> filter = LambdaUtils.filter(studentMap, map -> map.getKey() == 3);
    printMap(filter);

    Map<Integer, Student> filter3 = LambdaUtils.filter(studentMap, map -> map.getValue().getId() == 3);
    printMap(filter3);
  }
}
