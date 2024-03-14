package com.example.httpdemo;

import static android.util.Log.println;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Repeatable;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;

public class OkhttpUtils {

    private static OkhttpUtils instance = new OkhttpUtils();
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());
    public static  OkhttpUtils getInstance() {
        return instance;
    }

    private OkhttpUtils(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }
    public String doGet1(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        Call call = okHttpClient.newCall(request);

        Response response = call.execute();
        
        return response.body().string();
    }
    public void doGet(String url,CallBack callBack)  {
        Request request = new Request.Builder().url(url).build();

        extracted(callBack, request);

    }

    private void extracted(CallBack callBack, Request request) {
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                String string = null;
                try {
                    string = response.body().string();
                }
                catch (IOException e){
                    handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                        }
                    });
                }
                String finalString = string;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(finalString);
                    }
                });
            }
        });
    }

    public void doPost(String url, CallBack callBack, HashMap<String,String> hashMap){
        FormBody.Builder formBody = new FormBody.Builder();
        if (hashMap != null){
            for(String params:hashMap.keySet()){
                formBody.add(params, Objects.requireNonNull(hashMap.get(params)));
            }
        }
        Request request = new Request.Builder().post(formBody.build()).url(url).build();
        extracted(callBack, request);
    }

    public void getWithQueryParams(String url){
        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(url).newBuilder();
        queryUrlBuilder.addQueryParameter("username","txb");
        Request request = new Request.Builder().url(queryUrlBuilder.build()).build();
    }

    public void postWithBody(String url) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","txb");
        jsonObject.put("age","123");
        RequestBody requestBody = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json")
        );
        Request request = new Request.Builder().url(url).post(requestBody).build();
    }
    public void putWithFile(String url){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file","text:txt",
                        RequestBody.create(new File("src/main/resuorce/text.txt"),
                                MediaType.parse("application/octet-stream"))
                        )
                .build();
        Request request = new Request.Builder().url(url).put(requestBody).build();
    }
}
