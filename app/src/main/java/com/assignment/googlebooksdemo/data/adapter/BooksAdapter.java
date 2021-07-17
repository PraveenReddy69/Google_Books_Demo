package com.assignment.googlebooksdemo.data.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.googlebooksdemo.R;
import com.assignment.googlebooksdemo.data.model.BooksInfo;
import com.assignment.googlebooksdemo.data.model.Item;
import com.assignment.googlebooksdemo.data.utils.GlideApp;
import com.assignment.googlebooksdemo.data.utils.GlideRequest;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    List<Item> itemBooks;
    Context mContext;
    private boolean retryPageLoad = false;

    public BooksAdapter(List<Item> booksInfos, Context mContext) {
        this.itemBooks = booksInfos;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.custom_books_rv, parent, false);
                viewHolder = new BooksVH(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item result = itemBooks.get(position); // Movie

        switch (getItemViewType(position)) {


            case ITEM:
                final BooksVH booksVH = (BooksVH) holder;

                booksVH.tvBookDescription.setText(itemBooks.get(position).getVolumeInfo().getDescription());
                booksVH.tvBookTitle.setText(itemBooks.get(position).getVolumeInfo().getTitle());

                if (itemBooks.get(position).getVolumeInfo().getImageLinks() != null) {
                    loadImage(itemBooks.get(position).getVolumeInfo().getImageLinks().getThumbnail())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    // TODO: 2/16/19 Handle failure
                                    booksVH.mProgress.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // image ready, hide progress now
                                    booksVH.mProgress.setVisibility(View.GONE);
                                    return false;   // return false if you want Glide to handle everything else.
                                }
                            })
                            .into(booksVH.ivBookImage);
                }


                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {

                    loadingVH.mProgressBar.setVisibility(View.GONE);


                } else {

                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void add(Item r) {
        itemBooks.add(r);
        notifyItemInserted(itemBooks.size() - 1);
    }

    public void addAll(List<Item> moveResults) {
        for (Item result : moveResults) {
            add(result);
        }
    }

    public void remove(Item r) {
        int position = itemBooks.indexOf(r);
        if (position > -1) {
            itemBooks.remove(position);
            notifyItemRemoved(position);
        }
    }


    private GlideRequest<Drawable> loadImage(@NonNull String posterPath) {
        return GlideApp
                .with(mContext)
                .load(posterPath)
                .centerCrop();
    }


    @Override
    public int getItemCount() {
        return itemBooks.size();
    }

    class BooksVH extends RecyclerView.ViewHolder {

        private ProgressBar mProgress;
        private ImageView ivBookImage;
        private TextView tvBookTitle;
        private TextView tvBookDescription;

        public BooksVH(@NonNull View itemView) {
            super(itemView);
            mProgress = itemView.findViewById(R.id.books_progress);
            ivBookImage = itemView.findViewById(R.id.ivBookImage);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookDescription = itemView.findViewById(R.id.tvBookDescriptions);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);

        }


    }
}


