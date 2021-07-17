package com.assignment.googlebooksdemo.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.googlebooksdemo.R;
import com.assignment.googlebooksdemo.data.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksVH> {


    List<Item> itemBooks;
    Context mContext;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    public BooksAdapter(List<Item> booksInfos, Context mContext) {
        this.itemBooks = booksInfos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BooksVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_books_rv, parent, false);
        return new BooksVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksVH holder, int position) {
        holder.tvBookDescription.setText(itemBooks.get(position).getVolumeInfo().getDescription());
        holder.tvBookTitle.setText(itemBooks.get(position).getVolumeInfo().getTitle());

        if (itemBooks.get(position).getVolumeInfo().getImageLinks()!=null) {
            Picasso.with(mContext).load(itemBooks.get(position).getVolumeInfo().getImageLinks().getThumbnail()).into(holder.ivBookImage);
        }
            

    }

    @Override
    public int getItemCount() {
        return itemBooks.size();
    }

    class BooksVH extends RecyclerView.ViewHolder {


        private ImageView ivBookImage;
        private TextView tvBookTitle;
        private TextView tvBookDescription;

        public BooksVH(@NonNull View itemView) {
            super(itemView);

            ivBookImage = itemView.findViewById(R.id.ivBookImage);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookDescription = itemView.findViewById(R.id.tvBookDescriptions);
        }
    }
}


