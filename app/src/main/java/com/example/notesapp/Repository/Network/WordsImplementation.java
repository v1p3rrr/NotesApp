package com.example.notesapp.Repository.Network;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordsImplementation {
    private static WordsImplementation instance = null;
    public static String API_KEY = "89b7626184msh22a3eaaa5d02d02p15110bjsn1e901599cb41";

    public WordsAPI api;
    public WordsImplementation() {
        //Retrofit http rest builder with api retrofit interface usage
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://wordsapiv1.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(WordsAPI.class);
    }

    //Singleton
    public static WordsImplementation getInstance() {
        if (instance == null) {
            instance = new WordsImplementation();
        }
        return instance;
    }

//    private OkHttpClient createOkHttpClient(){
//        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(new Interceptor() {
//            @NonNull
//            @Override
//            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
//                final Request original = chain.request();
//                final HttpUrl originalHttpUrl = original.url();
//                final HttpUrl url = originalHttpUrl.newBuilder()
//                        .addQueryParameter("api_key", API_KEY)
//                        .build();
//                final Request.Builder requestBuilder = original.newBuilder()
//                        .url(url);
//                final Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        });
//
//    }

    //Request handling
    public LiveData<List<String>> getSynonyms(String word) {

        MutableLiveData<List<String>> synonyms = new MutableLiveData<>();

        api.getSynonyms(word).enqueue(new Callback<SynonymsResponse>() {
            @Override
            public void onResponse(Call<SynonymsResponse> call, Response<SynonymsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    synonyms.setValue(response.body().synonyms);
                    System.out.println("жжжж " + response.body());
                    System.out.println("loooooool: " + Objects.requireNonNull(synonyms.getValue()).get(0));
                }
            }

            @Override
            public void onFailure(Call<SynonymsResponse> call, Throwable t) {

            }
        });

        return synonyms;
    }

    //Json object representation
    static class SynonymsResponse{

        String word;

        List<String> synonyms;

    }

}
