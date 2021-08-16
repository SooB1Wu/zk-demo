package com.soob.demo.service;

import com.soob.demo.entity.CommonResult;

/**
 * 增删改查服务,调用客户端进行操作
 */
public interface IRestSDK {
    CommonResult exists(String path);
    CommonResult getData(String path);
    CommonResult getChildren(String path);
    CommonResult deleteNode(String path);
    CommonResult updateNode(String path, String data);
    CommonResult createNode(String path, String data);
    CommonResult getPathStruct();
    CommonResult getServerInfo();
}
