package com.soob.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * zk服务实体
 *
 */
@Data
@NoArgsConstructor
public class ZookeeperServer {
    private int id;
    private String host;
    private String port;
    private String mode;
    private String status;
}
