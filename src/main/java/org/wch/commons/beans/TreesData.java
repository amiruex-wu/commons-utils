package org.wch.commons.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wch.commons.lang.ObjectUtils;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-12 16:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreesData<T> implements Serializable {

    /**
     * 数据总量
     */
    private Long total;

    /**
     * 树形结构
     */
    private List<T> trees;

    public TreesData(List<T> trees) {
        this.trees = trees;
    }
}
