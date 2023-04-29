package com.Hiranmoy.EEE.SEC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFromOneDirToAnotherDir {
    public static void cpy(File src, File dest) throws IOException {
        //dest = myBackupLocation/packagename

        File sourceFile = src;
        File destDir = dest;
        File destFile = new File(destDir, "base.apk");

        try {
            FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
