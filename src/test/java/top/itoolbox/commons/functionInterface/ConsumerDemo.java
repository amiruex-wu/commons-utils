package top.itoolbox.commons.functionInterface;

import java.util.function.Consumer;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-13 15:55
 */
public class ConsumerDemo {

    public static void main(String[] args) {
        //Lambda表达式
        operatorString("唐青枫",(String name)->{
            System.out.println(name);
        });

        //Lambda表达式
        operatorString("唐青枫",name-> System.out.println(name));

        //方法引用
        operatorString("唐青枫", System.out::println);

        //Lambda表达式
        operatorString("唐青枫",(String name)->{
            String s=new StringBuilder(name).reverse().toString();
            System.out.println(s);
        });
        System.out.println("****************************");
        //Lambda表达式
        operatorString("曲无忆",name-> System.out.println(name),name-> System.out.println(new StringBuilder(name).reverse().toString()));
    }

    //消费一个字符串数据
    private static void operatorString(String name, Consumer<String> con){
        con.accept(name);
    }
    //用不同的方消费同一个字符串数据两次
    private static void operatorString(String name,Consumer<String> con1,Consumer<String> con2){
//        con1.accept(name);
//        con2.accept(name);
        //上两行代码等价于
        con1.andThen(con2).accept(name);
    }
}
