package com.example.rain.net.retrofit;


import android.annotation.SuppressLint;

import com.example.rain.BuildConfig;
import com.example.rain.net.call.BaseRequest;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;

public class BaseRetrofit {

    protected static Retrofit retrofit;

    // 获取具体的call
    public static <T extends BaseRequest> T getCall(Class<T> tClass){
        return (T)retrofit.create(tClass);
    }

    protected static OkHttpClient initClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier(new TrustAllHostnameVerifier());//trust all?信任所有host
        if (BuildConfig.DEBUG){
            builder.addInterceptor(new LoggingInterceptor());
        }
        return builder.build();
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }
    private static class TrustAllCerts implements X509TrustManager {

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;//trust All的写法就是在这里写的，直接无视hostName，直接返回true，表示信任所有主机
        }

    }
    // okhttp logger 拦截器
    private static class LoggingInterceptor implements Interceptor {

        private static final Charset UTF8 = Charset.forName("UTF-8");

        @SuppressLint("DefaultLocale")
        @Override public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Logger logger = Logger.getLogger("http-info");
            long t1 = System.nanoTime();
            logger.info(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            RequestBody requestBody = request.body();

            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            // 打印 body 数据
            if (isPlaintext(buffer)) {
                logger.info(buffer.readString(charset));
                logger.info("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
            } else {
                logger.info("--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)");
            }

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            logger.info(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = Math.min(buffer.size(), 64L);
            buffer.copyTo(prefix, 0L, byteCount);
            for(int i = 0; i < 16 && !prefix.exhausted(); ++i) {
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }

            return true;
        } catch (EOFException var6) {
            return false;
        }
    }

}
