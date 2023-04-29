package com.Hiranmoy.EEE.SEC;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;

public class RestoreAPKFile {
    private static final int REQUEST_INSTALL_PACKAGES = 2580;
    static Context context;
    static PackageManager packageManager;
    static String packageName;
    public static void restore(String dest, String pack){
        if(checkForPermission()){
            restore(dest);

        }
        else{
            Toast.makeText(context, "Permission Required!", Toast.LENGTH_SHORT).show();
        }
    }
    private static void restore(String dest){
        Uri uri = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider", new File(dest));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    private static boolean checkForPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Check if the app has permission to install APKs
            boolean hasInstallPermission = packageManager.canRequestPackageInstalls();
            if (!hasInstallPermission) {
                // Request the permission
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                intent.setData(Uri.parse("package:" + packageName));


                context.startActivity(intent);
            } else {
                // The app has permission, so proceed with the installation
                return true;
            }
        } else {
            // The app does not target Android 8.0 or higher, so no need to request the permission
            return true;
        }
        return packageManager.canRequestPackageInstalls();
    }


}
