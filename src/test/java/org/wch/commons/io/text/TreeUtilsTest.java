package org.wch.commons.io.text;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.TreeUtils;
import org.wch.commons.text.GsonUtils;
import org.wch.commons.text.TreeNode;
import org.wch.commons.text.TreesData;

import java.util.*;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-12 16:35
 */
@RunWith(JUnit4.class)
public class TreeUtilsTest {

    private List treeNodes = new ArrayList<>();

    @Test
    public void testOne(){
        List<TreeNode> treeNodes = new ArrayList<>();
        TreeNode treeNode = new TreeNode(1L,"node 1",null);
        TreeNode treeNode1 = new TreeNode(2L,"node 2",null);
        TreeNode treeNode2 = new TreeNode(3L,"node 3",1L);
        TreeNode treeNode4 = new TreeNode(4L,"node 4",1L);
        TreeNode treeNode5 = new TreeNode(5L,"node 5",4L);
        TreeNode treeNode6 = new TreeNode(6L,"node 6",5L);
        TreeNode treeNode7 = new TreeNode(7L,"node 7",2L);
        treeNodes.add(treeNode);
        treeNodes.add(treeNode1);
        treeNodes.add(treeNode2);
        treeNodes.add(treeNode4);
        treeNodes.add(treeNode5);
        treeNodes.add(treeNode6);
        treeNodes.add(treeNode7);
        List<TreeNode> treeSelects1 = TreeUtils.buildTreeById(treeNodes);
        /*Gson gson = new Gson();
        String s = gson.toJson(treeSelects1);*/
        System.out.println("result is: \n"+ GsonUtils.toJSONStringWithNull(treeSelects1));
    }


    @Test
    public void testTwo(){
        List<TreeSelect> treeNodes = new ArrayList<>();
        TreeSelect treeNode = new TreeSelect(1L,"node 1",null);
        TreeSelect treeNode1 = new TreeSelect(2L,"node 2",null);
        TreeSelect treeNode2 = new TreeSelect(3L,"node 3",1L);
        TreeSelect treeNode4 = new TreeSelect(4L,"node 4",1L);
        TreeSelect treeNode5 = new TreeSelect(5L,"node 5",4L);
        TreeSelect treeNode6 = new TreeSelect(6L,"node 6",5L);
        TreeSelect treeNode7 = new TreeSelect(7L,"node 7",2L);
        treeNodes.add(treeNode);
        treeNodes.add(treeNode1);
        treeNodes.add(treeNode2);
        treeNodes.add(treeNode4);
        treeNodes.add(treeNode5);
        treeNodes.add(treeNode6);
        treeNodes.add(treeNode7);
        List<TreeSelect> treeSelects1 = TreeUtils.buildByUid(treeNodes);

//        List<TreeSelect> treeSelects1 = (List<TreeSelect>) treeNodes1;
        /*Gson gson = new Gson();
        String s = gson.toJson(treeSelects1);*/
        System.out.println("result is: \n"+ GsonUtils.toJSONStringWithNull(treeSelects1));
    }

    @Test
    public void testThree(){
        List<TreeSelect> treeNodes = new ArrayList<>();
        TreeSelect treeNode = new TreeSelect(1L,"node 1",null);
        TreeSelect treeNode1 = new TreeSelect(2L,"node 2",null);
        TreeSelect treeNode2 = new TreeSelect(3L,"node 3",1L);
        TreeSelect treeNode4 = new TreeSelect(4L,"node 4",1L);
        TreeSelect treeNode5 = new TreeSelect(5L,"node 5",4L);
        TreeSelect treeNode6 = new TreeSelect(6L,"node 6",5L);
        TreeSelect treeNode7 = new TreeSelect(7L,"node 7",2L);
        treeNodes.add(treeNode);
        treeNodes.add(treeNode1);
        treeNodes.add(treeNode2);
        treeNodes.add(treeNode4);
        treeNodes.add(treeNode5);
        treeNodes.add(treeNode6);
        treeNodes.add(treeNode7);
        List<TreeSelect> treeSelects1 = TreeUtils.buildTreeById(treeNodes);

//        List<TreeSelect> treeSelects1 = (List<TreeSelect>) treeNodes1;
        /*Gson gson = new Gson();
        String s = gson.toJson(treeSelects1);*/
        System.out.println("result is: \n"+ GsonUtils.toJSONStringWithNull(treeSelects1));
    }

    @Test
    public void testFour(){
        List<TreeSelect> treeNodes = new ArrayList<>();
        TreeSelect treeNode = new TreeSelect("1","node 1",null);
        TreeSelect treeNode1 = new TreeSelect("2","node 2",null);
        TreeSelect treeNode2 = new TreeSelect("3","node 3","1");
        TreeSelect treeNode4 = new TreeSelect("4","node 4","1");
        TreeSelect treeNode5 = new TreeSelect("5","node 5","4");
        TreeSelect treeNode6 = new TreeSelect("6","node 6","5");
        TreeSelect treeNode7 = new TreeSelect("7","node 7","2");
        treeNodes.add(treeNode);
        treeNodes.add(treeNode1);
        treeNodes.add(treeNode2);
        treeNodes.add(treeNode4);
        treeNodes.add(treeNode5);
        treeNodes.add(treeNode6);
        treeNodes.add(treeNode7);
        List<TreeSelect> treeSelects1 = TreeUtils.buildTreeByUid(treeNodes);

//        List<TreeSelect> treeSelects2 = TreeUtils.buildByUid(treeNodes);

//        List<TreeSelect> treeSelects1 = (List<TreeSelect>) treeNodes1;
        /*Gson gson = new Gson();
        String s = gson.toJson(treeSelects1);*/
        System.out.println("result is: \n"+ GsonUtils.toJSONString(treeSelects1));
//        System.out.println("result is1: \n"+ GJSONUtils.toJSONStringWithNull(treeSelects2, false));
    }


    @Test
    public void testFive(){
        List<TreeSelect> treeNodes = new ArrayList<>();
        TreeSelect treeNode = new TreeSelect("1","node 1",null);
        TreeSelect treeNode1 = new TreeSelect("2","node 2",null);
        TreeSelect treeNode2 = new TreeSelect("3","node 3","1");
        TreeSelect treeNode4 = new TreeSelect("4","node 4","1");
        TreeSelect treeNode5 = new TreeSelect("5","node 5","4");
        TreeSelect treeNode6 = new TreeSelect("6","node 6","5");
        TreeSelect treeNode7 = new TreeSelect("7","node 7","2");
        treeNodes.add(treeNode);
        treeNodes.add(treeNode1);
        treeNodes.add(treeNode2);
        treeNodes.add(treeNode4);
        treeNodes.add(treeNode5);
        treeNodes.add(treeNode6);
        treeNodes.add(treeNode7);
        Map<String, Integer> map = new HashMap<>();
        map.put("1",1);
        map.put("2",2);
        map.put("3",3);
        map.put("4",4);
        map.put("5",5);
        map.put("6",6);
        map.put("7",7);
        Optional<TreesData<TreeSelect>> optional = TreeUtils.buildByUid(treeNodes, true, (treeSelect) ->
                treeSelect.setDataNum(map.get(treeSelect.getUid())));
        System.out.println("result is: \n"+ (optional.map(treeSelectTreesData -> GsonUtils.toJSONStringWithNull(treeSelectTreesData.getTrees())).orElse(null)));
    }


}
