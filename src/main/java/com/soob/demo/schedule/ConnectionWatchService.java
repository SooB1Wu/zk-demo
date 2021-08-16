package com.soob.demo.schedule;

import com.soob.demo.entity.ZookeeperServer;
import com.soob.demo.service.IServerInfoService;
import com.soob.demo.utils.WebSocketServer;
import com.soob.demo.utils.ZookeeperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时任务
 * 每分钟向zk服务器发送心跳
 */
@Service
@Slf4j
public class ConnectionWatchService {
    @Autowired
    ZookeeperUtil zookeeperUtil;
    @Autowired
    IServerInfoService infoService;
    @Autowired
    List<ZookeeperServer> zkList;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void ConnWatch(){//每分钟检测一次
        String data=zookeeperUtil.getData("/");
        log.info("[心跳]=>{}",data);
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void zkServerWatch() throws IOException {
        boolean flag=false;
        /*使用String记录服务器状态,避免引用问题*/
        List<String> oldList=new ArrayList<>();
        for(ZookeeperServer zk:zkList){
            oldList.add(zk.toString());
        }
        /*更新zkList(zk服务信息)
        *
        * */
        List<String> newList=infoService.getServerInfo(zkList);

        /*对比更新前与更新后的zkList,有变化则说明服务器状态更改,发送websocket请求*/
        for(int i=0;i<3;i++){
            if(!oldList.get(i).equals(newList.get(i))){
                flag=true;
            }
        }
        if(flag){
            WebSocketServer.sendInfo("ServerStatChanged");
        }
        log.info("[服务器状态更改:{}]",flag);
    }
}
