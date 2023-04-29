package com.Hiranmoy.EEE.SEC;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AllBackupApk {

    static Context context;
    public static void get( ){
        MainActivity.infoOfApk = new ArrayList<>();
        File file = new File(MainActivity.myBackupLocationInternal);
        if(!file.exists()) return;
        File[] fileList = file.listFiles();
        for (File f : fileList) {
            if(!f.isDirectory()) continue;
            File[] items = f.listFiles();
            int[] pos = new int[3];
            String str = "base.apk";
            int strLen = str.length();
            int curr = 0;
            for(File f2: items){
             //   Log.d("all information collect", f2.toString()+"  "+str);
                String currDir = f2.toString();
                int len = currDir.length();
                if(currDir.charAt(len-1)=='/') len--;
                boolean ok = true;
                for(int i=strLen-1, j=len-1; i>=0; i--, j--){
                    if(currDir.charAt(j)!=str.charAt(i)){
                        ok = false;
                        break;
                    }
                }
                if(ok==true){
                    pos[0] = curr;
                    break;
                }
            }
            str = "icon.png";
            strLen = str.length();
            curr=0;
            for(File f2: items){
                String currDir = f2.toString();
                int len = currDir.length();
                if(currDir.charAt(len-1)=='/') len--;
                boolean ok = true;
                for(int i=strLen-1, j=len-1; i>=0; i--, j--){
                    if(currDir.charAt(j)!=str.charAt(i)){
                        ok = false;
                        break;
                    }
                }
                if(ok==true){
                    pos[1] = curr;
                    break;
                }
                curr++;
            }
            str = "info.txt";
            strLen = str.length();
            curr=0;
            for(File f2: items){
                String currDir = f2.toString();
                int len = currDir.length();
               // if(currDir.charAt(len-1)=='/') len--;
                boolean ok = true;
                for(int i=strLen-1, j=len-1; i>=0; i--, j--){
                    if(currDir.charAt(j)!=str.charAt(i)){
                        ok = false;
                        break;
                    }
                }
                if(ok==true){
                    pos[2] = curr;
                    break;
                }
                curr++;
            }
            int cnt = 0;
            for(int i=0; i<=2; i++) {
                cnt += pos[i];
            }
          //  Log.d("all information collect", "    "+cnt);
            if(cnt!=3) continue;

            StringBuilder text = new StringBuilder(0);
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(f.toString()+"/info.txt")));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            } catch (IOException e) {
                // Handle the error
            }


            String infoStore = text.toString();

            String filePath = f.toString()+"/icon.png";

            Bitmap bitmap = BitmapFactory.decodeFile(filePath);

            Drawable icon = new BitmapDrawable(context.getResources(), bitmap);

            curr = 0;

            StringBuilder pack = new StringBuilder(0), Date = new StringBuilder(0), appName = new StringBuilder(0);
            int len = infoStore.length();
            while(curr<len){
                if(infoStore.charAt(curr)==' ' || infoStore.charAt(curr)=='\n') curr++;
                else break;
            }
            for(int i= curr ; i<len; i++){
                if(infoStore.charAt(i)==' ' || infoStore.charAt(i)=='\n'){
                    curr=i+1;
                    break;
                }
                pack.append(infoStore.charAt(i));
            }


            while(curr<len){
                if(infoStore.charAt(curr)==' ' || infoStore.charAt(curr)=='\n') curr++;
                else break;
            }
            for(int i= curr ; i<len; i++){
                if(infoStore.charAt(i)==' ' || infoStore.charAt(i)=='\n'){
                    curr=i+1;
                    break;
                }
                Date.append(infoStore.charAt(i));
            }

            while(curr<len){
                if(infoStore.charAt(curr)==' ' || infoStore.charAt(curr)=='\n') curr++;
                else break;
            }
            for(int i= curr ; i<len; i++){
                if(infoStore.charAt(i)=='\n' || infoStore.charAt(i)=='\0'){
                    curr=i+1;
                    break;
                }
                appName.append(infoStore.charAt(i));
            }
            String loc = f.toString()+"/base.apk";
            double apkSize = (double)(new File(loc).length())/1000000D;

            MainActivity.infoOfApk.add(new InfoForInstalledApk(pack.toString() , loc, appName.toString() , icon, apkSize, Date.toString() ));

        }

    }

}
