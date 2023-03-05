package com.classification.gc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Command {
    /*命令列*/
    protected List<String> commands;
    /*命令结果*/
    protected String resultInfo;
    /*添加的命令数目*/
    protected Integer commandsNumber;

    public Command() {
        commands = new LinkedList<>();
        resultInfo = null;
        commandsNumber = 0;
    }

    public void windowsInit() {
        this.commands.add("cmd");
        this.commands.add("/c");
        //this.commands.add("start");
        //this.commands.add("/b");
    }

    public Command addCmd(List<String> commands) {
        if (this.commandsNumber != 0) this.commands.add("&");
        this.commands.addAll(commands);
        this.commandsNumber++;
        return this;
    }

    /**
     * 执行命令列
     *
     * @param workDir 命令工作目录
     * @return 命令执行的输出结果
     * @throws IOException 读取执行输出结果时的IO错误
     */
    public String processCmd(File workDir) throws IOException, InterruptedException {
//        System.out.println(this.commands);
//        System.out.println("Processing");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(this.commands);
        if (workDir != null) {
            processBuilder.directory(workDir);
        }
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        String line;
        List<String> stringList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((line = br.readLine()) != null) {
            stringList.add(line);
        }
        process.destroy();
        this.resultInfo = stringList.get(stringList.size()-2) + " " +stringList.get(stringList.size()-1);
        return this.resultInfo;
    }
}
