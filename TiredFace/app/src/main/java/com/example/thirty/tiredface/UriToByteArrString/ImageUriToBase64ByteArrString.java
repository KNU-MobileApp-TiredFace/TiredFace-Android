package com.example.thirty.tiredface.UriToByteArrString;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.thirty.tiredface.BitmapToByteStringBase64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

//이미지 파일의 Uri를 Base64로 변경해주는 클래스
public class ImageUriToBase64ByteArrString implements UriToByteArrString {
    //Uri로 파일을 얻어오기 위해서 컨텐트리졸버가 필요
    private ContentResolver contentResolver;

    public ImageUriToBase64ByteArrString(ContentResolver contentResolver){
        this.contentResolver = contentResolver;
    }

    //이미지 파일의 Uri를 받아 비트맵으로 변환
   public Bitmap convertToBitmap(Uri uri){
        //미디어 디렉토리 경로
        String[] filePath = { MediaStore.Images.Media.DATA };

        //커서를 uri의 파일로 이동
        Cursor cursor = contentResolver.query(uri, filePath, null, null, null);
        cursor.moveToFirst();

        //이미지 경로를 얻어옴
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

        //파일 스트림 생성
        File imagefile = new File(imagePath);
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //비트맵 팩토리 옵션 지정
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //크기 압축 비율 지정. 4라고 하면 가로 / 4, 세로 / 4의 크기로 이미지가 변환된다.
        bmOptions.inSampleSize = 4;

        Bitmap bm = null;
        bm = BitmapFactory.decodeStream(fis, null, bmOptions);
        return bm;
    }

    @Override
    public String convert(Uri uri){
        /*
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
        //크기 압축 비율 지정. 4라고 하면 가로 / 4, 세로 / 4의 크기로 이미지가 변환된다.
        bmOptions.inSampleSize = 4;

        BitmapToByteStringBase64 bbsb = new BitmapToByteStringBase64();

        Bitmap bm = null;
        bm = BitmapFactory.decodeStream(fis, null, bmOptions);

        BitmapToByteStringBase64 btbsb = new BitmapToByteStringBase64();
        String encodedImage = btbsb.convert(bm);
*/
        Bitmap bm = convertToBitmap(uri);
        BitmapToByteStringBase64 btbsb = new BitmapToByteStringBase64();
        String encodedImage = btbsb.convert(bm);

        return encodedImage;
    }
}
