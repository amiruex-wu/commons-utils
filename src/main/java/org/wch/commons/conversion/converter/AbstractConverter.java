package org.wch.commons.conversion.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wch.commons.beans.FieldPropertyDescriptor;
import org.wch.commons.constant.CommonConstant;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractConverter<T> {

    protected static Map<Class<?>, FieldPropertyDescriptor[]> classFieldPD = new ConcurrentHashMap<>(64);

    protected Object source;

    protected Class<T> requiredType;

    abstract public Optional<T> convert();

}
