package org.wch.commons.beans;

import lombok.Data;
import org.wch.commons.lang.ObjectUtils;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-12 16:28
 */
@Data
public class TreeNode<T extends TreeNode<T>> implements Serializable {

    /**
     * 节点ID
     */
    private Long id;

    /**
     * 节点UUID
     */
    private String uid;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 编码
     */
    private String code;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 排序号
     */
    private Long sequence;

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 父节点UID
     */
    private String parentUid;

    /**
     * 子节点
     */
    private List<T> children;

    /**
     * 节点数据数量
     */
    private Integer dataNum;

    /**
     * 是否是叶子节点
     */
    private Boolean isLeaf;

    public TreeNode() {
    }

    public TreeNode(Long id, String label, Long parentId) {
        this.id = id;
        this.label = label;
        this.parentId = parentId;
    }

    public TreeNode(String uid, String label, String parentUid) {
        this.uid = uid;
        this.label = label;
        this.parentUid = parentUid;
    }

    public TreeNode(Long id, String label, Long sequence, Long parentId) {
        this.id = id;
        this.label = label;
        this.sequence = sequence;
        this.parentId = parentId;
    }

    public TreeNode(String uid, String label, Long sequence, String parentUid) {
        this.uid = uid;
        this.label = label;
        this.sequence = sequence;
        this.parentUid = parentUid;
    }

   /* public <R> R eval(T obj, Function<? super T, ? extends R> mapper) {
        if (ObjectUtils.isNull(obj)) {
            return null;
        }
        R apply = mapper.apply(obj);
        return apply;
    }*/


    public void eval(T obj, Consumer<T> mapper) {
        if (ObjectUtils.isNull(obj)) {
            return;
        }
        mapper.accept(obj);
    }
}
