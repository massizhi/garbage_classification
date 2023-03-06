package com.classification.gc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class Classificate {
    @RequestMapping(value = "/up", method = RequestMethod.POST)
    public String up(@RequestParam("file") MultipartFile file) throws Exception {
        //获取上传的图片并保存
        //下方要写绝对路径，不然transferTo不能实现
        File result = new File("C:\\Users\\UserName\\Desktop\\github\\garbage_classification\\algorithm", "hello.jpg");
        if (result.exists()) {
            //System.out.println(result.getAbsolutePath() + " 文件已存在");
        }
        try {
            file.transferTo(result);
            //System.out.println(result.getAbsolutePath() + " 文件保存成功");
        } catch (IOException e) {
            //System.out.println(result.getAbsolutePath() + " 文件保存失败");
            e.printStackTrace();
        }

        //使用上传图片进行模型预测，根据代码输入找到结果。
        Command command = new Command();
        command.windowsInit();
        List<String> cmd1 = new ArrayList<>();
        cmd1.add("activate");//activate
        cmd1.add("tensorflow");//tensorflow
        command.addCmd(cmd1);
        List<String> cmd = new ArrayList<>();
        cmd.add("python");
        cmd.add("mobilenet\\predict.py");
        cmd.add("hello.jpg");
        command.addCmd(cmd);
        File workDir = new File("C:\\Users\\UserName\\Desktop\\github\\garbage_classification\\algorithm");
        String classification = command.processCmd(workDir);
        return classification;
    }
}
