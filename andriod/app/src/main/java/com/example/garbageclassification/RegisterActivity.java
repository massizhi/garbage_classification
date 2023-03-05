package com.example.garbageclassification;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {
    private Button buttonRegister;
    private Button buttonValid;
    private MyDatabaseHelper myDatabaseHelper;
    private EditText email;
    private EditText code;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.register_email);
        code = (EditText) findViewById(R.id.register_code);
        password = (EditText) findViewById(R.id.register_password);
        myDatabaseHelper = new MyDatabaseHelper(this, "Classification.db", null, 1);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
        //注册（判断+存储到数据库）
        buttonRegister = (Button) findViewById(R.id.register_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "注册成功，自动跳转至登录界面", Toast.LENGTH_SHORT).show();
                //跳转至登录界面
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //发送验证码事件
        buttonValid = (Button) findViewById(R.id.register_valid);
        buttonValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //发送验证码
//                // 1.创建连接对象，链接到邮箱服务器
//                Properties props = new Properties(); // 参数配置
//                props.setProperty("mail.transport.protocol", "smtp");// 使用的协议（JavaMail规范要求）
//                props.setProperty("mail.smtp.host", myEmailSMTPHost);// 发件人的邮箱的 SMTP 服务器地址
//                props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
//                // 2.根据配置创建会话对象, 用于和邮件服务器交互
//                Session session = Session.getInstance(props, new Authenticator() {
//                    @Override
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(sendEmail, sendPassword);
//                    }
//                });
//                try {
//                    // 3.创建邮件对象
//                    Message message = new MimeMessage(session);
//                    // 3.1设置发件人
//                    message.setFrom(new InternetAddress(sendEmail));
//                    // 3.2设置收件人
//                    message.setRecipient(Message.RecipientType.TO, new InternetAddress("mkxfzu@163.com"));
//                    // 3.3设置邮件的主题
//                    message.setSubject("");
//                    // 3.4设置邮件的正文
//                    message.setContent("尊敬的用户您好，您的验证码是：test。", "text/html;charset=UTF-8");
//                    // 4.发送邮件
//                    Transport.send(message);
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }
                //按钮变化
                buttonValid.setText("已发送");
                buttonValid.setBackgroundColor(Color.parseColor("#11D183"));
                buttonValid.setActivated(false);
            }
        });
    }
}