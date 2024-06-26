package top.itoolbox.commons.conversion.converter;

import lombok.NoArgsConstructor;
import top.itoolbox.commons.lang.BooleanUtils;
import top.itoolbox.commons.lang.DateUtils;
import top.itoolbox.commons.lang.ObjectUtils;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @Description: 字符类型转换器
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@NoArgsConstructor
public class StringValueConverter<T> extends AbstractConverter<T> {

    public StringValueConverter(Object source, Class<T> requiredType) {
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
        String temp = ((String) obj).toLowerCase()
                .replaceAll("l", "")
                .replaceAll("d", "")
                .replaceAll("f", "");
        String s = temp.contains(".") ? temp.substring(0, temp.indexOf(".")) : temp;
        switch (requiredType.getSimpleName()) {
            case "Character":
                result = Optional.of(requiredType.cast(((String) obj).charAt(0)));
                break;
            case "String":
                result = Optional.of(requiredType.cast(obj.toString()));
                break;
            case "Byte":
                result = Optional.of(requiredType.cast(Byte.valueOf(obj.toString())));
                break;
            case "Short":
                result = Optional.of(requiredType.cast(Short.valueOf(obj.toString())));
                break;
            case "Integer":
                result = Optional.of(requiredType.cast(Integer.valueOf(obj.toString())));
                break;
            case "Long":
                result = Optional.of(requiredType.cast(Long.valueOf(obj.toString())));
                break;
            case "Double":
                result = Optional.of(requiredType.cast(Double.valueOf(obj.toString())));
                break;
            case "Float":
                result = Optional.of(requiredType.cast(Float.valueOf(obj.toString())));
                break;
            case "Boolean":
                result = Optional.of(requiredType.cast(BooleanUtils.toBoolean(s)));
                break;
            case "BigDecimal":
                result = Optional.of(requiredType.cast(new BigDecimal(temp)));
                break;
            case "LocalDateTime":
                result = DateUtils.parse((String) obj).map(requiredType::cast);
                break;
            case "LocalDate":
                result = DateUtils.parse((String) obj).map(localDateTime -> requiredType.cast(localDateTime.toLocalDate()));
                break;
            case "LocalTime":
                result = DateUtils.parse((String) obj).map(localDateTime -> requiredType.cast(localDateTime.toLocalTime()));
                break;
            case "Date":
                result = DateUtils.parse((String) obj).map(localDateTime -> requiredType.cast(DateUtils.toDate(localDateTime)));
                break;
            default:
                // 其他类型处理
//                result = caseJsonStrConvert();
                result = Optional.empty();
                break;
        }
        return result;
    }
}
