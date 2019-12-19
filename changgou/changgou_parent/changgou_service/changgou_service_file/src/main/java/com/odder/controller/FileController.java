package com.odder.controller;

import com.odder.file.FastDFSFile;
import com.odder.util.FastDFSClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/19 21:05
 * @Version 1.0
 */
@RestController
@CrossOrigin
public class FileController {

    @RequestMapping("upload")
    public Result upload(MultipartFile file) throws IOException {
        FastDFSFile fastDFSFile = new FastDFSFile(file.getOriginalFilename(),
                file.getBytes(),
                StringUtils.getFilenameExtension(file.getOriginalFilename()));
        String[] upload = FastDFSClient.update(fastDFSFile);
        //返回上传结果
        return new Result(true, StatusCode.OK,
                FastDFSClient.getTrackerUrl() + upload[0] + "/" + upload[1]
        );
    }
}
