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
public class FileController {

    //上传成功返回文件的url访问地址

    /**
     *  MultipartFile file
     *
     * <input type="file" name="file"  multiple/>
     * @param file  接受一个上传来的文件
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload")
    public String fileUpload(@RequestParam("file") MultipartFile file){
        if(!file.isEmpty()){
            String filename = file.getOriginalFilename();
            String name = file.getName();
            long size = file.getSize();
            log.info("文件项：{}；名字：{}；大小：{}；上传成功",name,filename,size);
        }
        //把地址返回别人就能看了
        return "https://m.360buyimg.com/babel/jfs/t5137/20/1794970752/352145/d56e4e94/591417dcN4fe5ef33.jpg";
    }
}
