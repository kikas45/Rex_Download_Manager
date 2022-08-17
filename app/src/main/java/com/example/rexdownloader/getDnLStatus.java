package com.example.rexdownloader;

import static android.content.Context.MODE_PRIVATE;


import static com.example.rexdownloader.DLAdapter.models;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.Toast;

import java.util.List;

public class getDnLStatus extends DLAdapter {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private long downloadReference = -1L;

    public getDnLStatus(List<Model> models, Context context) {
        super(models, context);
    }


    public static void getDownloadStatus(Context cxt, final DLAdapter.MyViewHolder holder, int position) {

        String dn_key = String.valueOf(holder.display_key.getText());
        holder.display_key.setText(models.get(position).getKey());

        SharedPreferences sharedPreferences = cxt.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        long num = sharedPreferences.getLong(dn_key, 10L);

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(num);


        try {
            Cursor c = ((DownloadManager) cxt.getSystemService(Context.DOWNLOAD_SERVICE)).query(query);

            if (c.moveToFirst()) {
                @SuppressLint("Range") int bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                @SuppressLint("Range") int bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                final int dl_progress = (int) ((double) bytes_downloaded / (double) bytes_total * 100f);

                // progressBar.setProgress((int) dl_progress);
                holder.name_txt.setText(bytes_downloaded + "/" + bytes_total);

            }

        } catch (Exception e) {

            Toast.makeText(cxt, "cursor failed to move to next", Toast.LENGTH_LONG).show();
        }


        try {

            Cursor c = ((DownloadManager) cxt.getSystemService(Context.DOWNLOAD_SERVICE)).query(query);

            if (c == null) {
                holder.display.setText(statusMessage(c));


            } else {
                c.moveToFirst();
                holder.display.setText(statusMessage(c));
            }

        } catch (Exception e) {

        }


    }


    @SuppressLint({"Range", "SetTextI18n"})
    public static String statusMessage(Cursor c) {
        String msg = "???";

        switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_PENDING:
                msg = "Download pending!";
                break;

            case DownloadManager.STATUS_RUNNING:
                msg = "Download in progress!";
                break;

            case DownloadManager.STATUS_PAUSED:
                msg = "Download paused!";
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Download complete!";
                break;
            case DownloadManager.STATUS_FAILED:
                msg = "Download failed!";
                break;

            default:
                msg = "Download is nowhere in sight";
                break;
        }

        return (msg);
    }



}
