package org.wch.commons.lang;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.Optional;

@RunWith(JUnit4.class)
public class ArrayUtilsTest {
    @Test
    public void test(){
        String[] temp = {"11-2","5","2","6","5"};
        String temp1 = "addddd,bbbb,ccccc,dddd,wwwww";
        String temp2 = "addddd,";
        String temp3 = "[\"addddd\",\"bbbb\",\"cccc\",\"eeeee\"]";
        String temp4 = "[{\"addddd\"},{\"bbbb\"},{\"cccc\"},{\"eeeee\"}]";
        String temp5 = "[{\"label\":\"addddd\"},{\"label\":\"bbbb\"},{\"label\":\"cccc\"},{\"label\":\"eeeee\"}]";
        String temp6 = "[[\"addddd\"],[\"bbbb\"],[\"cccc\"],[\"eeeee\"]]";
        Optional<Object[]> objects = ArrayUtils.toArray(temp4);

        System.out.println("exist " + objects.isPresent());
        objects.ifPresent(floats -> {
            for (Object aFloat : floats) {
                System.out.println("result is " + aFloat);
            }
        });
//        System.out.println("result is " + Arrays.toString(objects.orElse(null)));
    }
}
