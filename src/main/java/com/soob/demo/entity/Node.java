package com.soob.demo.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * zk服务器路径节点信息
 */
@Data
public class Node {
    private int id;
    private String path;
    private String absolutePath;
    private List<Node> children;

    public Node(String path, String absolutePath) {
        this.path = path;
        this.absolutePath = absolutePath;
    }

    public Node(){
        this.children=new ArrayList<>();
    }

}
