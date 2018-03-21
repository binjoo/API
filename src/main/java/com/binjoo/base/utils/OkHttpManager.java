package com.binjoo.base.utils;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpManager {
    private OkHttpClient okHttpClient;
    private Request.Builder builder;

    public OkHttpManager() {
        okHttpClient = getDefaultOkHttpClient();
        okHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
        okHttpClient.newBuilder().readTimeout(10, TimeUnit.SECONDS);
        okHttpClient.newBuilder().writeTimeout(10, TimeUnit.SECONDS);
    }

    private static OkHttpClient getDefaultOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        //
        final X509TrustManager trustManager = new X509TrustManagerImpl();
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[] { trustManager }, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.getMessage();
        }
        return builder.sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).build();
    }

    public void get(String url) throws Exception {
        this.builder = new Request.Builder();
        this.builder.url(url);
    }

    public void addHeader(String name, String value) throws Exception {
        this.builder.addHeader(name, value);
    }

    public Request build() throws Exception {
        return builder.build();
    }

    public Response execute() throws Exception {
        return okHttpClient.newCall(this.build()).execute();
    }
}
