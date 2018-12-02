package com.example.thirty.tiredface2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class ImageToGallery {
    //String을 이미지로 변환하여 저장하는 메소드
    public void stringImageToGallery(String image) {
        //비트맵으로 변환
        byte[] encoded = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encoded, 0, encoded.length);

        //파일 생성
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File("/storage/emulated/0/DCIM/Camera/");
        myDir.mkdirs();
        String fname = "/Image-testImage.jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();   //이미 같은 이름으로 파일이 존재하면 그 파일을 삭제

        //파일로 이미지를 출력
        Log.i("DevelopLog", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
