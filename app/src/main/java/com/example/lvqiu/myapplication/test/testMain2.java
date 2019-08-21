package com.example.lvqiu.myapplication.test;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Stack;

public class testMain2 {

    public static void main(String[] args){
        Node node1=new Node("1");
        Node node2=new Node("2");
        Node node3=new Node("3");
        Node node4=new Node("4");
        Node node5=new Node("5");
        Node node6=new Node("6");

        Node node=new Node("0",node1,node2);
        node2.left=node3;
        node2.right=node4;
        node3.left=node5;
        node1.left=node6;

        System.out.println("前序遍历");
        pre(node);
        System.out.println("中序遍历");
        mid(node);
    }

    static class Node{
        public String key;
        public Node left;
        public Node right;

        public Node() {
        }

        public Node(String key) {
            this.key = key;
        }

        public Node(String key, Node left, Node right) {
            this.key = key;
            this.left = left;
            this.right = right;
        }
    }

    public static void mid(Node root){
        Stack<Node> stack=new Stack<>();
        Node temp=root;
        while (temp!=null || !stack.isEmpty()) {
            while (temp != null) {
                stack.push(temp);
                temp = temp.left;
            }
            if (!stack.isEmpty()){
                temp=stack.pop();
                System.out.print("，"+temp.key);
                temp=temp.right;
            }
        }
    }

    public static void pre(Node root){
        Stack<Node> stack=new Stack<>();
        Node temp=root;
        while (temp!=null || !stack.isEmpty()) {
            while (temp != null) {
                stack.push(temp);
                System.out.print("，"+temp.key);
                temp = temp.left;
            }
            if (!stack.isEmpty()){
                temp=stack.pop();
                temp=temp.right;
            }
        }
    }
}
