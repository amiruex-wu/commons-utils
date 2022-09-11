package org.wch.commons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2022/9/10 10:54
 */
@RunWith(JUnit4.class)
public class BeanUtilsCopyUtils12Test {

    @Test
    public void test3() {
        // todo 待优化
        List<Person> temp = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("key", 1222);
        map.put("key1", 122222);
        map.put("key2", "target is map");
        List<String> list = new ArrayList<>();
        list.add("aaaaa");
        list.add("aaaaa1");
        int size = 10;
        // spring beanUtils性能
        // 1000     --> 181ms、174ms、175ms、163ms、177ms
        // 10000    --> 192ms、212ms、194ms、205ms、211ms
        // 100000   --> 318ms、311ms、332ms、314ms、310ms、 538ms
        // 1000000  --> 983ms、943ms、945ms、1833ms、947ms、948ms
        // -----------------------------------------------------------
        // 当前性能
        // 1000     --> 相差不大
        // 10000    --> 223ms、228ms、217ms、215ms、245ms、220ms  266ms、255ms、274ms、260ms
        // 100000   --> 471ms、438ms、491ms、460ms、475ms、451ms  687ms、696ms、681ms、684ms
        // 1000000  --> 2342ms、2525ms、2342ms                   6149ms、4537ms、4521ms、4504ms、4503ms、6773ms

        for (int i = 0; i < 10000; i++) {
            Person person = new Person();
            person.setUserName("aaaa");
            person.setPassword("aaaaerererere");
            person.setAge(20);
            person.setBirthday(new Date());
            person.setNickName("John simith" + i);
            person.setPhone("151255455252" + (i * 2));
            person.setEmail("151255455252@qq.com");
            person.setIdCardNo1("5515125545525");
            AddressVO addressVO = new AddressVO();

            addressVO.setProvince("湖南省");
            addressVO.setCity("长沙市");
            addressVO.setDistrict("望城区xxxx" + i + "号");
            addressVO.setDetail("xxxx团结路幸福街" + i + "号");
            person.setAddress(addressVO);
            List<AddressVO> addressVOS = new ArrayList<>();
            addressVOS.add(addressVO);
            addressVOS.add(addressVO);
            addressVOS.add(addressVO);
            addressVOS.add(addressVO);
            addressVOS.add(addressVO);
            person.setAddresses(addressVOS);
            person.setEmergencyLinkMan("赵市明");
            person.setRoles1(list);
            person.setTarget(map);
            temp.add(person);
        }
        final StopWatch stopWatch1 = StopWatch.create();
        final List<Person1> collect = temp.stream().map(person -> {
            final long l = System.currentTimeMillis();
            Person1 person1 = new Person1();
            BeanUtils.copyProperties(person, person1);
            final long l1 = System.currentTimeMillis() - l;
            if(l1 > 0){

                System.out.println("haoshi:"+ l1 +"ms");
            }
            return person1;
        }).collect(Collectors.toList());

        stopWatch1.stop();
        System.out.println("total cost time is " + stopWatch1.getTotalTimeMillis() + "ms");

        System.out.println("person1 is " + collect.size());
        for (int i = 0; i < 5; i++) {
            final Person1 person1 = collect.get(i);
            System.out.println("person1 is " + person1);
            if (Objects.nonNull(person1.getTarget())) {
                person1.getTarget().forEach((k, v) -> {
                    System.out.println("k:" + k + ",v:" + v);
                });
            }
            System.out.println("person1 getRoles1 is " + person1.getRoles1());
        }

    }
}
