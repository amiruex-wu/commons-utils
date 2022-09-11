package org.wch.commons.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TODO
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2022/9/4 17:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvertorCurrentClass {

    private Class<?> sourceClass;

    private Class<?> targetClass;

}
