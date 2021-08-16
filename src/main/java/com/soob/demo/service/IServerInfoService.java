package com.soob.demo.service;

import com.soob.demo.entity.ZookeeperServer;
import java.util.List;

public interface IServerInfoService {
    List<String> getServerInfo(List<ZookeeperServer> zkServer);
}
