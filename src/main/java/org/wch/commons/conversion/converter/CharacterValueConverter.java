package org.wch.commons.conversion.converter;

import lombok.NoArgsConstructor;
import org.wch.commons.lang.ObjectUtils;

import java.util.Optional;

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
