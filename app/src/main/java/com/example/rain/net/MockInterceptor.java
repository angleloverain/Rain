package com.example.rain.net;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String responseString = createResponseBody(chain);
        if (TextUtils.isEmpty(responseString)) {
            return chain.proceed(chain.request());
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }
        //Response 配置
        Response response = new Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();
        return response;
    }

    private String createResponseBody(Chain chain) {
        HttpUrl httpUrl = chain.request().url();
        String urlPath = httpUrl.url().toString();
        return onCreateResponseBody(urlPath);
    }

    protected String onCreateResponseBody(String url) {
        //TODO mock data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code","10000");
            jsonObject.put("msg","是的，好好");
            JSONObject userBean = new JSONObject();
            userBean.put("age",10);
            userBean.put("name","张三");
            jsonObject.put("data",userBean.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("dddd","mock : " + jsonObject.toString());
        return jsonObject.toString();
    }

}