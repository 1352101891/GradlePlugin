package com.example.lvqiu.myapplication.test;

import android.support.annotation.NonNull;

import java.util.ArrayList;
public class testMain {

    final static ArrayList<String> list=new ArrayList();
    public static void main(String[] args){
        for (int i=0;i<6;i++){
            list.add("商品"+i);
        }
        Thread[] addThreads=new Thread[5];
        for (int i=0;i<addThreads.length;i++){
            addThreads[i]=new AddThread("AddThread-"+i);
            addThreads[i].start();
        }
        Thread[] removeThreads=new Thread[5];
        for (int i=0;i<removeThreads.length;i++){
            removeThreads[i]=new RemoveThread("RemoveThread-"+i);
            removeThreads[i].start();
        }

    }


    static class AddThread extends Thread{

        public AddThread(@NonNull String name) {
            super(name);
        }

        @Override
        public void run() {
            super.run();
            synchronized(list){
                while (list.size()>5){
                    try {
                        System.out.println("满仓，请"+getName()+"等待！");
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                list.add("商品");
                System.out.println(getName()+"添加物品");
                list.notifyAll();
                System.out.println(getName()+"通知等待线程进行锁竞争！");
            }
        }
    }

    static class RemoveThread extends Thread{

        public RemoveThread(@NonNull String name) {
            super(name);
        }

        @Override
        public void run() {
            super.run();
            synchronized(list){
                while (list.size()==0){
                    try {
                        System.out.println("空仓，请"+getName()+"等待！");
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                list.remove(0);
                System.out.println(getName()+"移除物品！");
                list.notifyAll();
                System.out.println(getName()+"通知等待线程进行锁竞争！");
            }
        }
    }
}
