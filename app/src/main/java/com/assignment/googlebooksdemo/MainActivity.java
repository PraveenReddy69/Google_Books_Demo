package com.assignment.googlebooksdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.assignment.googlebooksdemo.data.utils.NetworkStateReceiver;


import java.util.ArrayList;

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
    NetworkStateReceiver connection;
    private Context mContext = this;

    boolean isNetworkAvailable = true;
    BooksAdapter booksAdapter;


    int mStartIndex = 1, mMaxLimit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rc_books = findViewById(R.id.rc_books);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);


        NetworkStateReceiver broadcastReceiver = new NetworkStateReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle b = intent.getExtras();

                isNetworkAvailable = b.getBoolean("network_connected");
                getAllBooks("flowers", mStartIndex, mMaxLimit,isNetworkAvailable);

            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter("broadCastName"));

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {

                    mStartIndex = mStartIndex + mMaxLimit;
                    Log.d("STARTINDEX", "onScrollChange: " + mStartIndex);
                    loadingPB.setVisibility(View.VISIBLE);
                    getAllBooks("flowers", mStartIndex, mMaxLimit,isNetworkAvailable);
                }
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        rc_books.setLayoutManager(linearLayoutManager);

    }


    private List<Item> fetchResults(Response<BooksInfo> response) {
        BooksInfo topRatedMovies = response.body();
        return topRatedMovies.getItems();
    }


    public void getAllBooks(String type, int start_index, int max_result,boolean isNetworkAvailable) {


        if (isNetworkAvailable){
            BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);
            Call<BooksInfo> books_list = bookService.getbooks(type, start_index, max_result);

            books_list.enqueue(new Callback<BooksInfo>() {
                @Override
                public void onResponse(Call<BooksInfo> call, Response<BooksInfo> response) {
                    if (response.isSuccessful() && response.body() != null) {


                        List<Item> results = fetchResults(response);
                        loadingPB.setVisibility(View.GONE);

                        booksAdapter = new BooksAdapter(list_books, mContext);
                        booksAdapter.addAll(results);


                        rc_books.setAdapter(booksAdapter);
                        loadingPB.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<BooksInfo> call, Throwable t) {
                    Log.d("TAG", t.getMessage());
                }
            });
        }else {
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        connection = new NetworkStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connection, filter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        connection = new NetworkStateReceiver();
        unregisterReceiver(connection);
    }
}