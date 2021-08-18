package com.example.rain.net.retrofit

import android.annotation.SuppressLint
import com.example.rain.BuildConfig
import com.example.rain.net.call.BaseRequest
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.Buffer
import retrofit2.Retrofit
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.logging.Logger
import javax.net.ssl.*

open class BaseRetrofit {

    val baseUrl = "https://os.zhijierongxing.com"
    var retrofit: Retrofit? = null

    // 获取具体的call
    open fun <T : BaseRequest?> getCall(tClass: Class<T>?): T {
        return retrofit!!.create(tClass) as T
    }

    protected open fun initClient(): OkHttpClient? {
        val builder = OkHttpClient.Builder()
            .sslSocketFactory(createSSLSocketFactory())
            .hostnameVerifier(TrustAllHostnameVerifier()) //trust all?信任所有host
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(LoggingInterceptor())
        }
        return builder.build()
    }

    open fun createSSLSocketFactory(): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory? = null
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(TrustAllCerts()), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (e: Exception) {
        }
        return ssfFactory
    }

    private class TrustAllCerts : X509TrustManager {
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }

    private class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true //trust All的写法就是在这里写的，直接无视hostName，直接返回true，表示信任所有主机
        }
    }

    // okhttp logger 拦截器
    private class LoggingInterceptor : Interceptor {
        @SuppressLint("DefaultLocale")
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val logger = Logger.getLogger("http-info")
            val t1 = System.nanoTime()
            logger.info(
                String.format(
                    "Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()
                )
            )
            val requestBody = request.body()
            val buffer = Buffer()
            requestBody!!.writeTo(buffer)
            var charset = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            // 打印 body 数据
            if (isPlaintext(buffer)) {
                logger.info(buffer.readString(charset))
                logger.info("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)")
            } else {
                logger.info("--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)")
            }
            val response = chain.proceed(request)
            val t2 = System.nanoTime()
            logger.info(
                String.format(
                    "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6, response.headers()
                )
            )
            return response
        }

        companion object {
            private val UTF8 = Charset.forName("UTF-8")
        }

        open fun isPlaintext(buffer: Buffer): Boolean {
            return try {
                val prefix = Buffer()
                val byteCount = Math.min(buffer.size(), 64L)
                buffer.copyTo(prefix, 0L, byteCount)
                var i = 0
                while (i < 16 && !prefix.exhausted()) {
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                    ++i
                }
                true
            } catch (var6: EOFException) {
                false
            }
        }
    }

}