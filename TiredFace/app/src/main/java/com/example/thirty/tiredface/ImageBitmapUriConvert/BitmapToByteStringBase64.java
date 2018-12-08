package com.example.thirty.tiredface.ImageBitmapUriConvert;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

//비트맵을 String으로 변환해주는 클래스
public class BitmapToByteStringBase64 {
    public String convert(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return encodedImage;
    }
}
