package com.Hiranmoy.EEE.SEC;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InstalledApkRVAdapter extends RecyclerView.Adapter<InstalledApkRVAdapter.MyViewHolder>{
    private ArrayList<InfoForInstalledApk> mData;
    public static Context context;

    public InstalledApkRVAdapter(ArrayList<InfoForInstalledApk> data) {
        mData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.installed_apk_item, parent, false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.icon.setImageDrawable(mData.get(position).icon);
        holder.appName.setText(mData.get(position).appName);
        holder.packageName.setText(mData.get(position).packageName);
        holder.apkSize.setText(String.format("%.2f", mData.get(position).apkSize)+" MB");
        StringBuilder compoLoc = new StringBuilder(0);
        String str = mData.get(position).location;
        if(str.length()<=47) compoLoc.append(str);
        else{
            compoLoc.append("....");
            for(int i=str.length()-44; i<str.length(); i++) compoLoc.append(str.charAt(i));
        }
        holder.directory.setText("location: "+compoLoc.toString());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        public ImageView icon;
        public TextView appName, packageName, directory, apkSize;
        public ConstraintLayout constraintLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.apkicon);
            appName = itemView.findViewById(R.id.app_name);
            packageName = itemView.findViewById(R.id.package_name);
            directory = itemView.findViewById(R.id.direcotryT);
            apkSize = itemView.findViewById(R.id.apkSize);
            constraintLayout = itemView.findViewById(R.id.installedapklayout);
            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }
        private void showPopupMenu(View v){
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            if(MainActivity.currTab==0){
                popupMenu.inflate(R.menu.istalledapk_rv_popupmenu);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
            }
            else{
                popupMenu.inflate(R.menu.restoreapk_rv_popupmenu);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
            }

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.popupmenu_bakcup:
                    try {
                        MakeAppBackup.performAction(MainActivity.infoOfApk.get(getAdapterPosition()));
                        Toast.makeText(context,"Backup process completed successfully",Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(context,"Backup process failed",Toast.LENGTH_LONG).show();
                    }
                    return true;
                case R.id.popupmenu_restore:

                    RestoreAPKFile.restore(MainActivity.infoOfApk.get(getAdapterPosition()).location, MainActivity.infoOfApk.get(getAdapterPosition()).packageName);
                  //  Toast.makeText(context,"Installation process successful",Toast.LENGTH_LONG).show();

                    return true;
                case R.id.popupmenu_delete:
                    DeleteDirectory.del(new File(MainActivity.myBackupLocationInternal+MainActivity.infoOfApk.get(getAdapterPosition()).packageName));
                    MainActivity.setForRestoreApk(context);
                    return true;
            }
            return false;
        }
    }
}
