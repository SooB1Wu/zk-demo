package com.soob.demo.controller;

import com.soob.demo.entity.CommonResult;
import com.soob.demo.service.IRestSDK;
import com.soob.demo.service.IServerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Scope("session")
@Slf4j
public class RestControl {
    @Autowired
    IRestSDK restSDK;
    @Autowired
    IServerInfoService infoService;

    /**
     * 将json转换为Map
     * @param json
     * @return
     */
    private Map<String,String> formatJson(String json){
        if(json.length()<=2){
            return new HashMap<>();
        }
        log.info("json=>{}",json);
        json=json.substring(1,json.length()-1);
        String[] formatStr=json.split(",");
        Map<String,String> res=new HashMap<>();
        for(int i=0;i<formatStr.length;i++){
            String[] strArr=formatStr[i].split(":");
            String pram=strArr[0].substring(1,strArr[0].length()-1);//去除前后引号
            String value=strArr[1].substring(1,strArr[1].length()-1);//去除前后引号
            res.put(pram,value);
        }
        return res;
    }

    /**
     * 测试
     * @return
     */
    @RequestMapping("/test")
    @ResponseBody
    public CommonResult postTest(){
        log.info("接收请求=>test");
        return new CommonResult(200,"Post Success");
    }

    /**
     * 请求服务信息
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public CommonResult getServerInfo(){
        log.info("接收请求=>info");
        return restSDK.getServerInfo();
    }

    /**
     * 判断是否存在
     * @param reqJson
     * @return
     */
    @RequestMapping("/exists")
    @ResponseBody
    public CommonResult postExists(@RequestBody String reqJson){
        log.info("接收请求=>exists");
        return restSDK.exists(formatJson(reqJson).get("path"));
    }

    /**
     * 新建节点
     * @param reqJson
     * @return
     */
    @RequestMapping("/create")
    @ResponseBody
    public CommonResult createNode(@RequestBody String reqJson){
        log.info("接收请求=>create");
        Map<String,String> params=formatJson(reqJson);
        return restSDK.createNode(params.get("path"),params.get("data"));
    }

    /**
     * 更新节点值
     * @param reqJson
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public CommonResult updateNode(@RequestBody String reqJson){
        log.info("接收请求=>update");
        Map<String,String> params=formatJson(reqJson);
        return restSDK.updateNode(params.get("path"),params.get("data"));
    }

    /**
     * 查询节点值
     * @param reqJson
     * @return
     */
    @RequestMapping("/query")
    @ResponseBody
    public CommonResult getNodeValue(@RequestBody String reqJson){
        log.info("接收请求=>query");
        Map<String,String> params=formatJson(reqJson);
        return restSDK.getData(params.get("path"));
    }

    /**
     * 删除节点(将递归删除当前节点下的子节点与数据)
     * @param reqJson
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    @RequestMapping("/delete")
    @ResponseBody
    public CommonResult deleteNode(@RequestBody String reqJson){
        log.info("接收请求=>delete");
        Map<String,String> params=formatJson(reqJson);
        return restSDK.deleteNode(params.get("path"));
    }

    /**
     * 查询子节点
     * @param reqJson
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    @RequestMapping("/children")
    @ResponseBody
    public CommonResult getChildrenList(@RequestBody String reqJson){
        log.info("接收请求=>children");
        Map<String,String> params=formatJson(reqJson);
        return restSDK.getChildren(params.get("path"));
    }

    /**
     * 获取当前zk目录结构
     */
    @RequestMapping("/tree")
    @ResponseBody
    public CommonResult getPathList(){
        log.info("接收请求=>tree");
        return restSDK.getPathStruct();
    }

}
