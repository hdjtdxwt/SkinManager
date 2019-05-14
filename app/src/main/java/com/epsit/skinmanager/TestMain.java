package com.epsit.skinmanager;

import java.util.ArrayList;
import java.util.List;

public class TestMain {
    public static void main(String args[]){
        /**
         * 证明只生成了一个类，两个实例共享
         */
        // 声明一个具体类型为String的ArrayList
        ArrayList<String> arrayList1 = new ArrayList<String>();
        arrayList1.add("abc");

        // 声明一个具体类型为Integer的ArrayList
        ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
        arrayList2.add(123);

        // 结果为true
        System.out.println(arrayList1.getClass() == arrayList2.getClass());


        /**
         * 证明了在编译后，擦除了Integer这个泛型信息，只保留了原始类型
         */
        ArrayList<Integer> arrayList3 = new ArrayList<Integer>();
        arrayList3.add(1);
        try {
            arrayList3.getClass().getMethod("add", Object.class).invoke(arrayList3, "asd");
            for (int i = 0; i < arrayList3.size(); i++) {
                System.out.println(arrayList3.get(i)); // 输出1，asd
            }
            // NoSuchMethodException：java.util.ArrayList.add(java.lang.Integer
            arrayList3.getClass().getMethod("add", Integer.class).invoke(arrayList3, 2);
        }catch (Exception e){
            e.printStackTrace();
        }


        Number num = new Integer(1);
        //type mismatch
        //ArrayList<Number> list = new ArrayList<Integer>();//后面的类型Integer与前面类型不同的话会编译报错

        List<? extends Number> list = new ArrayList<>();
        //list.add(num);
    }


}
