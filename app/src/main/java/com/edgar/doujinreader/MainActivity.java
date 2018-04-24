package com.edgar.doujinreader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_MSG = "================";

    private static final int GET_IMAGES_FINISHED = 101;
    private static final int GET_IMAGES_FAILED = 102;
    private static final String WORK_PATH = Environment.getExternalStorageDirectory() +
            "/EhViewer/download/";

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private ArrayList<CoverItem> coverItems = new ArrayList<>();

    private Handler getCoverHandler = null;

    private File rootDir = null;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading data ...");

        rootDir = new File(WORK_PATH);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            Toast.makeText(this, "Work Path does not exist!", Toast.LENGTH_SHORT).show();
            return;
        }

        mRecyclerView = (RecyclerView)findViewById(R.id.my_recyclerview);
        myAdapter = new MyAdapter(this, coverItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        getCoverHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case GET_IMAGES_FINISHED:
                        progressDialog.dismiss();
                        runAnimation(mRecyclerView);
                        break;

                    case GET_IMAGES_FAILED:
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Empty Data!", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
            }
        };
        progressDialog.show();
        new GetCoversThread().start();
    }

    private class GetCoversThread extends Thread {

        @Override
        public void run() {
            super.run();
            Message message = getCoverHandler.obtainMessage();

            File[] folders = rootDir.listFiles();
            if (folders == null || folders.length == 0) {
                message.what = GET_IMAGES_FAILED;
                getCoverHandler.sendMessage(message);
                return;
            }
            ImageDecoder.sortFilesByName(folders);

            for (int i = 0; i < folders.length; i++) {
                if (folders[i].isFile()) {
                    continue;
                }
                String[] imageFilesNames = folders[i].list();
                if (imageFilesNames == null || imageFilesNames.length == 0) {
                    continue;
                }
                ImageDecoder.sortStringsByName(imageFilesNames);
                int coverIndex = 0;
                while (!ImageDecoder.checkFomat(imageFilesNames[coverIndex]) && coverIndex < imageFilesNames.length) {
                    coverIndex++;
                }
                if (coverIndex == imageFilesNames.length) {
                    continue;
                }
                String coverName = imageFilesNames[coverIndex];
                String coverPath = folders[i].getPath() + "/" + coverName;
                Bitmap coverImage = ImageDecoder.decodeFromFile(MainActivity.this, new File(coverPath),
                        500, 500);
                int maxPage = imageFilesNames.length - coverIndex;
                String pageString = "Pages: " + String.valueOf(maxPage);
                String titleString = folders[i].getName();
                CoverItem coverItem = new CoverItem(titleString, pageString, folders[i].getPath(),
                        maxPage, coverImage);
                coverItems.add(coverItem);
            }

            message.what = GET_IMAGES_FINISHED;
            getCoverHandler.sendMessage(message);
        }
    }

    private void runAnimation(RecyclerView recyclerView) {
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this,
                R.anim.layout_from_bottom);
        myAdapter = new MyAdapter(this, coverItems);
        myAdapter.setItemClickListener(new MyAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Intent intent = new Intent(MainActivity.this, SlideActivity.class);
                intent.putExtra("folderPath", coverItems.get(position).getFolderPath());
                intent.putExtra("maxPage", coverItems.get(position).getMaxPage());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutAnimation(controller);
        myAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();


    }

}
