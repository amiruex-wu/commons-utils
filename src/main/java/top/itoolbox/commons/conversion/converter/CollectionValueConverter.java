package top.itoolbox.commons.conversion.converter;

import lombok.NoArgsConstructor;
import top.itoolbox.commons.lang.ObjectUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description: 集合类型转换器
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@NoArgsConstructor
public class CollectionValueConverter<T> extends AbstractConverter<T> {

    public CollectionValueConverter(Object source, Class<T> requiredType) {
        super(source, requiredType);
    }

    @Override
    public Optional<T> convert() {
        return convert(source, requiredType);
    }

    public Optional<T> convert(Object obj, Class<T> requiredType) {
        if (ObjectUtils.anyNull(obj, requiredType)) {
            return Optional.empty();
        }
        Optional<T> result;
        Collection<Object> collection = (Collection) obj;
        if ("String".equals(requiredType.getSimpleName())) {
            final String collect = collection.stream().map(String::valueOf).collect(Collectors.joining(","));
            result = Optional.of(requiredType.cast(collect));
        } else {
            result = Optional.empty();
        }
        return result;
    }
}
