package org.wch.commons.io.net;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.enums.ProtocolType;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.net.HttpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@RunWith(JUnit4.class)
public class HttpUtilsTest {

    @Test
    public void test() {
        Optional<URL> url = HttpUtils.buildURL(ProtocolType.HTTPS, "blog.csdn.net", null, "weixin_35201342/article/details/114101181", null);
        if (url.isPresent()) {
            BufferedReader bufferedReader = null;
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.get().openConnection();
                httpURLConnection.setConnectTimeout(5000);
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ObjectUtils.nonNull(bufferedReader)) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        /*if(url.isPresent()){
            System.out.println("url is "+ url.get().getPath());
        }else{
            System.out.println("url is "+ null);
        }*/
    }
}