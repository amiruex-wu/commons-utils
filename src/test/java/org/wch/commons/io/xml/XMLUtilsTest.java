package org.wch.commons.io.xml;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.wch.commons.beans.XMLAttribute;
import org.wch.commons.enums.XMLOperateType;
import org.wch.commons.io.Person;
import org.wch.commons.StopWatch;
import org.wch.commons.lang.StringUtils;
import org.wch.commons.text.XMLUtils;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-25 15:30
 */
@RunWith(JUnit4.class)
public class XMLUtilsTest {

    @Test
    public void test() throws IOException, DocumentException {
        String filePath = "E:\\IdeaProjects\\common-utils\\src\\test\\java\\xml\\test1.xml";
        XMLUtils.parseXML(filePath);
    }

    @Test
    public void test1() throws IOException, DocumentException {
        String filePath = "E:\\IdeaProjects\\common-utils\\src\\test\\java\\xml\\test1.xml";
        List<Student> students = XMLUtils.parseToList(filePath, Student.class);
        for (Student student : students) {
//            System.out.println("result is " + student.getStudentClass()+", interest is "+ Strings.join(student.getInterest(), ','));
            System.out.println("result is " + student);
        }
    }

    @Test
    public void test2() throws IOException, DocumentException {
        String filePath = "E:\\IdeaProjects\\common-utils\\src\\test\\java\\xml\\test2.xml";
        List<Student> students = XMLUtils.parseToList(filePath, Student.class, XMLOperateType.ATTRIBUTE);
        for (Student student : students) {
//            System.out.println("result is " + student.getStudentClass()+", interest is "+ Strings.join(student.getInterest(), ','));
            System.out.println("result is " + student);
        }
    }

    @Test
    public void test3() throws IOException, DocumentException {
        String filePath = "E:\\IdeaProjects\\common-utils\\src\\test\\java\\xml\\test3.xml";
        Optional<Student> student = XMLUtils.parseToObject(filePath, Student.class, XMLOperateType.ATTRIBUTE);

//            System.out.println("result is " + student.getStudentClass()+", interest is "+ Strings.join(student.getInterest(), ','));
        System.out.println("result is " + student.orElse(null));

    }

    @Test
    public void test4() throws IOException, DocumentException {
        String filePath = "E:\\IdeaProjects\\common-utils\\src\\test\\java\\xml\\test4.xml";
        Optional<Student> student = XMLUtils.parseToObject(filePath, Student.class);

//            System.out.println("result is " + student.getStudentClass()+", interest is "+ Strings.join(student.getInterest(), ','));
        System.out.println("result is " + student.orElse(null));

    }

    @Test
    public void addProperties() {
        String filePath = "E:\\IdeaProjects\\common-utils\\src\\test\\java\\xml\\test5.xml";
        Map<String, Object> properties = new HashMap<>();
        properties.put("data", "aaadddddddddddd");
        String parentPath = null;
        Optional<String> student = XMLUtils.addEleProperties(filePath, "name", "李四", parentPath, properties, XMLOperateType.GENERAL);

//            System.out.println("result is " + student.getStudentClass()+", interest is "+ Strings.join(student.getInterest(), ','));
        System.out.println("result is " + student.orElse(null));

    }

    @Test
    public void addProperties1() {
        String filePath = "E:\\IdeaProjects\\common-utils\\src\\test\\java\\xml\\test5.xml";
        Map<String, Object> properties = new HashMap<>();
        properties.put("data", "aaadddddddddddd");
        String parentPath = "/studentClass";
        Optional<String> student = XMLUtils.addEleProperties(filePath, "name", "李四", parentPath, properties, XMLOperateType.GENERAL);

//            System.out.println("result is " + student.getStudentClass()+", interest is "+ Strings.join(student.getInterest(), ','));
        System.out.println("result is " + student.orElse(null));

    }

    @Test
    public void addProperties2() {
        String filePath = "E:\\IdeaProjects\\common-utils\\src\\test\\java\\xml\\test5.xml";
        Map<String, Object> properties = new HashMap<>();
        properties.put("data", "aaadddddddddddd");
        String parentPath = "/studentClass";
        Optional<String> student = XMLUtils.addEleProperties(filePath, "name", "李四", parentPath, properties, XMLOperateType.ATTRIBUTE);

//            System.out.println("result is " + student.getStudentClass()+", interest is "+ Strings.join(student.getInterest(), ','));
        System.out.println("result is " + student.orElse(null));

    }

    @Test
    public void addProperties3ByElement() {
        String filePath = "C:\\Windows_Own_Workspace_Folder\\idea-projects\\maven-projects\\commons-utils\\src\\test\\java\\org\\wch\\commons\\io\\xml\\test5.xml";
        Map<String, Object> properties = new HashMap<>();
        properties.put("data", "aaadddddddddddd");
        List<String> strings = new ArrayList<>();
        strings.add("addd");
        strings.add("bbbb");
        strings.add("cccc");
        properties.put("data1", strings);
        List<Person> peoples = new ArrayList<>();
        peoples.add(new Person("Joh", "1223", 12));
        peoples.add(new Person("Joh1", "12232", 18));
        properties.put("data2", peoples);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 20);
        map.put("sex", "男");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id",1222222);
        map1.put("province","湖南省");
        map.put("address", map1);
        properties.put("data3", map);
        String parentPath = "/studentClass";
        XMLAttribute xmlAttribute = new XMLAttribute("name", "李四", parentPath, true, XMLOperateType.GENERAL, properties);
        Optional<String> student = XMLUtils.addEleProperties(filePath, xmlAttribute);
        System.out.println("result is " + student.orElse(null));

    }
    @Test
    public void addProperties4ByAttribute() {
        String filePath = "C:\\Windows_Own_Workspace_Folder\\idea-projects\\maven-projects\\commons-utils\\src\\test\\java\\org\\wch\\commons\\io\\xml\\test3.xml";
        Map<String, Object> properties = new HashMap<>();
        properties.put("data", "aaadddddddddddd");
        List<String> strings = new ArrayList<>();
        strings.add("addd");
        strings.add("bbbb");
        strings.add("cccc");
        properties.put("data1", strings);
        List<Person> peoples = new ArrayList<>();
        peoples.add(new Person("Joh", "1223", 12));
        peoples.add(new Person("Joh1", "12232", 18));
        properties.put("data2", peoples);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 20);
        map.put("sex", "男");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id",1222222);
        map1.put("province","湖南省");
        map.put("address", map1);
        properties.put("data3", map);
        String parentPath = "/studentClass";
        XMLAttribute xmlAttribute = new XMLAttribute("name", "李四", parentPath, true, XMLOperateType.ATTRIBUTE, properties);
        Optional<String> student = XMLUtils.addEleProperties(filePath, xmlAttribute);
        System.out.println("result is " + student.orElse(null));

    }

    @Test
    public void buildElement() {
//        String filePath = "C:\\Windows_Own_Workspace_Folder\\idea-projects\\maven-projects\\commons-utils\\src\\test\\java\\org\\wch\\commons\\io\\xml\\test5.xml";
        Map<String, Object> properties = new HashMap<>();
        properties.put("data", "aaadddddddddddd");
        List<String> strings = new ArrayList<>();
        strings.add("addd");
        strings.add("bbbb");
        strings.add("cccc");
        properties.put("data1", strings);
        List<Person> peoples = new ArrayList<>();
        peoples.add(new Person("Joh", "1223", 12));
        peoples.add(new Person("Joh1", "12232", 18));
        properties.put("data2", peoples);
        List<Object> objects = new ArrayList<>();
        objects.add("1234567");
        objects.add("342255634");
        objects.add(new Person("Joh2", "122sd3", 9));
        objects.add(new Person("Joh3", "122sa32", 22));
        Map<String, Object> map = new HashMap<>();
        map.put("test1", "aaaaddddd");
        map.put("test2", new Person("Joh4", "122sa32", 22));
        objects.add(map);
        properties.put("data3", objects);
        String parentPath = "/studentClass";
        final StopWatch stopWatch = StopWatch.create();
        XMLAttribute xmlAttribute = new XMLAttribute("name", "李四", parentPath, true, XMLOperateType.GENERAL, properties);
        final Optional<String> optional = XMLUtils.buildElement(properties, XMLOperateType.GENERAL);
        System.out.println("result is " + optional.orElse(null));
        final long totalTimeMillis = stopWatch.getTotalTimeMillis();
        System.out.println("耗时：" + totalTimeMillis + "ms");
    }

    @Test
    public void strTest() throws IOException, DocumentException {
        String filePath = "/IdeaProjects/common-utils/src/test/java/xml";
        String[] split = filePath.split("/");
        List<String> collect = Stream.of(split).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        System.out.println(split.length);
        for (String s : collect) {

            System.out.println(s);
        }

    }

}
