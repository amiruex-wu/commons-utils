package top.itoolbox.commons.beans;

//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import top.itoolbox.commons.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2023/3/18 12:56
 */
//@Slf4j
@RunWith(JUnit4.class)
public class BeanUtilsTest {
//    private Logger log = LoggerFactory.getLogger(BeanUtilsTest.class);

    @Test
    public void test() {
        final PersonB personB = new PersonB();
        personB.setUserName("aaaa");
        personB.setAge(20);
        final PersonD personD = new PersonD();
        personD.setName("dddd");
        personD.setAge(50);
        List<PersonD> objects = new ArrayList<>();
        objects.add(personD);
        personB.setPersons(objects);
        personB.setAddresses(objects);
//        final PersonA personA = new PersonA();
        final PersonA personA = BeanUtils.copyBean(personB, PersonA::new);

//        log.debug("result is:{}", personA);
    }

    @Test
    public void test1() {
        final PersonB personB = new PersonB();
        personB.setUserName("aaaa");
        personB.setAge(20);
        final PersonD personD = new PersonD();
        personD.setName("dddd");
        personD.setAge(50);
        List<PersonD> objects = new ArrayList<>();
        objects.add(personD);
        personB.setPersons(objects);
        personB.setAddresses(objects);
        List<PersonB> list = new ArrayList<>();
        list.add(personB);

        final List<PersonA> results = BeanUtils.copyList(list, PersonA::new);
        results.forEach(item->{
            System.out.println("result is:{}"+ item);
        });
    }

}
