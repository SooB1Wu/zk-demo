package com.soob.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Slf4j
public class ZookeeperUtil {

    @Autowired
    private CuratorFramework zkClient;

    /**
     * 判断节点是否存在并设置监听时间
     * 三种监听类型: 创建,删除,更新
     */
    public boolean exists(String path){
        try{
            return zkClient.checkExists().forPath(path)!=null;
        } catch (Exception e) {
            log.error("指定节点存在异常{},{}",path,e);
            return false;
        }
    }

    /**
     * 增加持久化节点
     */
    public boolean createNode(String path,String data){
        try {
            zkClient.create().forPath(path,data.getBytes());
            return true;
        } catch (Exception e) {
            log.error("创建节点异常=>{},{},{}",path,data,e);
            return false;
        }
    }

    /**
     * 修改持久化节点
     * @param path
     * @param data
     */
    public boolean updateNode(String path, String data){
        try {
            zkClient.setData().forPath(path,data.getBytes());
            return true;
        } catch (Exception e) {
            log.error("【修改持久化节点异常】{},{},{}",path,data,e);
            return false;
        }
    }

    /**
     * 删除持久化节点
     * @param path
     */
    public boolean deleteNode(String path){
        try {
            //version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zkClient.delete().forPath(path);
            return true;
        } catch (Exception e) {
            log.error("【删除持久化节点异常】{},{}",path,e);
            return false;
        }
    }


    /**
     * 获取当前节点的子节点(不包含孙子节点)
     * @param path 父节点path
     */
    public List<String> getChildren(String path){
        try{
            return zkClient.getChildren().forPath(path);
        } catch (Exception e) {
            log.info("[查询子节点异常]:{},{}",path,e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取指定节点的值
     * @param path
     * @return
     */
    public  String getData(String path){
        try {
            return  new String(zkClient.getData().forPath(path));
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }
}
