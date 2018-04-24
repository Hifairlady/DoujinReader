package com.edgar.doujinreader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPagerAdapter extends PagerAdapter {

    private String[] filenames;
    private Context context;
    private ImagesGetter imagesGetter;
    private String folderPath;

    public ViewPagerAdapter(Context context, String[] filenames, ImagesGetter imagesGetter, String folderPath) {
        this.context = context;
        this.filenames = filenames;
        this.imagesGetter = imagesGetter;
        this.folderPath = folderPath;
    }


    @Override
    public int getCount() {
        return filenames.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_slide_show, container, false);
        TextView tvPager = (TextView)itemView.findViewById(R.id.slide_tv_page);
        ImageView ivDisplay = (ImageView)itemView.findViewById(R.id.slide_iv_display);
        String filename = folderPath + "/" + filenames[position];
        imagesGetter.startSetImageTask(ivDisplay, filename);
        tvPager.setText("Page " + (position + 1) + "/" + filenames.length + " ========== Filename: "
            + filenames[position]);
        ((ViewPager)container).addView(itemView, 0);
        return itemView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }
}
