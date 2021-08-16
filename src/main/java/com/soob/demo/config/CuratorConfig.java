package com.soob.demo.config;

import com.soob.demo.entity.ZookeeperServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class CuratorConfig {
    /*用于检查连接*/
    @Value("${zookeeper.zkAddress}")
    public static String urls;

    @Value("${zookeeper.sessionTimeout}")
    private int sessionTimeout;
    @Value("${zookeeper.connectionTimeout}")
    private int connectionTimeout;
    @Value("${zookeeper.zkAddress}")
    private String zkAddress;
    @Value("${zookeeper.server1.address}")
    private String zkAddress1;
    @Value("${zookeeper.server2.address}")
    private String zkAddress2;
    @Value("${zookeeper.server3.address}")
    private String zkAddress3;

    private CuratorFramework zkClient;

    @Bean(name = "zkClient")
    public CuratorFramework createCrudClient(){
        return createClient(zkAddress,connectionTimeout,sessionTimeout);
    }
//    @Bean
    public List<CuratorFramework> createClientList(){
        List<CuratorFramework> list=new ArrayList<>();
        for(int i=0;i<3;i++){
            String address=null;
            switch (i){
                case 0:
                    address=zkAddress1;
                    break;
                case 1:
                    address=zkAddress2;
                    break;
                case 2:
                    address=zkAddress3;
                    break;
                default:
                    break;
            }
            CuratorFramework client=createClient(address,connectionTimeout,sessionTimeout);
            list.add(client);
        }
        return list;
    }

    private CuratorFramework createClient(String address,int conTimeout,int sesTimeout){
        //重连策略
        RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,10);
        //客户端对象配置
        zkClient= CuratorFrameworkFactory.builder()
                .connectString(address)
                .connectionTimeoutMs(conTimeout)
                .sessionTimeoutMs(sesTimeout)
                .retryPolicy(retryPolicy)
                .namespace("")
                .build();
        zkClient.start();
        return zkClient;
    }



    @Bean
    public List<ZookeeperServer> createServerList(){
        List<ZookeeperServer> list=new ArrayList<>();
        for(int i=0;i<3;i++){
            String host;
            String port;
            String address = null;
            switch (i){
                case 0:
                    address=zkAddress1;
                    break;
                case 1:
                    address=zkAddress2;
                    break;
                case 2:
                    address=zkAddress3;
                    break;
                default:
                    break;
            }
            String[] cache=address.split(":");
            host=cache[0];
            port=cache[1];
            ZookeeperServer zkServer=new ZookeeperServer();
            zkServer.setId(i);
            zkServer.setHost(host);
            zkServer.setPort(port);
            list.add(zkServer);
        }
        return list;
    }

}
