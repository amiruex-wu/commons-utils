package top.itoolbox.commons.conversion.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.itoolbox.commons.beans.FieldPropertyDescriptor;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractConverter<T> {

    protected static Map<Class<?>, FieldPropertyDescriptor[]> classFieldPD = new ConcurrentHashMap<>(64);

    protected Object source;

    protected Class<T> requiredType;

    abstract public Optional<T> convert();

}
