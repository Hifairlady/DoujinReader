package com.edgar.doujinreader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;

public class SlideActivity extends AppCompatActivity {

    private static final String DEBUG_MSG = "================";
    private String folderPath;
    private int maxPage;

    private int height;

    private ImagesGetter imagesGetter;

    private String[] filenames = new String[0];
    private BottomSheetDialog bottomSheetDialog;
    private TextView tvCurPage;
    private TextView tvMaxPage;
    private SeekBar pagingSeekbar;
    private ViewPager viewPager;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        initData();
        initUI();

        viewPager = (ViewPager)findViewById(R.id.my_viewpager);
        mAdapter = new ViewPagerAdapter(SlideActivity.this, filenames,
                imagesGetter, folderPath);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnTouchListener(mOnTouchListener);
    }

    private void initUI() {

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitle("Details");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_sheet_view, null);
        bottomSheetDialog.setContentView(sheetView);

        tvCurPage = (TextView)sheetView.findViewById(R.id.cur_page_num);
        tvMaxPage = (TextView)sheetView.findViewById(R.id.max_page_num);
        pagingSeekbar = (SeekBar)sheetView.findViewById(R.id.page_seekbar);
        tvCurPage.setText(String.valueOf(1));
        tvMaxPage.setText(String.valueOf(maxPage));
        pagingSeekbar.setMax(maxPage);
        pagingSeekbar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
    }

    private void initData() {

        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();

        folderPath = getIntent().getStringExtra("folderPath");
        maxPage = getIntent().getIntExtra("maxPage", 1);
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            Toast.makeText(this, "Empty Folder!", Toast.LENGTH_SHORT).show();
            finish();
        }
        imagesGetter = new ImagesGetter(SlideActivity.this);

        String[] tmpFilenames = folder.list();
        int index = 0;
        for (int i = 0; i < tmpFilenames.length; i++) {
            if (ImageDecoder.checkFomat(tmpFilenames[i])) {
                filenames = Arrays.copyOf(filenames, filenames.length+1);
                filenames[index] = tmpFilenames[i];
                index++;
            }
        }
        ImageDecoder.sortStringsByName(filenames);
    }

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (progress == 0) {
                progress = 1;
                seekBar.setProgress(progress);
            }
            tvCurPage.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (seekBar.getProgress() == 0) {
                seekBar.setProgress(1);
            }
            tvCurPage.setText(String.valueOf(seekBar.getProgress()));
            viewPager.setCurrentItem(seekBar.getProgress()-1);
        }
    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (v.getId()) {

                case R.id.my_viewpager:

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (event.getY() >= height*8/10 && !bottomSheetDialog.isShowing()) {
                            int pos = viewPager.getCurrentItem() + 1;
                            pagingSeekbar.setProgress(pos);
                            tvCurPage.setText(String.valueOf(pos));
                            bottomSheetDialog.show();
                            break;
                        }
                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    };
}
