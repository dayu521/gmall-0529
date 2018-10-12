package com.atguigu.gmall.manager.controller;

import com.atguigu.gmall.manager.components.FastDFSTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequestMapping("/file")
@Controller
public class FileController {

    //上传成功返回文件的url访问地址

    @Autowired
    FastDFSTemplate fastDFSTemplate;

    /**
     *  MultipartFile file
     *
     * <input type="file" name="file"  multiple/>
     * @param file  接受一个上传来的文件
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload")
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            //xxxxx.jpg
            String filename = file.getOriginalFilename();
            String name = file.getName();
            long size = file.getSize();
            log.info("文件项：{}；名字：{}；大小：{}；上传成功",name,filename,size);

            //获取StorageClient来存储文件
            StorageClient storageClient = fastDFSTemplate.getStorageClient();

            //获取文件扩展名
            String ext = StringUtils.substringAfterLast(filename, ".");

            try {
                //返回真正的访问路径
                String[] strings = storageClient.upload_file(file.getBytes(), ext, null);
                String path = fastDFSTemplate.getPath(strings);
                return path;
            } catch (MyException e) {
               log.error("文件上传出错：{}",e);
            }

        }
        //把地址返回别人就能看了
        return "/images/error.png";
    }
}
