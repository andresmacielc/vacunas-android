package com.vacunas.rest;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vacunas.Configuraciones.PASSWORD_SERVICE;
import static com.vacunas.Configuraciones.URL_BASE_SERVICE;
import static com.vacunas.Configuraciones.USER_NAME_SERVICE;




public class ApiBuilder {

    public static Api build() {
         OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE_SERVICE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }

}
