package com.edgar.doujinreader;

import android.graphics.Bitmap;

public class CoverItem {

    private String titleString;
    private String pageString;
    private Bitmap coverBitmap;
    private String folderPath;
    private int maxPage;
//    private String coverUrl;

    public CoverItem(String titleString, String pageString, String folderPath, int maxPage,
                     Bitmap coverBitmap) {
        this.titleString = titleString;
        this.pageString = pageString;
        this.folderPath = folderPath;
        this.maxPage = maxPage;
        this.coverBitmap = coverBitmap;
    }

    public Bitmap getCoverBitmap() {
        return coverBitmap;
    }

    public String getPageString() {
        return pageString;
    }

    public String getTitleString() {
        return titleString;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setCoverBitmap(Bitmap coverBitmap) {
        this.coverBitmap = coverBitmap;
    }

    public void setPageString(String pageString) {
        this.pageString = pageString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    @Override
    public String toString() {
        String str = "Doujin Title: " + titleString + "; Pages: " + pageString;
        return str;
    }
}
