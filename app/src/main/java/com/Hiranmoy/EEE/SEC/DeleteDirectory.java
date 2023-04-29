package com.Hiranmoy.EEE.SEC;


import java.io.File;

public class DeleteDirectory {
    public static void del(File file){
        //file = myBackupLocation/packagename
        if(!file.exists()) return;
        if(!file.isDirectory()){
            file.delete();
            return;
        }
        //Toast.makeText(context, file.toString(), Toast.LENGTH_SHORT).show();
        File[] fileList = file.listFiles();

        for (File f : fileList) {
            del(f);
        }
        file.delete();
    }
}
