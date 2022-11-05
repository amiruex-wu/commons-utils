package org.wch.commons.callback;


import org.wch.commons.callableInterface.FileReaderCallable;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-07 15:15
 */
public class Son {
    public void rideTrain(FileReaderCallable fileReaderCallable) {
        try {
            //模拟坐火车
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileReaderCallable.call("I'm coming here now");//到了打电话给妈妈
    }

}
