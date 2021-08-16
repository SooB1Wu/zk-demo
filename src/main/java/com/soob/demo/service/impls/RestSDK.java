package com.soob.demo.service.impls;

import com.soob.demo.entity.CommonResult;
import com.soob.demo.entity.Node;
import com.soob.demo.entity.ZookeeperServer;
import com.soob.demo.service.IRestSDK;
import com.soob.demo.utils.ZookeeperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class RestSDK implements IRestSDK {
        @Autowired
        ZookeeperUtil zookeeperUtil;
        @Autowired
        List<ZookeeperServer> zkList;

        @Override
        public CommonResult exists(String path) {
            if (zookeeperUtil.exists(path)) {
                return new CommonResult(200, "节点存在");
            } else {
                return new CommonResult(400, "节点不存在");
            }
        }

        @Override
        public CommonResult getData(String path) {
            if (!zookeeperUtil.exists(path)) {
                return new CommonResult(401, "节点不存在");
            }
            String nodeData = zookeeperUtil.getData(path);
            return new CommonResult(200, "查询成功", nodeData);
        }

        @Override
        public CommonResult getChildren(String path) {
            if (!zookeeperUtil.exists(path)) {
                return new CommonResult(401, "节点不存在");
            }
            List<String> children = zookeeperUtil.getChildren(path);
            return new CommonResult(200, "查询成功", children);
        }

        @Override
        public CommonResult deleteNode(String path) {
            if (!zookeeperUtil.exists(path)) {
                return new CommonResult(401, "节点不存在");
            }
            boolean flag = dfs_deleteNode(path);
            if (flag) {
                return new CommonResult(200, "删除成功");
            } else {
                return new CommonResult(400, "删除失败");
            }
        }

        private boolean dfs_deleteNode(String path) {
            /*
             * 递归删除子节点*/
            List<String> children = zookeeperUtil.getChildren(path);
            for (String child : children) {
                if (!dfs_deleteNode(path + "/" + child)) {
                    return false;
                }
            }
            /*删除当前节点*/
            return zookeeperUtil.deleteNode(path);
        }

        @Override
        public CommonResult updateNode(String path, String data) {
            if (!zookeeperUtil.exists(path)) {
                return new CommonResult(401, "节点不存在");
            }
            boolean flag = zookeeperUtil.updateNode(path, data);
            if (flag) {
                return new CommonResult(200, "更新成功");
            } else {
                return new CommonResult(400, "更新失败");
            }
        }

        @Override
        public CommonResult createNode(String path, String data) {
            if (zookeeperUtil.exists(path)) {
                return new CommonResult(401, "节点已存在");
            }
            boolean flag = zookeeperUtil.createNode(path, data);
            if (flag) {
                return new CommonResult(200, "创建成功");
            } else {
                return new CommonResult(400, "创建失败");
            }
        }

        @Override
        public CommonResult getPathStruct() {
            Node root = new Node("/", "/");
            dfs(root, "/");
            Node head = new Node();
            head.getChildren().add(root);
            return new CommonResult(200, "获取成功", head);
        }

    @Override
    public CommonResult getServerInfo() {
        return new CommonResult(200,"Success",zkList);
    }

    private void dfs(Node node, String path) {
            if (!zookeeperUtil.exists(path)) {
                return;
            }
            List<String> children = zookeeperUtil.getChildren(path);
            if (children == null || children.size() == 0) {
                return;
            }
            //如果路径是"/"则替换为"",防止出现"//zk"的情况
            path = path.equals("/") ? "" : path;
            node.setChildren(new ArrayList<>());
            for (String child : children) {
                node.getChildren().add(new Node("/" + child, path + "/" + child));
                dfs(node.getChildren().get(node.getChildren().size() - 1), path + "/" + child);
            }

        }
    }