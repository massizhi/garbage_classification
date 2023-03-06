# garbage_classification 垃圾分类应用
including andriod application, springboot backend, and convnet classification
---

## 1. 功能

 - 基于卷积神经网络的垃圾分类，并将深度学习模型整合到安卓应用，实现了生活垃圾的图像识别与分类。
 - 安卓应用主要包含垃圾搜索、图像识别、题目测试和知识科普等功能模块。垃圾搜索中包含 3986 种物品，图像识别包含 143 种物品，覆盖了居民常见生活垃圾，可以有效辅助人们进行垃圾分类。
 - 算法采用基于MobileNetV2的深度学习分类模型进行迁移学习。MobileNetV2模型于2018年被谷歌发布，是MobileNet的改进版，引入了反向残差结构和线性瓶颈结构提高模型性能。模型优化包括 dropout 正则化、Adam优化器等。图像主要筛选整理于华为垃圾分类挑战赛数据集，并且进行了随机水平翻转、图像旋转等数据增强操作，其中 90%用于训练集，10%用于验证集。
 - 图像识别运行流程为安卓客户端调用图库或图像头权限->选择图片->图片上传到后端->后端使用命令行操作调用算法predict->后端得到预测结果->返回至前端（后续可使用TensorFlow Mobile 把深度学习模型转化为pb格式文件并部署到安卓客户端）

## 2. 环境配置

 - TensorFlow2.0
 - Android studio（安卓开发IDE、Java）
 - springboot框架（maven管理，注意导入的项目文件夹是否正确）

## 3. 常见问题
### 3.1 predict.py
TensorFlow对图片预测的代码主要在这里面，注意确认一些路径是否准确（如需要预测的图片路径是否与保存的图片路径一致）。
### 3.2 APP运行拍照就闪退
代码没有问题的前提下，可能是手机系统没有分配访问相机相册权限给这个APP。
### 3.3 照片无法上传
拍照过后发现照片没有上传到相应文件夹，且会报“”，可能是IP地址没有修改。在CMD中使用ipconfig指令查看ip地址，并修改前端连接服务器的相应代码的ip地址（此处有注释）。
### 3.4 输出乱码

 - 照片上传过后输出乱码，应该是图片上传失败，排查一下图片的路径。
 - 此代码是用cmd命令行参数运行的。如果直接用anaconda黑窗口是可以直接运行的，但要在cmd运行anaconda，需要配置系统环境变量。如果没有配，在cmd没有办法运行conda等命令，配好系统环境变量就没问题。

### 3.5 cmd运行问题
由于windows系统进入某盘的命令如：D:这种形式。而cmd默认是在C盘目录下。这个项目写的直接在C盘运行，如果将这个项目存在其他盘，则还需要修改cmd的命令。
