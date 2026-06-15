package com.itheima.controller;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
    //上传文件

    private static final String UPLOAD_DIR = "D:/images/";
    @PostMapping("/upload")
    public Result upload(String username, Integer age, MultipartFile file) throws IOException {
        log.info("上传文件：{},{},{}",username,age,file);
        if(!file.isEmpty()){

            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + extName;
            // 拼接完整的文件路径
            File targetFile = new File(UPLOAD_DIR + uniqueFileName);

            //如果目标目录不存在，则创建它
            if(!targetFile.getParentFile().exists()){
                targetFile.getParentFile().mkdirs();
            }
            //保存文件到targetFile这个目录下
            file.transferTo(targetFile);
        }
        return Result.success();
    }

}
