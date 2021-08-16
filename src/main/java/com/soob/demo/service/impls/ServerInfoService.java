package com.soob.demo.service.impls;

import com.soob.demo.entity.ZookeeperServer;
import com.soob.demo.service.IServerInfoService;
import com.soob.demo.utils.CmdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ServerInfoService implements IServerInfoService {
    @Value("${zookeeper.zkAddress}")
    private String address;
    @Autowired
    CmdUtil cmdUtil;

    @Override
    public List<String> getServerInfo(List<ZookeeperServer> servers){
        List<String> zkList=new ArrayList<>();
        for(ZookeeperServer server:servers){
            cmdUtil.getInfo(server);
            zkList.add(server.toString());
    }
        return zkList;
    }

}
