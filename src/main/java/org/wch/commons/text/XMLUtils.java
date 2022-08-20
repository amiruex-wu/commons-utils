package org.wch.commons.text;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.wch.commons.beans.XMLAttribute;
import org.wch.commons.enums.XMLOperateType;
import org.wch.commons.lang.*;
import org.xml.sax.SAXException;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * @Description: XML操作工具类，基于dom4j
 * @Author: wuchu
 * @CreateTime: 2022-07-25 14:45
 */

public class XMLUtils {

    private final static Class<?>[] BASIC_CLAZZ = {Byte.class, Short.class, Integer.class, Float.class, Double.class,
            Long.class, BigDecimal.class, BigInteger.class, String.class, Boolean.class, Character.class,
            byte.class, short.class, int.class, float.class, double.class,
            long.class, boolean.class, char.class,};

    /**
     * 读取xml并转换成java bean对象(性能问题)
     *
     * @param filePath
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Optional<T> parseToObject(String filePath, Class<T> clazz) {
        if (ObjectUtils.anyNull(filePath, clazz) || StringUtils.isBlank(filePath)) {
            return Optional.empty();
        }
        return parseToObject(new File(filePath), clazz, XMLOperateType.GENERAL);
    }

    public static <T> Optional<T> parseToObject(File file, Class<T> clazz) {
        if (ObjectUtils.anyNull(file, clazz)) {
            return Optional.empty();
        }
        return parseToObject(file, clazz, XMLOperateType.GENERAL);
    }

    public static <T> Optional<T> parseToObject(InputStream inputStream, Class<T> clazz) {
        if (ObjectUtils.anyNull(inputStream, clazz)) {
            return Optional.empty();
        }
        return parseToObject(inputStream, clazz, XMLOperateType.GENERAL);
    }

    public static <T> Optional<T> parseToObject(String filePath, Class<T> clazz, XMLOperateType xmlOperateType) {
        if (ObjectUtils.anyNull(filePath, clazz) || StringUtils.isBlank(filePath)) {
            return Optional.empty();
        }
        return parseToObject(new File(filePath), clazz, xmlOperateType);
    }

    public static <T> Optional<T> parseToObject(File file, Class<T> clazz, XMLOperateType xmlOperateType) {
        if (ObjectUtils.anyNull(file, clazz)) {
            return Optional.empty();
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            return parseToObject(inputStream, clazz, xmlOperateType);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static <T> Optional<T> parseToObject(InputStream inputStream, Class<T> clazz, XMLOperateType xmlOperateType) {
        if (ObjectUtils.anyNull(inputStream, clazz, xmlOperateType)) {
            return Optional.empty();
        }
        Optional<T> result = Optional.empty();
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            Element root = document.getRootElement();
            Iterator<?> it = root.elementIterator();
            Optional<String> label = StringUtils.unCapitalize(clazz.getSimpleName());
            while (it.hasNext()) {
                Element element = (Element) it.next();
                if (result.isPresent()) {
                    break;
                }
                if (Objects.equals(label.orElse(null), element.getName())) {
                    T bean;
                    if (xmlOperateType == XMLOperateType.ATTRIBUTE) {
                        bean = getBeanByAttribute(element, clazz);
                    } else {
                        bean = getBeanByGeneral(element, clazz);
                    }
                    result = Optional.of(bean);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> Optional<T> parseToObjectByXmlStr(String xml, Class<T> clazz) {
        if (ObjectUtils.anyNull(xml, clazz) || StringUtils.isBlank(xml)) {
            return Optional.empty();
        }
        return parseToObject(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)), clazz, XMLOperateType.GENERAL);
    }

    public static <T> Optional<T> parseToObjectByXmlStr(String xml, Class<T> clazz, XMLOperateType xmlOperateType) {
        if (ObjectUtils.anyNull(xml, clazz, xmlOperateType) || StringUtils.isBlank(xml)) {
            return Optional.empty();
        }
        return parseToObject(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)), clazz, xmlOperateType);
    }

    public static <T> List<T> parseToList(String filePath, Class<T> clazz) {
        if (ObjectUtils.anyNull(filePath, clazz)) {
            return CollectionUtils.emptyList();
        }
        return parseToList(filePath, clazz, XMLOperateType.GENERAL);
    }

    public static <T> List<T> parseToList(String filePath, Class<T> clazz, XMLOperateType xmlOperateType) {
        if (ObjectUtils.anyNull(filePath, clazz) || StringUtils.isBlank(filePath)) {
            return CollectionUtils.emptyList();
        }
        try (InputStream inputStream = new FileInputStream(filePath)) {
            return parseToList(inputStream, clazz, xmlOperateType);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static <T> List<T> parseToList(InputStream inputStream, Class<T> clazz, XMLOperateType xmlOperateType) {
        List<T> result = new ArrayList<>();
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            Element root = document.getRootElement();
            Iterator<?> it = root.elementIterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                T bean;
                if (xmlOperateType == XMLOperateType.ATTRIBUTE) {
                    bean = getBeanByAttribute(element, clazz);
                } else {
                    bean = getBeanByGeneral(element, clazz);
                }
                result.add(bean);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static <T> List<T> parseToListByXmlStr(String xmlStr, Class<T> clazz) {
        if (ObjectUtils.anyNull(xmlStr, clazz) || StringUtils.isBlank(xmlStr)) {
            return Collections.emptyList();
        }
        return parseToList(new ByteArrayInputStream(xmlStr.getBytes(StandardCharsets.UTF_8)), clazz, XMLOperateType.GENERAL);
    }

    public static <T> List<T> parseToListByXmlStr(String xmlStr, Class<T> clazz, XMLOperateType xmlOperateType) {
        if (ObjectUtils.anyNull(xmlStr, clazz, xmlOperateType) || StringUtils.isBlank(xmlStr)) {
            return Collections.emptyList();
        }
        return parseToList(new ByteArrayInputStream(xmlStr.getBytes(StandardCharsets.UTF_8)), clazz, xmlOperateType);
    }

    @Deprecated
    public static Optional<String> addEleProperties(String filePath,
                                                    String identifierKey,
                                                    String identifierValue,
                                                    String parentPath,
                                                    Map<String, Object> properties,
                                                    XMLOperateType xmlOperateType) {
        XMLAttribute xmlAttribute = new XMLAttribute(identifierKey, identifierValue, parentPath, false, xmlOperateType, properties);
        return addEleProperties(filePath, xmlAttribute);
    }

    public static Optional<String> addEleProperties(String filePath, XMLAttribute xmlAttribute) {
        if (ObjectUtils.anyNull(filePath, xmlAttribute) || xmlAttribute.isAnyNull()) {
            return Optional.empty();
        }

        List<String> parentNode = new ArrayList<>();
        StringUtils.defaultIfNull(xmlAttribute.getParentRoute())
                .map(s -> s.split("/"))
                .ifPresent(split -> Stream.of(split)
                        .filter(StringUtils::isNotBlank)
                        .forEach(parentNode::add));
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new FileInputStream(filePath));
            Element root = document.getRootElement();
            System.out.println("开始执行添加操作");
            findTargetNode(root, xmlAttribute, parentNode, false);

            final String result = formatXMLString(document);
            return Optional.ofNullable(result);
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<String> buildElement(Map<String, Object> properties, XMLOperateType xmlOperateType) {
        if (ObjectUtils.isEmpty(properties)) {
            return Optional.empty();
        }
        final Document document = DocumentFactory.getInstance().createDocument();
        final Element root = document.addElement("root");

        System.out.println("");
        final List<Class<?>> basicClazz = Arrays.asList(BASIC_CLAZZ);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            if (ObjectUtils.nonNull(entry.getValue())) {
                buildEle(xmlOperateType, root, basicClazz, entry.getKey(), entry.getValue());
            }
        }
        final String xmlString = formatXMLString(document);
//        final Element data = root.element("data");
//        System.out.println("elementText is " + ObjectUtils.isNull(data));
        return Optional.of(xmlString);
    }

    public static Optional<Element> buildElement1(Element root, Map<String, Object> properties, XMLOperateType xmlOperateType, boolean updateOnExists) {
        if (ObjectUtils.isEmpty(properties)) {
            return Optional.empty();
        }
        final List<Class<?>> basicClazz = Arrays.asList(BASIC_CLAZZ);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            final Element element = root.element(entry.getKey());
            if (ObjectUtils.isNull(element)) {
                if (ObjectUtils.nonNull(entry.getValue())) {
                    buildEle(xmlOperateType, root, basicClazz, entry.getKey(), entry.getValue());
                }
            } else {
                buildEle1(xmlOperateType, root, basicClazz, entry.getKey(), entry.getValue(), updateOnExists);
            }
        }
//        final String xmlString = formatXMLString(document);
        return Optional.of(root);
    }

    private static void buildEle(XMLOperateType xmlOperateType, Element root, List<Class<?>> basicClazz, String key, Object value) {
        if (basicClazz.contains(value.getClass())) {
            final Optional<String> optional = ConvertUtils.convertIfNecessary(value, String.class);
            if (XMLOperateType.GENERAL == xmlOperateType) {
                final Element element = root.addElement(key);
                optional.ifPresent(s -> element.setText(s));
            } else {
                optional.ifPresent(s -> root.addAttribute(key, s));
            }
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            final Element element = root.addElement(key);
            for (Object o : collection) {
                buildEle(xmlOperateType, element, basicClazz, "value", o);
            }
        } else if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (ObjectUtils.nonNull(entry.getValue())) {
                    buildEle(xmlOperateType, root, basicClazz, entry.getKey(), entry.getValue());
                }
            }
        } else {
            // 自定义类
            final JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(value));
            final String simpleName = value.getClass().getSimpleName();
            final Optional<String> optional = StringUtils.unCapitalize(simpleName);
            Element element;
            if (optional.isPresent()) {
                if (!ObjectUtils.equals("value", key)) {
                    element = root.addElement(key).addElement(optional.get());
                } else {
                    element = root.addElement(optional.get());
                }
            } else {
                element = root.addElement(key);
            }
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if (ObjectUtils.nonNull(entry.getValue())) {
                    buildEle(xmlOperateType, element, basicClazz, entry.getKey(), entry.getValue());
                }
            }

        }
    }

    /**
     * 元素已经存在，根据是否更新选项以更新属性
     *
     * @param xmlOperateType
     * @param root
     * @param basicClazz
     * @param key
     * @param value
     * @param updateOnExists
     */
    private static void buildEle1(XMLOperateType xmlOperateType, Element root, List<Class<?>> basicClazz, String key, Object value, boolean updateOnExists) {
        if (basicClazz.contains(value.getClass())) {
            final Optional<String> optional = ConvertUtils.convertIfNecessary(value, String.class);
            if (XMLOperateType.GENERAL == xmlOperateType) {
                final Element element = root.element(key);
                if (ObjectUtils.nonNull(element)) {
                    if (updateOnExists) {
                        optional.ifPresent(element::setText);
                    }
                } else {
                    final Element temp = root.addElement(key);
                    optional.ifPresent(temp::setText);
                }
            } else {
                final String s1 = root.attributeValue(key);
                if (StringUtils.isBlank(s1)) {
                    optional.ifPresent(s -> root.addAttribute(key, s));
                } else {
                    if (updateOnExists) {
                        optional.ifPresent(s -> root.addAttribute(key, s));
                    }
                }
            }
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            final Element element = root.addElement(key);
            for (Object o : collection) {
                buildEle1(xmlOperateType, element, basicClazz, "value", o, updateOnExists);
            }
        } else if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (ObjectUtils.nonNull(entry.getValue())) {
                    buildEle1(xmlOperateType, root, basicClazz, entry.getKey(), entry.getValue(), updateOnExists);
                }
            }
        } else {
            // 自定义类
            final JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(value));
            final String simpleName = value.getClass().getSimpleName();
            final Optional<String> optional = StringUtils.unCapitalize(simpleName);
            Element element;
            if (optional.isPresent()) {
                if (!ObjectUtils.equals("value", key)) {
                    element = root.addElement(key).addElement(optional.get());
                } else {
                    element = root.addElement(optional.get());
                }
            } else {
                element = root.addElement(key);
            }
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if (ObjectUtils.nonNull(entry.getValue())) {
                    // todo
                    buildEle1(xmlOperateType, element, basicClazz, entry.getKey(), entry.getValue(), updateOnExists);
                }
            }

        }
    }


    @Deprecated
    public static void parseXML(String filePath) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(filePath));
        Element root = document.getRootElement();

        Iterator<?> it = root.elementIterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            System.out.println(MessageFormat.format("a-标签名称：{0}，属性值：{1}", element.getName(), element.getStringValue()));
            //未知属性名称情况下
            Iterator<?> attrIt = element.attributeIterator();
            while (attrIt.hasNext()) {
                Attribute a = (Attribute) attrIt.next();
                System.out.println(MessageFormat.format("b-标签名称：{0}，属性值：{1}", a.getName(), a.getStringValue()));
//                log.info("标签名称：{}，属性值：{}", element.getName(), element.getStringValue());
                System.out.println(a.getValue());
            }
        }
    }

    @Deprecated
    public static void readXMLFileElementAndSave(String filePath, String savePath) throws DocumentException, IOException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(filePath));
        Element root = document.getRootElement();

        Iterator<?> it = root.elementIterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();

            //未知属性名称情况下
        /* Iterator<?> attrIt = element.attributeIterator();
        while (attrIt.hasNext()) {
            Attribute a  = (Attribute) attrIt.next();
            log.info("标签名称：{}，属性值：{}", element.getName(), element.getStringValue());
            System.out.println(a.getValue());
        }*/
            //已知属性名称情况下
            System.out.println("id: " + element.attributeValue("id"));

            //未知元素名情况下
            Iterator<?> eleIt = element.elementIterator();
            while (eleIt.hasNext()) {
                Element e = (Element) eleIt.next();
                if (e.getName().equalsIgnoreCase("userTask")) {
                    e.attributes().forEach(attribute -> {

//                        log.info("元素标签名称；{}，属性值：{}-{}", e.getName(), attribute.getName(), attribute.getValue());
                        if (attribute.getName().equalsIgnoreCase("assignee")) {
                            attribute.setValue(null);
                        }
                    });
                } else {

//                    log.info("元素标签名称；{}，属性值：{}", e.getName(), e.getText() + e.getData());
                }
            }
        }
        // 保存xml文件
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter(new File(savePath)), format);
        writer.write(document);
        //[8] 关闭资源
        writer.close();
    }

    @Deprecated
    public static String readXMLFileElement(String str, String savePath) throws DocumentException, SAXException, IOException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        Element root = document.getRootElement();

        Iterator<?> it = root.elementIterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            // 未知元素名情况下
            Iterator<?> eleIt = element.elementIterator();
            while (eleIt.hasNext()) {
                Element e = (Element) eleIt.next();
                if (e.getName().equalsIgnoreCase("userTask")) {
                    e.attributes().forEach(attribute -> {
//                        log.info("元素标签名称；{}，属性值：{}-{}", e.getName(), attribute.getName(), attribute.getValue());
                        if (attribute.getName().equalsIgnoreCase("assignee")) {
                            attribute.setValue(null);
                        }
                    });
                } else {
//                    log.info("元素标签名称；{}，属性值：{}", e.getName(), e.getText() + e.getData());
                }
            }
        }
        // 返回xml文件字符
        return document.asXML();
    }

    /*
    @Deprecated
    public static <T> T getBeanByGeneral(Element root, Class<T> clazz, boolean isCollection) {
        try {
            T t = clazz.newInstance();
            Field[] properties = clazz.getDeclaredFields();

            for (Field field : properties) {
                field.setAccessible(true);
                if (Objects.equals(field.getType(), List.class)) {
                    List<Element> elements = root.elements(field.getName());
                    List<Object> temp = new ArrayList<>();
                    for (Element element : elements) {
                        Type genericType = field.getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            // 得到泛型里的class类型对象
                            Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                            Object t1 = getBeanByGeneral(element, actualTypeArgument, true);
                            if (ObjectUtils.nonNull(t1)) {
                                temp.add(t1);
                            }
                        }
                    }
                    field.set(t, temp);
                } else if (Arrays.asList(BASIC_CLAZZ).contains(field.getType())) {
                    setFieldValue(root, t, field);
                } else {
                    if (isCollection && Arrays.asList(BASIC_CLAZZ).contains(clazz)) {
                        String stringValue = root.getStringValue();
                        return ConvertUtils.convertIfNecessary(stringValue, clazz).orElse(null);
                    } else {
                        setFieldValue(root, t, field);
                    }
                }
            }

           *//* for (int i = 0; i < properties.length; i++) {
                fieldType = (properties[i].getType() + "");
                setmeth = t.getClass().getMethod(
                        "set"
                                + properties[i].getName().substring(0, 1)
                                .toUpperCase()
                                + properties[i].getName().substring(1), properties[i].getType());
                if ("interface java.util.List".equals(fieldType)) {
                    fieldGenericType = properties[i].getGenericType() + "";
                    String[] sp1 = fieldGenericType.split("<");
                    String[] sp2 = sp1[1].split(">");
                    className = sp2[0];
                    Object listNode = getList(root.element(properties[i].getName()),
                            Class.forName(className));
                    setmeth.invoke(t, listNode);
                } else {
                    setmeth.invoke(t, root.elementText(properties[i].getName()));
                }
            }*//*
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
*/

    //region 私有方法区

    /**
     * 格式化输出xml
     *
     * @param document dom元素
     * @return 格式化的xml
     */
    private static String formatXMLString(Document document) {
        StringWriter writer = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 指定使用tab键缩进
        format.setIndentSize(4);
        format.setEncoding(StandardCharsets.UTF_8.name());
        XMLWriter xmlwriter = new XMLWriter(writer, format);
        try {
            xmlwriter.write(document);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                xmlwriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer.toString();
    }

    private static <T> T getBeanByGeneral(Element root, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            Field[] properties = clazz.getDeclaredFields();

            for (Field field : properties) {
                field.setAccessible(true);
                if (Objects.equals(field.getType(), List.class)) {
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        // 得到泛型里的class类型对象
                        Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                        List<?> bean = getBeanByGeneral(root, field.getName(), actualTypeArgument);
                        if (ObjectUtils.isNotEmpty(bean)) {
                            field.set(t, bean);
                        }
                    }
                } else if (Arrays.asList(BASIC_CLAZZ).contains(field.getType())) {
                    String value = root.elementText(field.getName());
                    Optional<?> optional = ConvertUtils2.convertIfNecessary(value, field.getType());
                    if (optional.isPresent()) {
                        field.set(t, optional.get());
                    }
                } else {
                    List<?> bean = getBeanByGeneral(root, field.getName(), field.getType());
                    if (ObjectUtils.isNotEmpty(bean)) {
                        field.set(t, bean.get(0));
                    }
                }
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T getBeanByAttribute(Element root, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            Field[] properties = clazz.getDeclaredFields();

            for (Field field : properties) {
                field.setAccessible(true);
                if (Objects.equals(field.getType(), List.class)) {
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        // 得到泛型里的class类型对象
                        Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                        List<?> bean = getBeanByAttribute(root, field.getName(), actualTypeArgument);
                        if (ObjectUtils.isNotEmpty(bean)) {
                            field.set(t, bean);
                        }
                    }
                } else if (Arrays.asList(BASIC_CLAZZ).contains(field.getType())) {
                    String attributeValue = root.attributeValue(field.getName());
                    if (ObjectUtils.nonNull(attributeValue)) {
                        Optional<?> optional = ConvertUtils2.convertIfNecessary(attributeValue, field.getType());
                        if (optional.isPresent()) {
                            field.set(t, optional.get());
                        }
                    }
                } else {
                    // special class
                    List<?> bean = getBeanByAttribute(root, field.getName(), field.getType());
                    if (ObjectUtils.isNotEmpty(bean)) {
                        field.set(t, bean.get(0));
                    }
                }
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> getBeanByGeneral(Element root, String fieldName, Class<T> clazz) {
        try {
            List<T> temp = new ArrayList<>();
            List<Element> elements = root.elements(fieldName);
            if (Arrays.asList(BASIC_CLAZZ).contains(clazz)) {
                for (Element element : elements) {
                    for (Element ele : element.elements()) {
                        String stringValue = ele.getStringValue();
                        ConvertUtils.convertIfNecessary(stringValue, clazz).ifPresent(temp::add);
                    }
                }
            } else {
                for (Element element : elements) {
                    T bean = getBeanByGeneral(element, clazz);
                    temp.add(bean);
                }
            }
            return temp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> getBeanByAttribute(Element root, String fieldName, Class<T> clazz) {
        try {
            List<T> temp = new ArrayList<>();
            List<Element> elements = root.elements(fieldName);
            if (Arrays.asList(BASIC_CLAZZ).contains(clazz)) {
                elements.stream().flatMap(element ->
                        element.elements().stream()
                                .map(ele -> {
                                    if (CollectionUtils.isNotEmpty(ele.attributes())) {
                                        String stringValue = ele.attributes().get(0).getStringValue();
                                        return ConvertUtils.convertIfNecessary(stringValue, clazz).orElse(null);
                                    }
                                    return null;
                                })
                                .filter(Objects::nonNull))
                        .forEach(temp::add);
            } else {
                for (Element element : elements) {
                    T bean = getBeanByAttribute(element, clazz);
                    temp.add(bean);
                }
            }
            return temp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void findTargetNode(Element root, XMLAttribute xmlAttribute, List<String> parentNode, Boolean targetNodeFind) {
        // 到达目标属性父节点
        if (BooleanUtils.isTrue(targetNodeFind) && ObjectUtils.isEmpty(parentNode)) {
            setElementProperties(root, xmlAttribute);
        } else {
            // 循环查找
            Iterator<?> it = root.elementIterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                String eleChildValue;
                if (XMLOperateType.GENERAL == xmlAttribute.getXmlOperateType()) {
                    eleChildValue = element.elementText(xmlAttribute.getIdentifierKey());
                } else {
                    eleChildValue = element.attributeValue(xmlAttribute.getIdentifierKey());
                }
                if (BooleanUtils.isFalse(targetNodeFind)) {
                    if (ObjectUtils.equals(xmlAttribute.getIdentifierValue(), eleChildValue)) {
                        xmlAttribute.setIdentifierValue(parentNode.get(0));
                        findTargetNode(element, xmlAttribute, parentNode, true);
                        break;
                    }
                } else {
                    if (ObjectUtils.equals(xmlAttribute.getIdentifierValue(), element.getName())) {
                        List<String> subList = CollectionUtils.subList(parentNode, NumberUtils.INTEGER_ONE);
                        xmlAttribute.setIdentifierValue(parentNode.get(0));
                        findTargetNode(element, xmlAttribute, subList, targetNodeFind);
                        break;
                    }
                }
            }
        }
    }

    private static void setElementProperties(Element root, XMLAttribute xmlAttribute) {
        final List<Class<?>> classes = Arrays.asList(BASIC_CLAZZ);
        xmlAttribute.getProperties().forEach((k, v) -> {
            boolean present = root.elements().stream().anyMatch(e -> Objects.equals(e.getName(), k));
            if (!present) {
                switch (xmlAttribute.getXmlOperateType()) {
                    case GENERAL:
                        addElement(root, classes, k, v);
                        break;
                    case ATTRIBUTE:
                        addAttribute(root, classes, k, v);
                        break;
                    default:
                        break;
                }
            } else {
                // 存在标签或属性
                if (BooleanUtils.isTrue(xmlAttribute.getUpdateOnExists())) {
                    switch (xmlAttribute.getXmlOperateType()) {
                        case GENERAL:
                            updateElement(root, classes, k, v);
                            break;
                        case ATTRIBUTE:
                            updateAttribute(root, classes, k, v);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    // 深层递归解析map对象
    private static void addElement(Element root, List<Class<?>> basicClasses, String label, Object value) {
        if (value instanceof Collection) {
            Element element = root.addElement(label);
            Collection collection = (Collection) value;
            for (Object o : collection) {
                if (basicClasses.contains(o.getClass())) {
                    element.addElement("item").setText(String.valueOf(o));
                } else {
                    System.out.println("其他自定义类...");
                    addElement(element, basicClasses, label, o);
                }
            }
        } else if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            Element element = root.addElement(label);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (basicClasses.contains(entry.getValue().getClass())) {
                    Element element1 = element.addElement(entry.getKey());
                    final Optional<String> optional = ConvertUtils.convertIfNecessary(entry.getValue(), String.class);
                    optional.ifPresent(element1::setText);
                } else {
                    addElement(element, basicClasses, entry.getKey(), entry.getValue());
                }
            }
        } else if (basicClasses.contains(value.getClass())) {
            Element element = root.addElement(label);
            final Optional<String> optional = ConvertUtils.convertIfNecessary(value, String.class);
            optional.ifPresent(element::setText);
        } else {
            // 自定义class对象类
            final JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(value));
            final String simpleName = value.getClass().getSimpleName();
            final Optional<String> optional = StringUtils.unCapitalize(simpleName);
            Element element = root.addElement(optional.orElse(label));

            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if (ObjectUtils.isNull(entry.getValue())) {
                    continue;
                }
                addElement(element, basicClasses, entry.getKey(), entry.getValue());
            }
        }
    }

    private static void updateElement(Element root, List<Class<?>> basicClasses, String label, Object value) {
        final List<Class<?>> classes = Arrays.asList(BASIC_CLAZZ);
        if (value instanceof Collection) {
            Element element = root.element(label);
            if (Objects.isNull(element)) {
                addElement(root, classes, label, value);
            } else {
                updateElementCollectionType(basicClasses, label, (Collection) value, element);
            }
        } else if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            Element element = root.element(label);
            // 元素不存在的情况
            if (Objects.isNull(element)) {
                addElement(root, classes, label, value);
            } else {
                // 元素存在的情况
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Element elementTemp = element.element(entry.getKey());
                    if (ObjectUtils.isNull(elementTemp)) {
                        addElement(element, basicClasses, entry.getKey(), entry.getValue());
                    } else {
                        if (basicClasses.contains(entry.getValue().getClass())) {
                            final Optional<String> optional = ConvertUtils.convertIfNecessary(entry.getValue(), String.class);
                            optional.ifPresent(elementTemp::setText);
                        } else {
                            updateElement(element, basicClasses, entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
        } else if (basicClasses.contains(value.getClass())) {
            Element element = root.element(label);
            if (ObjectUtils.isNull(element)) {
                element = root.addElement(label);
            }
            final Optional<String> optional = ConvertUtils.convertIfNecessary(value, String.class);
            optional.ifPresent(element::setText);
        } else {
            // 自定义class对象类
            final JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(value));
            final String simpleName = value.getClass().getSimpleName();
            final Optional<String> optional = StringUtils.unCapitalize(simpleName);
            if (!optional.isPresent()) {
                return;
            }
            Element element = root.element(optional.get());
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if (ObjectUtils.isNull(entry.getValue())) {
                    continue;
                }
                if (ObjectUtils.isNull(element)) {
                    element = root.addElement(optional.get());
                    addElement(element, basicClasses, entry.getKey(), entry.getValue());
                } else {
                    updateElement(element, basicClasses, entry.getKey(), entry.getValue());
                }
            }
        }
    }

    // todo
    private static void addAttribute(Element root, List<Class<?>> basicClasses, String label, Object value) {
        if (value instanceof Collection) {
            Element element = root.addElement(label);
            Collection collection = (Collection) value;
            for (Object o : collection) {
                if (basicClasses.contains(o.getClass())) {
                    final Optional<String> optional = ConvertUtils.convertIfNecessary(o, String.class);
                    final String item = element.attributeValue("item");
                    final String s = (Objects.nonNull(item) ? item + "," : "") + optional.orElse(null);
                    element.addAttribute("item", s);
                } else {
                    System.out.println("其他自定义类...");
                    addAttribute(element, basicClasses, label, o);
                }
            }
        } else if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            Element element = root.addElement(label);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (basicClasses.contains(entry.getValue().getClass())) {
                    final Optional<String> optional = ConvertUtils.convertIfNecessary(entry.getValue(), String.class);
                    optional.ifPresent(s -> element.addAttribute(entry.getKey(), s));
                } else {
                    addAttribute(element, basicClasses, entry.getKey(), entry.getValue());
                }
            }
        } else if (basicClasses.contains(value.getClass())) {
            final Optional<String> optional = ConvertUtils.convertIfNecessary(value, String.class);
            optional.ifPresent(s -> root.addAttribute(label, s));
        } else {
            // 自定义class对象类
            final JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(value));
            final String simpleName = value.getClass().getSimpleName();
            final Optional<String> optional = StringUtils.unCapitalize(simpleName);
            if (!optional.isPresent()) {
                return;
            }
            Element element = root.addElement(optional.get());

            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if (ObjectUtils.isNull(entry.getValue())) {
                    continue;
                }
                addAttribute(element, basicClasses, entry.getKey(), entry.getValue());
            }
        }
    }

    private static void updateAttribute(Element root, List<Class<?>> basicClasses, String label, Object value) {
        final List<Class<?>> classes = Arrays.asList(BASIC_CLAZZ);
        if (value instanceof Collection) {
            Element element = root.element(label);
            if (Objects.isNull(element)) {
                addElement(root, classes, label, value);
            } else {
                Collection collection = (Collection) value;
                for (Object o : collection) {
                    if (basicClasses.contains(o.getClass())) {
                        final Optional<String> optional = ConvertUtils.convertIfNecessary(o, String.class);
                        final String item = element.attributeValue("item");
                        final String s = (Objects.nonNull(item) ? item + "," : "") + optional.orElse(null);
                        element.addAttribute("item", s);
                    } else {
                        updateAttribute(element, basicClasses, label, o);
                    }
                }
            }
        } else if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            Element element = root.element(label);

            // 元素不存在的情况
            if (Objects.isNull(element)) {
                addAttribute(root, classes, label, value);
            } else {
                // 元素存在的情况
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (basicClasses.contains(entry.getValue().getClass())) {
                        final Optional<String> optional = ConvertUtils.convertIfNecessary(entry.getValue(), String.class);
                        optional.ifPresent(s -> element.addAttribute(entry.getKey(), s));
                    } else {
                        addAttribute(element, basicClasses, entry.getKey(), entry.getValue());
                    }
                }
            }
        } else if (basicClasses.contains(value.getClass())) {
            final Optional<String> optional = ConvertUtils.convertIfNecessary(value, String.class);
            optional.ifPresent(s -> root.attributeValue(label, s));
        } else {
            // 自定义class对象类
            final JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(value));
            final String simpleName = value.getClass().getSimpleName();
            final Optional<String> optional = StringUtils.unCapitalize(simpleName);
            if (!optional.isPresent()) {
                return;
            }
            Element element = root.element(optional.get());
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if (ObjectUtils.isNull(entry.getValue())) {
                    continue;
                }
                if (ObjectUtils.isNull(element)) {
                    element = root.addElement(optional.get());
                    addAttribute(element, basicClasses, entry.getKey(), entry.getValue());
                } else {
                    updateAttribute(element, basicClasses, entry.getKey(), entry.getValue());
                }
            }
        }
    }


    /**
     * 集合类型数据更新元素
     *
     * @param basicClasses
     * @param label
     * @param collection
     * @param element
     */
    private static void updateElementCollectionType(List<Class<?>> basicClasses, String label, Collection collection, Element element) {
        for (Object o : collection) {
            if (basicClasses.contains(o.getClass())) {
                final List<Element> elements = element.elements();
                final Optional<String> optional = ConvertUtils.convertIfNecessary(o, String.class);
                if (CollectionUtils.isEmpty(elements)) {
                    element.addElement("item").setText(optional.orElse(null));
                } else {
                    final Optional<Element> first = elements.stream()
                            .filter(e -> ObjectUtils.equals(e.getText(), optional.orElse(null)))
                            .findFirst();
                    if (first.isPresent()) {
                        first.get().setText(optional.orElse(null));
                    } else {
                        element.addElement("item").setText(optional.orElse(null));
                    }
                }
            } else {
//                System.out.println("其他自定义类...");
                updateElement(element, basicClasses, label, o);
            }
        }
    }

    // endregion

}
