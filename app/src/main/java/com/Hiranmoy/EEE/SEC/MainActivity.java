package com.Hiranmoy.EEE.SEC;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    static ArrayList<InfoForInstalledApk> infoOfApk;
    static String myBackupLocationInternal;
    static String loc = "/APKBackupAndRestoreFree/";

    static int currTab = 0;



    private static RecyclerView iRecyclerView ;
    private static InstalledApkRVAdapter adapter;
    private static TextView restoreAPK, installedAPK;
    private static View xIview, xRview;
    private static EditText editText;
    private static ImageView searchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBackupLocationInternal = "/storage/emulated/0"+loc;
        File file = new File(myBackupLocationInternal);
        if(!file.exists()) file.mkdirs();
        iRecyclerView = findViewById(R.id.recyclerview);
        AllBackupApk.context = this;
        RelativeInfo.context = this;
        InstalledApkRVAdapter.context = this;
        RestoreAPKFile.context = this;
        RestoreAPKFile.packageName = getPackageName();
        RestoreAPKFile.packageManager = getPackageManager();

        restoreAPK = findViewById(R.id.restoreactivity);
        installedAPK = findViewById(R.id.installedapktxt);
        xIview = findViewById(R.id.viewForInstalled);
        xRview = findViewById(R.id.viewForRestore);
        editText = findViewById(R.id.editSearch);
        searchIcon = findViewById(R.id.iconsearch);

        if(currTab==0){
            getAllAppsForBackup();
            iRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new InstalledApkRVAdapter(infoOfApk);
            iRecyclerView.setAdapter(adapter);
        }
        else{
            AllBackupApk.get();
            iRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new InstalledApkRVAdapter(infoOfApk);
            iRecyclerView.setAdapter(adapter);
        }

        restoreAPK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currTab = 1;
                xRview.setBackgroundColor(getResources().getColor(R.color.LightGrey));
                xIview.setBackgroundColor(getResources().getColor(R.color.teal_200));
                setForRestoreApk(v.getContext());
            }
        });
        installedAPK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currTab = 0;
                xIview.setBackgroundColor(getResources().getColor(R.color.LightGrey));
                xRview.setBackgroundColor(getResources().getColor(R.color.teal_200));
                getAllAppsForBackup();
                iRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
                adapter = new InstalledApkRVAdapter(infoOfApk);
                iRecyclerView.setAdapter(adapter);
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString().toLowerCase();
                ArrayList<InfoForInstalledApk> temp = new ArrayList<>();
                if(currTab==1){
                    AllBackupApk.get();
                    infoOfApk.sort((a,b)->b.date.compareTo(a.date));
                }
                else{
                    getAllAppsForBackup();
                }
                for(InfoForInstalledApk item: infoOfApk){
                    String appName = item.appName.toLowerCase(), pack = item.packageName.toLowerCase();
                    if(appName.contains(text) || pack.contains(text)){
                        temp.add(item);
                    }
                }
                infoOfApk = temp;
                iRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
                adapter = new InstalledApkRVAdapter(infoOfApk);
                iRecyclerView.setAdapter(adapter);
            }
        });














    }
    public static void setForRestoreApk(Context context){
        AllBackupApk.get();
        infoOfApk.sort((a,b)->b.date.compareTo(a.date));
        iRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new InstalledApkRVAdapter(infoOfApk);
        iRecyclerView.setAdapter(adapter);
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu_istalledapp_filter);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.memory_sort:
                infoOfApk.sort((a,b)->(int)(100*a.apkSize)-(int)(100*b.apkSize));
                iRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new InstalledApkRVAdapter(infoOfApk);
                iRecyclerView.setAdapter(adapter);

                return true;
            case R.id.name_sort:
                infoOfApk.sort((a,b)->a.appName.compareTo(b.appName));
                iRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new InstalledApkRVAdapter(infoOfApk);
                iRecyclerView.setAdapter(adapter);

                return true;
            case R.id.date_sort:
                infoOfApk.sort((a,b)->b.date.compareTo(a.date));
                iRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new InstalledApkRVAdapter(infoOfApk);
                iRecyclerView.setAdapter(adapter);
                return true;
            default:
                return false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!Utils.isPermissionGranted(this)){
            /// make a alert dialogue
            new AlertDialog.Builder(this).
                    setTitle("All Files Permission").setMessage("Due to restriction, this app requires storage permission").
                    setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //take permission
                            takePermission();
                        }
                    }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //if press deny
                        }
                    }).show();
        }
        else{
            //permission already granted
        }
    }
    private void getAllAppsForBackup(){
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        infoOfApk = new ArrayList<InfoForInstalledApk>();

        for (ApplicationInfo ai : packages) {

            if((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }
            CharSequence appName = pm.getApplicationLabel(ai);
            long apkSize = new File(ai.sourceDir).length();
            double memory = (double) apkSize/1000000D;
            long lastMod = new File(ai.sourceDir).lastModified();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(lastMod);
            int year = calendar.get(Calendar.YEAR)+10000;
            int month = calendar.get(Calendar.MONTH) + 1+100; // January is 0
            int day = calendar.get(Calendar.DAY_OF_MONTH)+100;
            int minute = calendar.get(Calendar.MINUTE)+100;
            int hour = calendar.get(Calendar.HOUR)+100;
            int sec = calendar.get(Calendar.SECOND)+100;
            String installedTime = year+" "+month+" "+day+" "+hour+" "+minute+" "+sec;
            infoOfApk.add(new InfoForInstalledApk(ai.packageName, ai.sourceDir, appName.toString() , ai.loadIcon(pm), memory, installedTime));
        }

    }
    private void takePermission(){
      //  Log.d("SDK_INT", Integer.toString(Build.VERSION.SDK_INT)+"   "+Integer.toString(Build.VERSION_CODES.R));
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){  ///for android >=11
            try {
                Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                startActivity(intent);

            }
            catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }
        else{
            ActivityCompat.requestPermissions(this , new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            if(requestCode==101){
                boolean readExt = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(!readExt) takePermission();

            }
        }
    }

}