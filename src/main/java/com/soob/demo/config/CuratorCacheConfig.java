package com.soob.demo.config;

import com.soob.demo.utils.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class CuratorCacheConfig{
    @Autowired
    CuratorFramework zkClient;

    /**
     * 监听/servers目录
     * 若有节点变化则更新服务器状态,同时重新写入
     */
    @Bean
    public void setConnectionWatcher() throws Exception {
        TreeCache treeCache= new TreeCache(zkClient,"/servers");
        treeCache.start();
        treeCache.getListenable().addListener((client, event) -> {
            try{
                if (event.getType()==TreeCacheEvent.Type.NODE_ADDED||
                        event.getType()==TreeCacheEvent.Type.NODE_REMOVED){

                    log.info("服务器状态更新!",event.getData().getPath());
                    /*重新获取服务器状态,且重新创建临时节点*/
                    WebSocketServer.sendInfo("");
                }
            } catch (IOException e) {
                log.info("[websocket异常]");
            }
        });
    }

    @Bean
    public void setTreeCache() throws Exception {
        TreeCache treeCache= new TreeCache(zkClient,"/");
        treeCache.start();
        treeCache.getListenable().addListener((client, event) -> {
            try{
                if (event.getType()==TreeCacheEvent.Type.NODE_ADDED){
                    log.info("检测到节点增加=>{}",event.getData().getPath());
                    WebSocketServer.sendInfo("nodeAdd");
                }
                else if (event.getType()==TreeCacheEvent.Type.NODE_REMOVED){
                    log.info("检测到节点删除=>{}",event.getData().getPath());
                    WebSocketServer.sendInfo("nodeRm");
                }
                else if (event.getType()==TreeCacheEvent.Type.NODE_UPDATED){
                    log.info("检测到节点修改=>{}",event.getData().getPath());
                    WebSocketServer.sendInfo("nodeUpdate");
                }
                else if(event.getType()== TreeCacheEvent.Type.CONNECTION_LOST){
                    log.info("检测到服务器失去连接!");
                    WebSocketServer.sendInfo("ConLost");
                }
                else if(event.getType()== TreeCacheEvent.Type.CONNECTION_RECONNECTED){
                    log.info("服务器重连!");
                    WebSocketServer.sendInfo("ReConnected");
                }
                else if(event.getType()== TreeCacheEvent.Type.CONNECTION_SUSPENDED){
                    log.info("检测到服务器连接终止");
                    WebSocketServer.sendInfo("ConSuspended");
                }
                else if (event.getType()== TreeCacheEvent.Type.INITIALIZED){
                    log.info("初始化完成!");
                }
            } catch (IOException e) {
                log.info("[websocket异常]");
            }
        });
    }
}
