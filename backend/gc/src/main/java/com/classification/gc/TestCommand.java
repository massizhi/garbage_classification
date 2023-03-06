package com.classification.gc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestCommand {
    public static void main(String[] args) throws IOException, InterruptedException {
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
        System.out.println(classification);
    }
}
