package top.itoolbox.commons.enums;

public enum  XMLOperateType {
    /**
     * 普通模式
     * 示例：
     * <aClass>
     * <a>a</a>
     * <b>b</b>
     * <c>c</c>
     * <d>
     * <e>e</e>
     * <f>f</f>
     * </d>
     * </aClass>
     */
    GENERAL,
    /**
     * 属性解析模式
     * 示例：
     * <aClass a="a" b="b" c="c">
     * <d e="e" f="f"></d>
     * </aClass>
     */
    ATTRIBUTE
}
