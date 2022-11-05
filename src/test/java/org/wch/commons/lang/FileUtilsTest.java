package org.wch.commons.lang;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-28 16:51
 */
@RunWith(JUnit4.class)
public class FileUtilsTest {

    @Test
    public void test(){
        String fileName = "initsql/update.sql";
        Reader reader = FileUtils.of(fileName).readResourceFile();
        if(ObjectUtils.nonNull(reader)){
            System.out.println("reader is not null");
            try(BufferedReader bufferedReader = new BufferedReader(reader)) {
                String line;
                while ((line = bufferedReader.readLine())!= null){
                    System.out.println("result is:" + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }else {
            System.out.println("reader is null");
        }

    }
}
