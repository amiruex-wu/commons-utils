package top.itoolbox.commons.conversion.converter;

import lombok.NoArgsConstructor;
import top.itoolbox.commons.lang.ObjectUtils;

import java.util.Optional;

/**
 * @Description: 字符类型转换器
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@NoArgsConstructor
public class CharacterValueConverter<T> extends AbstractConverter<T> {

    public CharacterValueConverter(Object source, Class<T> requiredType) {
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
        switch (requiredType.getSimpleName()) {
            case "Character":
                result = Optional.of(requiredType.cast(obj));
                break;
            case "String":
                result = Optional.of(requiredType.cast(obj.toString()));
                break;
            default:
                result = Optional.empty();
                break;
        }
        return result;
    }
}
