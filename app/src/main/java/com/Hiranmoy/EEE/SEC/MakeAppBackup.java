package com.Hiranmoy.EEE.SEC;

import java.io.File;
import java.io.IOException;

public class MakeAppBackup {
    static void performAction(InfoForInstalledApk mData) throws IOException {
        File file = new File(MainActivity.myBackupLocationInternal);
        if(!file.exists()) file.mkdirs();

        String temp = MainActivity.myBackupLocationInternal + mData.packageName;
        File tempFile = new File(temp);
        DeleteDirectory.del(tempFile);
        if(!tempFile.exists()) tempFile.mkdirs();


        CopyFromOneDirToAnotherDir.cpy(new File(mData.location) , tempFile);
        RelativeInfo.set(mData.packageName, CurrentTimeAndDate.get(), mData.appName, tempFile, mData.icon);


    }
}
