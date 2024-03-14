package com.example.httpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button eBtnGet;
    private Button eBtnPost;
    private TextView eTvContent;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        eBtnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread(){
//                    @Override
//                    public void run() {
//                        try {
//                            String context = OkhttpUtils.getInstance().doGet("http://10.62.140.105:9000/testlogin?username=txb");
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    eTvContent.setText(context);
//                                }
//                            });
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }.start();
                OkhttpUtils.getInstance().doGet("http://10.62.140.105:9000/gettest?username=txb", new CallBack() {
                    @Override
                    public void onSuccess(String result) {
                        eTvContent.setText(result);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(MainActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("username","txb");
        eBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkhttpUtils.getInstance().doPost("http://10.62.140.105:9000/posttest", new CallBack() {
                    @Override
                    public void onSuccess(String result) {
                        eTvContent.setText(result);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(MainActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
                    }
                },hashMap);
            }
        });
    }

    private void initView() {
        eBtnGet = findViewById(R.id.id_btn_get);
        eBtnPost = findViewById(R.id.id_btn_post);
        eTvContent = findViewById(R.id.id_tv_content);
    }

}