package com.edgar.doujinreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<CoverItem> coverItems;
    private Context context;
    private ItemClickListener mListener;

    public MyAdapter(Context context, ArrayList<CoverItem> coverItems) {
        this.context = context;
        this.coverItems = coverItems;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_cover_item,
                viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int pos) {
        Bitmap bitmap = coverItems.get(pos).getCoverBitmap();
        String pageString = "Error: Cannot find this cover!";
        if (bitmap == null) {
            viewHolder.tvPage.setText(pageString);
        } else {
            viewHolder.tvPage.setText(coverItems.get(pos).getPageString());
            viewHolder.tvTitle.setText(coverItems.get(pos).getTitleString());
            viewHolder.ivCover.setImageBitmap(coverItems.get(pos).getCoverBitmap());
            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return coverItems.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;
        protected TextView tvPage;
        protected ImageView ivCover;
        protected FrameLayout rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvPage = (TextView)itemView.findViewById(R.id.tv_page);
            ivCover = (ImageView)itemView.findViewById(R.id.iv_cover);
            rootView = (FrameLayout)itemView.findViewById(R.id.item_root_view);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.mListener = listener;
    }

}
