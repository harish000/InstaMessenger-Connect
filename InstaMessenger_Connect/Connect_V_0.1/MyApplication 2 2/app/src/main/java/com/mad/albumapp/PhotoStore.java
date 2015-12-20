package com.mad.albumapp;

import android.graphics.Bitmap;

import com.parse.ParseFile;

/**
 * Created by Trishaan on 12/3/15.
 */
public class PhotoStore {
    int flag;
    Bitmap bitmap;
    ParseFile file;

    public ParseFile getFile() {
        return file;
    }

    public void setFile(ParseFile file) {
        this.file = file;
    }



    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
