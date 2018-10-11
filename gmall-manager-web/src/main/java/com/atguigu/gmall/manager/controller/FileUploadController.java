package com.atguigu.gmall.manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/file")
@Controller
public class FileUploadController {

    /**
     * 文件上传成功返回 文件的实际访问地址
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file){

        if (!file.isEmpty()){
            log.info("文件:{}，大小：{}；上传完成",file.getOriginalFilename(),file.getSize());
        }
        return "https://m.360buyimg.com/babel/jfs/t5137/20/1794970752/352145/d56e4e94/591417dcN4fe5ef33.jpg";
    }
}
