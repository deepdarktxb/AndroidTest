package com.example.httpdemo.datademo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.httpdemo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FileWriteActivity extends AppCompatActivity {

    private EditText editText_testdata;
    private Button button_filewrite;
    private Button button_imgwrite;
    private Button button_imgread;
    private ImageView imageView_imgread;
    private String path;
    private  SharedPreferences preference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_write);
        initView();
        initEvent();
        reload();
    }

    private void reload() {
        String data = preference.getString("data",null);
        if (data != null){
            editText_testdata.setText(data);
        }
    }

    private void initView() {
        editText_testdata = findViewById(R.id.testdata);
        button_filewrite = findViewById(R.id.id_btn_filewrite);
        button_imgwrite = findViewById(R.id.id_btn_imgwrite);
        button_imgread = findViewById(R.id.id_btn_imgread);
        imageView_imgread = findViewById(R.id.id_iv_imgread);

    }
    private void saveImage(String path, Bitmap bitmap) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fos != null){
                fos.close();
            }
        }

    }

    private void initEvent() {
        preference = getSharedPreferences("config", Context.MODE_PRIVATE);
        String filName = "a.jpeg";
        path = Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).toString() + File.separatorChar+ filName;
        button_filewrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = editText_testdata.getText().toString();
                
                SharedPreferences.Editor editor = preference.edit();
                editor.putString("data",data);
                editor.apply();
            }
        });

        button_imgwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String filName = System.currentTimeMillis() + ".jpeg";
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.a);
                try {
                    saveImage(path,bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


        });

        button_imgread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                    Bitmap bitmap = openImage(path);
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                imageView_imgread.setImageBitmap(bitmap);
                imageView_imgread.setImageURI(Uri.parse(path));


            }
        });
    }

    private Bitmap openImage(String path) throws IOException {
        Bitmap bitmap = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
            bitmap =  BitmapFactory.decodeStream(fileInputStream);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (fileInputStream != null){
                fileInputStream.close();
            }
        }
        return bitmap;
    }
}