package com.atguigu.gmall.manager.components;

import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component   //单例的
public class FastDFSTemplate {

    @Value("${gmall.file.server}")
    private String fileServer;

    public FastDFSTemplate(){
        //初始化流程
        //1、加载配置文件
        String file = this.getClass().getResource("/tracker.conf").getFile();
        //2、初始化fastdfs客户端配置
        try {
            ClientGlobal.init(file);
        } catch (IOException e) {
           log.error("FastDFS客户端插件初始化异常：{}",e);
        } catch (MyException e) {
            log.error("FastDFS客户端插件初始化异常：{}",e);
        }

    }

    /**
     * 获取一个StorageClient
     * @return
     * @throws IOException
     */
    public StorageClient getStorageClient() throws IOException {
        //上传流程
        //1、创建TrackerClient
        TrackerClient trackerClient=new TrackerClient();

        //2、获取到和TrackerServer的连接
        TrackerServer trackerServer=trackerClient.getConnection();

        //3、根据TrackerServer返回的信息创建出操作Storage的客户端
        StorageClient storageClient=new StorageClient(trackerServer,null);
        return storageClient;
    }

    public String getPath(String[] strings){

        String path = "http://"+fileServer+"/";
        path+=strings[0]+"/";
        path+=strings[1];

        return path;
    }
}
