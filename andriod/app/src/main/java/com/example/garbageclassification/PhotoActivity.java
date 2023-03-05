package com.example.garbageclassification;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView picture;
    private Uri imageUri;
    private Button takePhoto;
    private Button choosePhoto;
    private TextView result;
    private String TakePhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        picture = (ImageView) findViewById(R.id.photo_view);
        result = (TextView) findViewById(R.id.photo_result);
        takePhoto = (Button) findViewById(R.id.photo_take);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                //保存路径，在使用后端接口时使用，用来得到图片文件。
                TakePhotoPath = outputImage.getAbsolutePath();
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(PhotoActivity.this,
                            "com.example.cameraalbumtest.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                //显示图片和获取获取模型训练结果在onActivityResult里进行。
            }
        });
        choosePhoto = (Button) findViewById(R.id.photo_choose);
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //动态申请对SD卡读写的权限
                if (ContextCompat.checkSelfPermission(PhotoActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PhotoActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                //显示图片和获取获取模型训练结果在onActivityResult里进行。
            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);  //打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "您拒绝了该权限申请！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    //最终路径：com.example.cameraalbumtest.fileprovider/my_images/Android/data/com.example.garbageclassification/cache/output_image.jpg
    //拍照或者选图最后完成后进行，主要是显示图片的工作。
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                        //在这里进行连接，不可在主线程中进行。
                        result.setText("图像解析，模型预测中。。。");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.obj = link(TakePhotoPath);
                                handler.sendMessage(message);
                                //result.setText(link(TakePhotoPath));
                            }
                        }).start();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //显示图片和得到模型训练结果
                    handleImageOnKitKat(data);
                }
            default:
                break;
        }
    }

    private void handleImageOnKitKat(Intent data) {   //处理图片
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];   //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {  //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {   //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);

        //在这里进行连接，不可在主线程中进行。
        result.setText("图像解析，模型预测中。。。");
        String finalImagePath = imagePath;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.obj = link(finalImagePath);
                handler.sendMessage(message); 
                //result.setText(link(finalImagePath));
            }
        }).start();
    }

    private String getImagePath(Uri uri, String selection) {   //通过Uri和selection来获取真实的图片路径
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {   //显示图片
        if (imagePath != null) {
            //Log.v("imagePath",imagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取照片失败!", Toast.LENGTH_SHORT).show();
        }
    }

    //上传文件到服务器并从服务器得到模型预测结果
    public String link(String filePath) {
        //下面的地址为电脑本机ip地址，在控制台输入ipconfig后可查看。但似乎需要安卓端和电脑连接同一无线网才可以连接，未进行更深的测试。
        String uploadUrl = "http://192.168.146.93:8080/up";
        String result = null;
        try {
            //上传文件到服务器
            HttpClient hc = new DefaultHttpClient();
            hc.getParams().setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost hp = new HttpPost(uploadUrl);
            File file = new File(filePath);
            final MultipartEntity entity = new MultipartEntity();
            ContentBody contentBody = new FileBody(file);
            entity.addPart("file", contentBody);
            hp.setEntity(entity);
            HttpResponse hr = hc.execute(hp);
            HttpEntity he = hr.getEntity();

            //int statusCode = hr.getStatusLine().getStatusCode();
            //System.out.println(statusCode);

            //得到服务器方面的返回结果
            result = EntityUtils.toString(he, HTTP.UTF_8);
            String index = result.split(" ")[0];
            String prob = result.split(" ")[1];
            List<String> list = new ArrayList<>();
            list.add("其他垃圾_一次性杯子");
            list.add("其他垃圾_一次性棉签");
            list.add("其他垃圾_便利贴");
            list.add("其他垃圾_厨房手套");
            list.add("其他垃圾_口罩");
            list.add("其他垃圾_塑料袋");
            list.add("其他垃圾_干燥剂");
            list.add("其他垃圾_打火机");
            list.add("其他垃圾_搓澡巾");
            list.add("其他垃圾_毛巾");
            list.add("其他垃圾_涂改带");
            list.add("其他垃圾_湿纸巾");
            list.add("其他垃圾_烟蒂");
            list.add("其他垃圾_牙刷");
            list.add("其他垃圾_牙签");
            list.add("其他垃圾_百洁布");
            list.add("其他垃圾_眼镜");
            list.add("其他垃圾_破碎花盆及碟碗");
            list.add("其他垃圾_竹筷");
            list.add("其他垃圾_笔");
            list.add("其他垃圾_胶带");
            list.add("其他垃圾_苍蝇拍");
            list.add("其他垃圾_草帽");
            list.add("其他垃圾_鸡毛掸");
            list.add("厨余垃圾_八宝粥");
            list.add("厨余垃圾_冰激凌");
            list.add("厨余垃圾_冰糖葫芦");
            list.add("厨余垃圾_咖啡");
            list.add("厨余垃圾_哈密瓜");
            list.add("厨余垃圾_大骨头");
            list.add("厨余垃圾_巴旦木");
            list.add("厨余垃圾_橙子");
            list.add("厨余垃圾_残渣剩饭");
            list.add("厨余垃圾_汉堡");
            list.add("厨余垃圾_火龙果");
            list.add("厨余垃圾_烤鸡");
            list.add("厨余垃圾_瓜子");
            list.add("厨余垃圾_甘蔗");
            list.add("厨余垃圾_番茄");
            list.add("厨余垃圾_粉条");
            list.add("厨余垃圾_肉类");
            list.add("厨余垃圾_胡萝卜");
            list.add("厨余垃圾_茶叶");
            list.add("厨余垃圾_草莓");
            list.add("厨余垃圾_菜根菜叶");
            list.add("厨余垃圾_菠萝");
            list.add("厨余垃圾_薯条");
            list.add("厨余垃圾_薯片");
            list.add("厨余垃圾_蚕豆");
            list.add("厨余垃圾_蛋");
            list.add("厨余垃圾_蛋挞");
            list.add("厨余垃圾_蛋糕");
            list.add("厨余垃圾_豆腐");
            list.add("厨余垃圾_面包");
            list.add("厨余垃圾_饼干");
            list.add("厨余垃圾_香肠");
            list.add("厨余垃圾_鱼骨");
            list.add("厨余垃圾_鸡翅");
            list.add("可回收物_乒乓球拍");
            list.add("可回收物_体重秤");
            list.add("可回收物_保温杯");
            list.add("可回收物_充电宝");
            list.add("可回收物_剪刀");
            list.add("可回收物_勺子");
            list.add("可回收物_单肩包");
            list.add("可回收物_卡牌");
            list.add("可回收物_双肩包");
            list.add("可回收物_台灯");
            list.add("可回收物_吹风机");
            list.add("可回收物_塑料玩具");
            list.add("可回收物_塑料盒");
            list.add("可回收物_塑料碗盆");
            list.add("可回收物_塑料衣架");
            list.add("可回收物_奶盒");
            list.add("可回收物_尺子");
            list.add("可回收物_手机");
            list.add("可回收物_手链");
            list.add("可回收物_打气筒");
            list.add("可回收物_拉杆箱");
            list.add("可回收物_插头电线");
            list.add("可回收物_插线板");
            list.add("可回收物_旧衣服");
            list.add("可回收物_易拉罐");
            list.add("可回收物_木桶");
            list.add("可回收物_木质梳子");
            list.add("可回收物_木质锅铲");
            list.add("可回收物_枕头");
            list.add("可回收物_桌子");
            list.add("可回收物_档案袋");
            list.add("可回收物_椅子");
            list.add("可回收物_毛绒玩具");
            list.add("可回收物_水壶");
            list.add("可回收物_泡沫盒子");
            list.add("可回收物_洗发水瓶");
            list.add("可回收物_灭火器");
            list.add("可回收物_烟灰缸");
            list.add("可回收物_热水瓶");
            list.add("可回收物_燃气灶");
            list.add("可回收物_燃气瓶");
            list.add("可回收物_玻璃壶");
            list.add("可回收物_玻璃杯");
            list.add("可回收物_电动剃须刀");
            list.add("可回收物_电熨斗");
            list.add("可回收物_电磁炉");
            list.add("可回收物_电路板");
            list.add("可回收物_电风扇");
            list.add("可回收物_砧板");
            list.add("可回收物_笼子");
            list.add("可回收物_纸张");
            list.add("可回收物_纸箱");
            list.add("可回收物_纸袋");
            list.add("可回收物_耳机");
            list.add("可回收物_蛋糕盒");
            list.add("可回收物_袜子");
            list.add("可回收物_被子");
            list.add("可回收物_计算器");
            list.add("可回收物_订书机");
            list.add("可回收物_调料瓶");
            list.add("可回收物_路由器");
            list.add("可回收物_轮胎");
            list.add("可回收物_遥控器");
            list.add("可回收物_酒瓶");
            list.add("可回收物_金属食品罐");
            list.add("可回收物_钉子");
            list.add("可回收物_钟表");
            list.add("可回收物_铁丝球");
            list.add("可回收物_锅煲");
            list.add("可回收物_键盘");
            list.add("可回收物_镊子");
            list.add("可回收物_雨伞");
            list.add("可回收物_鞋子");
            list.add("可回收物_食用油桶");
            list.add("可回收物_饮料瓶");
            list.add("可回收物_鼠标");
            list.add("有害垃圾_干电池");
            list.add("有害垃圾_杀虫剂");
            list.add("有害垃圾_灯泡");
            list.add("有害垃圾_玻璃灯管");
            list.add("有害垃圾_纽扣电池");
            list.add("有害垃圾_胶水");
            list.add("有害垃圾_药瓶");
            list.add("有害垃圾_蓄电池");
            list.add("有害垃圾_过期药物");
            System.out.println("index"+index);
            System.out.println("prob"+prob);
            if (Integer.parseInt(index) > 142 || Integer.parseInt(index) < 0)
                result = "识别失败";
            else
                result = "名称：" + list.get(Integer.parseInt(index)) + "    概率：" + prob;
            System.out.println("result"+result);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "文件上传失败！上传文件为：" + filePath);
            Log.e("TAG", "报错信息toString：" + e.toString());
        }
        return result;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            result.setText(msg.obj.toString());
        }
    };
}