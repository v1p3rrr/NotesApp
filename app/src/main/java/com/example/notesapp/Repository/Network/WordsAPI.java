package com.example.notesapp.Repository.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface WordsAPI {
    @GET("/words/{word}/synonyms")
    @Headers({
            "Content-Type: application/json",
            "x-rapidapi-host: wordsapiv1.p.rapidapi.com",
            "x-rapidapi-key: 89b7626184msh22a3eaaa5d02d02p15110bjsn1e901599cb41"
    })
    Call<WordsImplementation.SynonymsResponse> getSynonyms(@Path("word") String word);


}
