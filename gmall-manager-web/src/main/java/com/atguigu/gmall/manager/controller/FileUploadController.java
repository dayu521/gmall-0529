package com.atguigu.gmall.manager.controller;

import com.atguigu.gmall.manager.utils.FastDFSUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequestMapping("/file")
@Controller
public class FileUploadController {

    @Autowired
    FastDFSUtils fastDFSUtils;
    /**
     * 文件上传成功返回 文件的实际访问地址
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException, MyException {

        if (!file.isEmpty()){
            log.info("文件:{}，大小：{}；上传完成",file.getOriginalFilename(),file.getSize());
            StorageClient storageClient = fastDFSUtils.getStorageClient();
            String ext = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");

            NameValuePair[] pairs = new NameValuePair[1];
            pairs[0] = new NameValuePair("fileName", file.getOriginalFilename());

            String[] strings = storageClient.upload_file(file.getBytes(), ext, pairs);

            String uploadFileUrl = fastDFSUtils.getUploadFileUrl(strings);
            return uploadFileUrl;

        }

        return "images/error.png";
    }
}
