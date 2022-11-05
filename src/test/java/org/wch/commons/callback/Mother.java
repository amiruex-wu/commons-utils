package org.wch.commons.callback;


import org.wch.commons.callableInterface.FileReaderCallable;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-07 15:14
 */
public class Mother implements FileReaderCallable {
    Son son;

    public Mother(Son son) {
        this.son = son;
    }

    // 表示妈妈和儿子的分别函数，儿子在这期间搭乘火车离开
    public void parting() {
        System.out.println("start execute synchronize callback function 开始执行同步回调函数");
        son.rideTrain(this);
        System.out.println("同步回调函数执行完成");
    }

    @Override
    public void call(Object t) {
        System.out.println("儿子到学校了," + t);
    }
}
