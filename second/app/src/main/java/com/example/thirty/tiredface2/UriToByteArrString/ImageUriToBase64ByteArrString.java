package com.example.thirty.tiredface2.UriToByteArrString;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageUriToBase64ByteArrString implements UriToByteArrString {
    private ContentResolver contentResolver;


    public ImageUriToBase64ByteArrString(ContentResolver contentResolver){
        this.contentResolver = contentResolver;
    }

    @Override
    public String convert(Uri uri){
        // Let's read picked image path using content resolver
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = contentResolver.query(uri, filePath, null, null, null);
        cursor.moveToFirst();

        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

        File imagefile = new File(imagePath);
        FileInputStream fis = null;
        File externalStroage = Environment.getExternalStorageDirectory();

        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        File sd = Environment.getExternalStorageDirectory();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //bmOptions.inSampleSize = 64;
        bmOptions.inSampleSize = 4;

        Bitmap bm = null;
        bm = BitmapFactory.decodeStream(fis, null, bmOptions);

/*
        while(bm == null)
            BitmapFactory.decodeFile(imagefile.getAbsolutePath(),bmOptions);
*/

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100 , baos);

        //byte[] b = ;
        //baos.toByteArray();
        String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        //String encodedImage = "hello";
        return encodedImage;
    }
}
