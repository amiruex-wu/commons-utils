package top.itoolbox.commons.lang;

//import com.sun.xml.internal.ws.encoding.soap.SerializationException;

import java.io.*;

/**
 * @Description: 对象序列化工具类
 * @Author: wuchu
 * @CreateTime: 2022-07-13 16:53
 */
public class SerializationUtils {

    public SerializationUtils() {
    }

    public static Object clone(Serializable object) {
        return deserialize(serialize(object));
    }

    public static void serialize(Serializable obj, OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        } else {
            ObjectOutputStream out = null;

            try {
                out = new ObjectOutputStream(outputStream);
                out.writeObject(obj);
            } catch (IOException var11) {
                throw new RuntimeException(var11);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException var10) {
                    var10.printStackTrace();
                }

            }

        }
    }

    public static byte[] serialize(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }

    public static Object deserialize(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        } else {
            ObjectInputStream in = null;

            Object var2;
            try {
                in = new ObjectInputStream(inputStream);
                var2 = in.readObject();
            } catch (ClassNotFoundException | IOException var12) {
                throw new RuntimeException(var12);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException var11) {
                    var11.printStackTrace();
                }
            }

            return var2;
        }
    }

    public static Object deserialize(byte[] objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        } else {
            ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
            return deserialize(bais);
        }
    }

}
