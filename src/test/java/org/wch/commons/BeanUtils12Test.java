//package org.wch.commons;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @Description: TODO
// * @Author: wuchu
// * @CreateTime: 2022-07-13 17:22
// */
//@RunWith(JUnit4.class)
//public class BeanUtils12Test {
//
//    @Test
//    public void test() {
//        Person person = new Person();
//        Person person1 = new Person();
//        person.setUserName("aaaa");
//        person.setPassword("aaaaerererere");
//        person.setAge(20);
//        Object o = BeanUtils12.copyProperties(person, Person.class);
//        System.out.println("person1 is " + o);
//
//    }
//
//    @Test
//    public void test1() {
//        Person person = new Person();
//        Person person1 = new Person();
//        person.setUserName("aaaa");
//        person.setPassword("aaaaerererere");
//        person.setAge(20);
//        Object o = BeanUtils12.copyProperties(person, Person.class);
//        System.out.println("person1 is " + o);
//
//    }
//
//    @Test
//    public void test2() {
//        Person person = new Person();
//        Person1 person1 = new Person1();
//        person.setUserName("aaaa");
//        person.setPassword("aaaaerererere");
//        person.setAge(20);
//        person.setNickName("John simith");
//        person.setPhone("151255455252");
//        person.setEmail("151255455252@qq.com");
//        person.setIdCardNo1("5515125545525");
//        AddressVO addressVO = new AddressVO();
//        addressVO.setProvince("湖南省");
//        addressVO.setCity("长沙市");
//        addressVO.setDistrict("望城区");
//        person.setAddress(addressVO);
//        person.setEmergencyLinkMan("赵市明");
//        final StopWatch stopWatch1 = StopWatch.create();
////        try {
////            BeanUtils.getMethod(person);
////        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
////            e.printStackTrace();
////        }
//        BeanUtils12.copyProperties(person, person1, false);
//        System.out.println("person1 read cost time is " + stopWatch1.getTotalTimeMillis() + "ms");
//        System.out.println("person1 is " + person1);
//
//    }
//
//    @Test
//    public void test3() {
//        // todo 待优化
//
//        List<Person> temp = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("key", 1222);
//        map.put("key1", 122222);
//        map.put("key2", "target is map");
//        List<String> list = new ArrayList<>();
//        list.add("aaaaa");
//        list.add("aaaaa1");
//        int size = 10;
//        // spring beanUtils性能
//        // 1000     --> 181ms、174ms、175ms、163ms、177ms
//        // 10000    --> 192ms、212ms、194ms、205ms、211ms
//        // 100000   --> 318ms、311ms、332ms、314ms、310ms、 538ms
//        // 1000000  --> 983ms、943ms、945ms、1833ms、947ms、948ms
//        // -----------------------------------------------------------
//        // 当前性能
//        // 1000     --> 554ms、500ms、478ms、434ms、428ms
//        // 10000    --> 1952ms、
//        // 100000   -->
//        // 1000000  -->
//
//        for (int i = 0; i < 100000; i++) {
//            Person person = new Person();
//            person.setUserName("aaaa");
//            person.setPassword("aaaaerererere");
//            person.setAge(20);
//            person.setBirthday(new Date());
//            person.setNickName("John simith" + i);
//            person.setPhone("151255455252" + (i * 2));
//            person.setEmail("151255455252@qq.com");
//            person.setIdCardNo1("5515125545525");
//            AddressVO addressVO = new AddressVO();
//
//            addressVO.setProvince("湖南省");
//            addressVO.setCity("长沙市");
//            addressVO.setDistrict("望城区xxxx" + i + "号");
//            addressVO.setDetail("xxxx团结路幸福街" + i + "号");
//            person.setAddress(addressVO);
//            List<AddressVO> addressVOS = new ArrayList<>();
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);
//            person.setAddresses(addressVOS);
//            person.setEmergencyLinkMan("赵市明");
//            person.setRoles1(list);
//            person.setTarget(map);
//            temp.add(person);
//        }
//        final StopWatch stopWatch1 = StopWatch.create();
//        final List<Person1> collect = temp.stream().map(person -> {
//            final long curent = System.currentTimeMillis();
//            Person1 person1 = new Person1();
////            BeanUtils12.copyProperties(person, person1, false);
////            BeanUtils.copyProperties31(person, person1, false);
////            BeanUtils.copyProperties32(person, person1, false);
//            BeanUtils.copyProperties(person, person1);
////            BeanUtils.of(person, person1).copy();
//            System.out.println("person1 read cost time is "+  (System.currentTimeMillis() - curent)+ "ms");
//            return person1;
//        }).collect(Collectors.toList());
//
////        try {
////            BeanUtils.getMethod(person);
////        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
////            e.printStackTrace();
////        }
//        stopWatch1.stop();
//        System.out.println("total cost time is " + stopWatch1.getTotalTimeMillis() + "ms");
//
//        System.out.println("person1 is " + collect.size());
//        for (int i = 0; i < 10; i++) {
//            final Person1 person1 = collect.get(i);
//            System.out.println("person1 is " + person1);
//            if(Objects.nonNull(person1.getTarget())){
//
//                person1.getTarget().forEach((k, v) -> {
//                    System.out.println("k:" + k + ",v:" + v);
//                });
//            }
//            System.out.println("person1 is " + person1.getRoles1());
//        }
//
//    }
//
//
//    @Test
//    public void test4() {
//        // todo 待优化
//
//        List<Person> temp = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("key", 1222);
//        map.put("key1", 122222);
//        map.put("key2", "target is map");
//        List<String> list = new ArrayList<>();
//        list.add("aaaaa");
//        list.add("aaaaa1");
//        int size = 10;
//        // 1000     --> 372ms
//        // 10000    --> 1107ms
//        // 100000   --> 5562ms
//        // 1000000  --> 46649ms
//
//        /*
//         * 1000   597ms、561ms、503ms、457ms
//         * 10000   2113ms、2158ms、2039ms、1924ms、1836ms、2109ms
//         * 100000  12434ms、13761ms
//         */
//        for (int i = 0; i < 100; i++) {
//            Person person = new Person();
//            person.setUserName("aaaa");
//            person.setPassword("aaaaerererere");
//            person.setAge(20);
//            person.setBirthday(new Date());
//            person.setNickName("John simith" + i);
//            person.setPhone("151255455252" + (i * 2));
//            person.setEmail("151255455252@qq.com");
//            person.setIdCardNo1("5515125545525");
//        /*    AddressVO addressVO = new AddressVO();
//
//            addressVO.setProvince("湖南省");
//            addressVO.setCity("长沙市");
//            addressVO.setDistrict("望城区xxxx" + i + "号");
//            addressVO.setDetail("xxxx团结路幸福街" + i + "号");
//            person.setAddress(addressVO);*/
//           /* List<AddressVO> addressVOS = new ArrayList<>();
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);*/
////            person.setAddresses(addressVOS);
//            person.setEmergencyLinkMan("赵市明");
////            person.setRoles1(list);
////            person.setTarget(map);
//            temp.add(person);
//        }
//        final StopWatch stopWatch1 = StopWatch.create();
//        final List<Person1> collect = temp.stream().map(person -> {
////            final long curent = System.currentTimeMillis();
//            Person1 person1 = new Person1();
//            BeanUtils12.copyProperties(person, person1);
////            System.out.println("person1 read cost time is "+  (System.currentTimeMillis() - curent)+ "ms");
//            return person1;
//        }).collect(Collectors.toList());
//
////        try {
////            BeanUtils.getMethod(person);
////        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
////            e.printStackTrace();
////        }
//        stopWatch1.stop();
//        System.out.println("total cost time is " + stopWatch1.getTotalTimeMillis() + "ms");
//
//        System.out.println("person1 is " + collect.size());
//        for (int i = 0; i < 1; i++) {
//            final Person1 person1 = collect.get(i);
//            System.out.println("person1 is " + person1);
//            if(Objects.nonNull(person1.getTarget())){
//
//                person1.getTarget().forEach((k, v) -> {
//                    System.out.println("k:" + k + ",v:" + v);
//                });
//            }
//            System.out.println("person1 is " + person1.getRoles1());
//        }
//
//    }
//
//    @Test
//    public void test5() {
//        // todo 待优化
//
//        List<Person> temp = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("key", 1222);
//        map.put("key1", 122222);
//        map.put("key2", "target is map");
//        List<String> list = new ArrayList<>();
//        list.add("aaaaa");
//        list.add("aaaaa1");
//        int size = 10;
//        // 1000     --> 372ms
//        // 10000    --> 1107ms
//        // 100000   --> 5562ms
//        // 1000000  --> 46649ms
//
//        /*
//         * 1000   597ms、561ms、503ms、457ms
//         * 10000   2113ms、2158ms、2039ms、1924ms、1836ms、2109ms
//         * 100000  12434ms、13761ms
//         */
//        for (int i = 0; i < 1000; i++) {
//            Person person = new Person();
//            person.setUserName("aaaa");
//            person.setPassword("aaaaerererere");
//            person.setAge(20);
//            person.setBirthday(new Date());
//            person.setNickName("John simith" + i);
//            person.setPhone("151255455252" + (i * 2));
//            person.setEmail("151255455252@qq.com");
//            person.setIdCardNo1("5515125545525");
//        /*    AddressVO addressVO = new AddressVO();
//
//            addressVO.setProvince("湖南省");
//            addressVO.setCity("长沙市");
//            addressVO.setDistrict("望城区xxxx" + i + "号");
//            addressVO.setDetail("xxxx团结路幸福街" + i + "号");
//            person.setAddress(addressVO);*/
//           /* List<AddressVO> addressVOS = new ArrayList<>();
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);
//            addressVOS.add(addressVO);*/
////            person.setAddresses(addressVOS);
//            person.setEmergencyLinkMan("赵市明");
////            person.setRoles1(list);
////            person.setTarget(map);
//            temp.add(person);
//        }
//        final StopWatch stopWatch1 = StopWatch.create();
//        final List<Person> collect = temp.stream().map(person -> {
////            final long curent = System.currentTimeMillis();
//            Person person1 = new Person();
//            BeanUtils12.copyProperties(person, person1);
////            System.out.println("person1 read cost time is "+  (System.currentTimeMillis() - curent)+ "ms");
//
//            return person1;
//        }).collect(Collectors.toList());
//
////        try {
////            BeanUtils.getMethod(person);
////        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
////            e.printStackTrace();
////        }
//        stopWatch1.stop();
//        System.out.println("total cost time is " + stopWatch1.getTotalTimeMillis() + "ms");
//
//        System.out.println("person1 is " + collect.size());
//        for (int i = 0; i < 1; i++) {
//            final Person person1 = collect.get(i);
//            System.out.println("person1 is " + person1);
//            if(Objects.nonNull(person1.getTarget())){
//
//                person1.getTarget().forEach((k, v) -> {
//                    System.out.println("k:" + k + ",v:" + v);
//                });
//            }
//            System.out.println("person1 is " + person1.getRoles1());
//        }
//
//    }
//    @Test
//    public void test6() {
//        AddressVO addressVO = new AddressVO();
//        addressVO.setProvince("湖南省");
//        addressVO.setCity("长沙市");
//        addressVO.setDistrict("望城区xxxx" + 1 + "号");
//        addressVO.setDetail("xxxx团结路幸福街" + 1 + "号");
//        final Optional<AddressVO> clone = BeanUtils12.clone(addressVO);
//
//        System.out.println("result is " + clone.orElse(null));
//    }
//
//}
