package org.wch.commons.conversion;

import com.sun.corba.se.impl.io.TypeMismatchException;
import org.wch.commons.conversion.converter.AbstractConverter;
import org.wch.commons.lang.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-06 17:20
 */
public class SimpleTypeConverter {

    @NotNull
    public static <T> Optional<T> convertIfNecessary(@NotNull Object value, @NotNull Class<T> requiredType) {
        if (ObjectUtils.anyNull(value, requiredType)) {
            return Optional.empty();
        }
        Optional<AbstractConverter<T>> converter = ConvertFactory.getConverter(value, requiredType);
        if (converter.isPresent()) {
            return converter.get().convert();
        }
        return Optional.empty();
    }

    // region 私有方法
}
