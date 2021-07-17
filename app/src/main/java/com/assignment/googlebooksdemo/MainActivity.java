package com.assignment.googlebooksdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.assignment.googlebooksdemo.data.adapter.BooksAdapter;
import com.assignment.googlebooksdemo.data.api.BookService;
import com.assignment.googlebooksdemo.data.api.RetrofitInstance;
import com.assignment.googlebooksdemo.data.model.BooksInfo;
import com.assignment.googlebooksdemo.data.model.Item;
 

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    List<Item> list_books = new ArrayList<>();
    RecyclerView rc_books;
    private NestedScrollView nestedSV;
    private ProgressBar loadingPB;
    LinearLayoutManager linearLayoutManager;
    Context mContext = this;
    BooksAdapter booksAdapter;


    int mStartIndex = 1, mMaxLimit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rc_books = findViewById(R.id.rc_books);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);
        getAllBooks("flowers", mStartIndex, mMaxLimit);
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {

                    mStartIndex = mStartIndex + mMaxLimit;
                    Log.d("STARTINDEX", "onScrollChange: " + mStartIndex);
                    loadingPB.setVisibility(View.VISIBLE);
                    getAllBooks("flowers", mStartIndex, mMaxLimit);
                }
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        rc_books.setLayoutManager(linearLayoutManager);

    }


    public void getAllBooks(String type, int start_index, int max_result) {

        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);
        Call<BooksInfo> books_list = bookService.getbooks(type, start_index, max_result);

        books_list.enqueue(new Callback<BooksInfo>() {
            @Override
            public void onResponse(Call<BooksInfo> call, Response<BooksInfo> response) {
                if (response.isSuccessful() && response.body() != null) {


                    list_books.addAll(response.body().getItems());
                    booksAdapter = new BooksAdapter(list_books, mContext);
                    rc_books.setAdapter(booksAdapter);
                    loadingPB.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BooksInfo> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });
    }
}