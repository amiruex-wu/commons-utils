package org.wch.commons;

import org.wch.commons.lang.NumberUtils;
import org.wch.commons.lang.NumberUtils1;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.lang.StringUtils;
import org.wch.commons.beans.TreeNode;
import org.wch.commons.beans.TreesData;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Description: 树形构建工具类
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
public class TreeUtils {

    /**
     * 两层循环实现建树(原理：利用java的 引用传递 特性),使用ID作为主键
     *
     * @param treeNodes 传入的树节点列表
     * @return 树结构列表
     */
    public static <T extends TreeNode<T>> List<T> buildById(List<T> treeNodes) {
        if (ObjectUtils.isEmpty(treeNodes)) {
            return Collections.emptyList();
        }
        List<T> trees = new ArrayList<>();
        List<T> dataList = getDataList(treeNodes);
        for (T treeNode : dataList) {
            if (Objects.isNull(treeNode.getParentId())) {
                trees.add(treeNode);
            }
            for (T t : dataList) {
                if (ObjectUtils.nonNull(t.getParentId())
                        && treeNode.getId().compareTo(t.getParentId()) == 0) {
                    if (ObjectUtils.isEmpty(treeNode.getChildren())) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.getChildren().add(t);
                }
            }
        }
        return trees;
    }

    /**
     * 两层循环实现建树(原理：利用java的 引用传递 特性),使用ID作为主键
     *
     * @param treeNodes         树形结构数据
     * @param calculateChildNum 当前节点数据数量是否包含字节点的数据数量
     * @param mapper            自定义接口函数
     * @return 树结构列表
     */
    public static <T extends TreeNode<T>> List<T> buildById(List<T> treeNodes, boolean calculateChildNum, Consumer<T> mapper) {
        if (ObjectUtils.isEmpty(treeNodes)) {
            return Collections.emptyList();
        }
        List<T> trees = buildById(treeNodes);
        if (calculateChildNum) {
            calculateNodeCountContainChild(trees, mapper);
        } else {
            calculateNodeCountUnContainChild(trees, mapper);
        }
        return trees;
    }

    /**
     * 两层循环实现建树(原理：利用java的 引用传递 特性),使用UID作为主键
     *
     * @param treeNodes 传入的树节点列表
     * @return 树结构列表
     */
    public static <T extends TreeNode<T>> List<T> buildByUid(List<T> treeNodes) {
        if (ObjectUtils.isEmpty(treeNodes)) {
            return Collections.emptyList();
        }
        List<T> trees = new ArrayList<>();
        List<T> dataList = getDataList(treeNodes);
        for (T treeNode : dataList) {
            if (Objects.isNull(treeNode.getParentUid())) {
                trees.add(treeNode);
            }
            for (T t : dataList) {
                if (StringUtils.isNotBlank(t.getParentUid())
                        && StringUtils.equals(treeNode.getUid(), t.getParentUid())) {
                    if (ObjectUtils.isEmpty(treeNode.getChildren())) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.getChildren().add(t);
                }
            }
        }
        return trees;
    }

    /**
     * @param treeNodes         列表
     * @param calculateChildNum 当前节点数据数量是否包含字节点的数据数量
     * @param mapper            自定义接口函数
     * @return 树结构列表
     */
    public static <T extends TreeNode<T>> Optional<TreesData<T>> buildByUid(List<T> treeNodes, boolean calculateChildNum, Consumer<T> mapper) {
        if (ObjectUtils.isEmpty(treeNodes)) {
            return Optional.empty();
        }
        List<T> trees = buildByUid(treeNodes);
        TreesData<T> result;
        if (calculateChildNum) {
            int total = calculateNodeCountContainChild(trees, mapper);
            result = new TreesData<>((long) total, trees);
        } else {
            calculateNodeCountUnContainChild(trees, mapper);
            result = new TreesData<>(trees);
        }
        return Optional.of(result);
    }

    /**
     * 构建前端所需要树结构,stream流模式遍历（原理：递归）使用ID作为主键
     *
     * @param treeNodes 列表
     * @return 树结构列表
     */
    public static <T extends TreeNode<T>> List<T> buildTreeById(List<T> treeNodes) {
        if (ObjectUtils.isEmpty(treeNodes)) {
            return Collections.emptyList();
        }
        List<T> dataList = getDataList(treeNodes);
        // get all root node
        List<T> collect = dataList.stream()
                .filter(item -> ObjectUtils.isNull(item.getParentId()) || Objects.equals(item.getParentId(), 0L))
                .peek(item -> {
                    List<T> children = getChildrenListById(item, dataList);
                    item.setChildren(children);
                })
                .collect(Collectors.toList());
        return collect;
    }

    /**
     * 构建前端所需要树结构,stream流模式遍历（原理：递归）使用ID作为主键
     *
     * @param treeNodes         列表
     * @param calculateChildNum 当前节点数据数量是否包含字节点的数据数量
     * @param mapper            自定义接口函数
     * @return 树结构列表
     */
    public static <T extends TreeNode<T>> List<T> buildTreeById(List<T> treeNodes, boolean calculateChildNum, Consumer<T> mapper) {
        if (ObjectUtils.isEmpty(treeNodes)) {
            return Collections.emptyList();
        }
        List<T> trees = buildTreeById(treeNodes);
        if (calculateChildNum) {
            calculateNodeCountContainChild(trees, mapper);
        } else {
            calculateNodeCountUnContainChild(trees, mapper);
        }
        return trees;
    }

    /**
     * 构建前端所需要树结构,stream流模式遍历（原理：递归）使用UID作为主键
     *
     * @param treeNodes
     * @param <T>
     * @return
     */
    public static <T extends TreeNode<T>> List<T> buildTreeByUid(List<T> treeNodes) {
        if (ObjectUtils.isEmpty(treeNodes)) {
            return Collections.emptyList();
        }
        List<T> dataList = getDataList(treeNodes);

        // get all root node
        List<T> collect = dataList.stream()
                .filter(item -> StringUtils.isBlank(item.getParentUid()))
                .peek(item -> {
                    List<T> children = getChildrenListByUid(item, dataList);
                    item.setChildren(children);
                })
                .collect(Collectors.toList());
        return collect;
    }

    /**
     * 构建前端所需要树结构,stream流模式遍历（原理：递归）使用UID作为主键
     *
     * @param treeNodes         列表
     * @param calculateChildNum 当前节点数据数量是否包含字节点的数据数量
     * @param mapper            自定义接口函数
     * @return
     */
    public static <T extends TreeNode<T>> List<T> buildTreeByUid(List<T> treeNodes, boolean calculateChildNum, Consumer<T> mapper) {
        if (ObjectUtils.isEmpty(treeNodes)) {
            return Collections.emptyList();
        }
        List<T> trees = buildTreeByUid(treeNodes);
        if (calculateChildNum) {
            calculateNodeCountContainChild(trees, mapper);
        } else {
            calculateNodeCountUnContainChild(trees, mapper);
        }
        return trees;
    }

    // region 私有方法
    private static <T extends TreeNode<T>> void calculateNodeCountUnContainChild(List<T> treeNodes, Consumer<T> mapper) {
        if (ObjectUtils.anyNull(treeNodes, mapper)) {
            return;
        }
        for (T treeNode : treeNodes) {
            treeNode.eval(treeNode, mapper);
            // todo
            System.out.println("uid:" + treeNode.getUid() + "->" + treeNode.getDataNum());

            treeNode.setIsLeaf(ObjectUtils.isNotEmpty(treeNode.getChildren()));
            if (ObjectUtils.isNotEmpty(treeNode.getChildren())) {
                calculateNodeCountUnContainChild(treeNode.getChildren(), mapper);
            }
        }
    }

    private static <T extends TreeNode<T>> int calculateNodeCountContainChild(List<T> treeNodes, Consumer<T> mapper) {
        if (ObjectUtils.anyNull(treeNodes, mapper)) {
            return 0;
        }
        int rowCount = 0;
        for (T treeNode : treeNodes) {
            if (ObjectUtils.nonNull(mapper)) {
                treeNode.eval(treeNode, mapper);
            }
            rowCount += NumberUtils.toIntIfNull(treeNode.getDataNum());
            if (ObjectUtils.isNotEmpty(treeNode.getChildren())) {
                int count = calculateNodeCountContainChild(treeNode.getChildren(), mapper);
                treeNode.setIsLeaf(count <= 0);
                treeNode.setDataNum(count + NumberUtils.toIntIfNull(treeNode.getDataNum()));
                rowCount += count;
            }
        }
        return rowCount;
    }

    private static <T extends TreeNode<T>> List<T> getDataList(List<T> treeNodes) {
        boolean present = treeNodes.stream().anyMatch(item -> ObjectUtils.isNull(item.getSequence()));
        List<T> dataList;
        if (!present) {
            dataList = treeNodes.stream()
                    .sorted(Comparator.comparing(TreeNode::getSequence))
                    .collect(Collectors.toList());
        } else {
            dataList = treeNodes;
        }
        return dataList;
    }

    /**
     * 获取子节点列表
     *
     * @param root
     * @param dataList
     * @param <T>
     * @return
     */
    private static <T extends TreeNode<T>> List<T> getChildrenListById(T root, List<T> dataList) {
        if (ObjectUtils.anyNull(root, dataList)) {
            return Collections.emptyList();
        }
        List<T> collect = dataList.stream()
                .filter(item -> ObjectUtils.nonNull(item.getParentId()) && Objects.equals(item.getParentId(), root.getId()))
                .peek(item -> {
                    List<T> children = getChildrenListById(item, dataList);
                    item.setChildren(children);
                })
                .collect(Collectors.toList());
        return collect;
    }

    /**
     * 获取子节点列表
     *
     * @param root
     * @param dataList
     * @param <T>
     * @return
     */
    private static <T extends TreeNode<T>> List<T> getChildrenListByUid(T root, List<T> dataList) {
        if (ObjectUtils.anyNull(root, dataList)) {
            return Collections.emptyList();
        }
        List<T> collect = dataList.stream()
                .filter(item -> Objects.equals(item.getParentUid(), root.getUid()))
                .peek(item -> {
                    List<T> children = getChildrenListByUid(item, dataList);
                    item.setChildren(children);
                })
                .collect(Collectors.toList());
        return collect;
    }

}
