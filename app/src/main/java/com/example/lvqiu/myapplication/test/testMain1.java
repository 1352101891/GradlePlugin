package com.example.lvqiu.myapplication.test;

class p{
    public String s="p";

    public p(String s) {
        this.s = s;
        System.out.println("s："+s);
    }
}

class q extends p{
    public q(String s) {
        super(s);
    }
    public String s="q";
}

public class testMain1 {
    private static p test=new p("测试");
    private static int price=20;
    private int disoff;

    static {
        try {
            Thread.sleep(1000);
            System.out.println("sleep:"+1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public testMain1(int disoff) {
        System.out.println("testMain1构造函数disoff"+disoff);
        this.disoff = disoff;
    }

    public static void main(String[] args){
        p p1=new q("aaa");
        System.out.println("是："+(p1).s);
        System.out.println(""+testMain1.price);
        testMain1 main1=new testMain1(1);
        System.out.println(""+main1.price);

        Long L1=111L;
        Long L2=111L;
        Long L3=112L;
        Long L4=112L;
        System.out.println("L1=L2:"+(L1==L2));
        System.out.println("L3=L4:"+(L3==L4));
        System.out.println("L3=112L:"+(L3==112L));
    }



}
