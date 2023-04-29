package com.Hiranmoy.EEE.SEC;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class RelativeInfo {
    static Context context;
    public static void set(String packageName, String time, String appName, File dest, Drawable icon) throws IOException {
        File directory = dest;
        //dest = myBackupLocation/packagename

        File file = new File(directory, "info.txt");


        file.createNewFile();

        // Write to the file
        FileWriter writer = new FileWriter(file);
        writer.write(packageName+" "+time+" "+appName);
        writer.close();

        Bitmap bitmap = DrawableToBitmap.get(icon);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        try {
            file = new File(directory, "icon.png");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
