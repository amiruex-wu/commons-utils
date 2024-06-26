package top.itoolbox.commons.conversion;

import top.itoolbox.commons.conversion.converter.AbstractConverter;
import top.itoolbox.commons.lang.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @Description: 类型转换器
 * @Author: wuchu
 * @CreateTime: 2022-07-06 17:20
 */
public class SimpleTypeConverter {

    @NotNull
    public static <T> Optional<T> convertIfNecessary(@NotNull Object value, @NotNull Class<T> requiredType) {
        if (ObjectUtils.anyNull(value, requiredType)) {
            return Optional.empty();
        }
        return ConvertFactory.getConverter(value, requiredType)
                .map(AbstractConverter::convert)
                .orElse(null);
    }

    // region 私有方法
}
