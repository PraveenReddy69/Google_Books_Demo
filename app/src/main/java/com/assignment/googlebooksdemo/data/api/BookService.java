package com.assignment.googlebooksdemo.data.api;


import com.assignment.googlebooksdemo.data.model.BooksInfo;
import com.assignment.googlebooksdemo.data.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookService {


    @GET("volumes")
    Call<BooksInfo> getbooks(@Query("q")String type, @Query("startIndex")int start_index, @Query("maxResults")int max_results);
}
