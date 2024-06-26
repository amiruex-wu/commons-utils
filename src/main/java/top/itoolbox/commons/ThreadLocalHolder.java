package top.itoolbox.commons;

import java.util.Objects;
import java.util.Optional;

/**X
 * @Description: 通用本地线程记录信息工具
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2023/4/1 19:15
 */
public class ThreadLocalHolder {

    private static ThreadLocal<Object> threadLocal = new InheritableThreadLocal<>();

    public void setObjInfo(Object obj) {
        if (Objects.nonNull(threadLocal)) {
            threadLocal.set(obj);
        }
    }

    public static Optional<Object> getObjInfo() {
        if (Objects.isNull(threadLocal)) {
            return Optional.empty();
        }
        return Optional.ofNullable(threadLocal.get());
    }

    public static <T> Optional<T> getObjInfoConvert(Class<T> requiredType) {
        if (Objects.isNull(threadLocal)) {
            return Optional.empty();
        }
        try {
            Object o = threadLocal.get();
            if (null == o) {
                return Optional.empty();
            }
            return Optional.of(requiredType.cast(o));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear() {
        if (Objects.nonNull(threadLocal)) {
            threadLocal.remove();
        }
    }
}
