package com.tianshaokai.video;

import android.content.Context;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * httpClient基类
 */
public class AFHttpClient {
    private static OkHttpClient httpClient = null;

    public static OkHttpClient getHttpClient() {
        return getClient();
    }

    private static OkHttpClient getClient() {
        if (httpClient == null) {
            final OkHttpClient.Builder builder = new OkHttpClient.Builder();

            List<Interceptor> interceptors = builder.interceptors();
//            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            interceptors.add(httpLoggingInterceptor);

            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);

            //ssl
            X509TrustManager trustAllCerts = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }


                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }


                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            try {
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, null, new SecureRandom());
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }

            httpClient = builder.build();
        }

        return httpClient;
    }
}
