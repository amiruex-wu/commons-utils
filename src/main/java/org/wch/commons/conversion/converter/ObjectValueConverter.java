package org.wch.commons.conversion.converter;

import lombok.NoArgsConstructor;
import org.wch.commons.lang.ObjectUtils;

import java.util.Optional;

@NoArgsConstructor
public class ObjectValueConverter<T> extends AbstractConverter<T> {

    public ObjectValueConverter(Object source, Class<T> requiredType) {
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

        return caseJsonStrConvert();
    }
}
