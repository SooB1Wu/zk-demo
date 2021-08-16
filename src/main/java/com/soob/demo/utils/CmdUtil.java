package com.soob.demo.utils;

import com.soob.demo.entity.ZookeeperServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
@Component
@Slf4j
public class CmdUtil {
    public String getInfo(ZookeeperServer zkServer){
        String res=null;
        String host=zkServer.getHost();
        String cmd="stat";
        int port=Integer.valueOf(zkServer.getPort());
        try {
            Socket sock = new Socket(host, port);
            OutputStream outstream = sock.getOutputStream();
            // 通过Zookeeper的四字命令获取服务器的状态
            outstream.write(cmd.getBytes());
            outstream.flush();
            sock.shutdownOutput();

            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("Mode: ") != -1) {
                    res=line.replaceAll("Mode: ", "").trim();
//                    log.info("[Mode:]{}",res);
                    /*配置服务器对象*/
                    zkServer.setStatus("Connected");
                    zkServer.setMode(res);
                    return res;
                }
            }
            sock.close();
            if (reader != null) {
                reader.close();
            }
        }catch (ConnectException e) {
//            log.info("[Mode:]DisConnected");
            zkServer.setStatus("DisConnected");
            zkServer.setMode("NULL");
            return "Lost";
        }catch (IOException e){
            log.info("IO异常");
            return null;
        }
        return null;
    }
}
