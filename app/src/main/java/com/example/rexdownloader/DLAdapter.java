package com.example.rexdownloader;

import static android.content.Context.MODE_PRIVATE;

import static com.example.rexdownloader.MainActivity.handler;
import static com.example.rexdownloader.MainActivity.runnable;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DLAdapter extends RecyclerView.Adapter<DLAdapter.MyViewHolder> {


    public static List<Model> models;
    private Context context;


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private long downloadReference = -1L;
    private long num_n = 10L;

    private String aLong = "along";
    private Handler myHandler;


    public DownloadManager manager;

    private static final String imageUrl = "https://th..is.jpg";
    private static String imageName = "mito.png";


    public DLAdapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        if (models != null) {
            return models.size();

        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name_txt, display, link_url, display_key;
        ProgressBar progressBar;
        Button btn_download, btn_cancell, btn_pause, btn_resume, btn_check;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_txt = itemView.findViewById(R.id.name);
            display = itemView.findViewById(R.id.display);
            display_key = itemView.findViewById(R.id.display_key);
            link_url = itemView.findViewById(R.id.url);
            btn_download = itemView.findViewById(R.id.download);
            btn_cancell = itemView.findViewById(R.id.cancel_it);
            btn_pause = itemView.findViewById(R.id.pause);
            btn_resume = itemView.findViewById(R.id.resume);
            btn_check = itemView.findViewById(R.id.check);
            progressBar = itemView.findViewById(R.id.progressBar2);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);

        return new MyViewHolder(view);
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.name_txt.setText(models.get(holder.getAdapterPosition()).getName());
        holder.link_url.setText(models.get(holder.getAdapterPosition()).getUrl());
        holder.display_key.setText(models.get(holder.getAdapterPosition()).getKey());

        String dn_name = String.valueOf(holder.name_txt.getText());
        String dn_url = String.valueOf(holder.link_url.getText());
        String dn_key = String.valueOf(holder.display_key.getText());

        handler.removeCallbacks(runnable);
        getDnLStatus.getDownloadStatus(context, holder, position);
        updaterclass.loadDer(context, holder, position);
        handler.postDelayed(runnable, 1000);


        myHandler = new Handler(Looper.getMainLooper());

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                downloadImage(imageName, imageUrl, aLong);
            }
        }, 2000);


        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(num_n);
                try {

                    Cursor c = ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).query(query);
                    if (c.moveToFirst()) {
                        manager.remove(num_n);

                    }

                } catch (Exception e) {

                }

            }
        }, 3000);

        
        holder.btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseDownload();
                Toast.makeText(context, "Paused", Toast.LENGTH_SHORT).show();
            }
        });
        
        
        holder.btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resumeDownload();
                Toast.makeText(context, "Resumed", Toast.LENGTH_SHORT).show();
            }
        });


        holder.btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                //saveData();
                downloadImage(dn_name, dn_url, dn_key);

                Toast.makeText(context, dn_name + "  ....  " + dn_key, Toast.LENGTH_SHORT).show();

                updaterclass.loadDer(context, holder, position);
                handler.postDelayed(runnable, 1000);


            }
        });


        holder.btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDnLStatus.getDownloadStatus(context, holder, position);
                Toast.makeText(context, "" + dn_key, Toast.LENGTH_SHORT).show();


            }
        });

        holder.btn_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                long num = sharedPreferences.getLong(dn_key, 10L);

                Toast.makeText(context, dn_key +".." + num, Toast.LENGTH_SHORT).show();

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(num);


                try {

                    Cursor c = ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).query(query);
                    if (c.moveToFirst()) {

                        manager.remove(num);
                        holder.display.setText("Display");
                        holder.name_txt.setText("Name");

                        Toast.makeText(context, dn_key +"  ..  " + num, Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void downloadImage(String outFileName, String url, String key_) {


        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        File direct = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/First_Downloads/old");
        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(outFileName);
        request.setDescription("Downloading " + outFileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir("/First_Downloads/old", outFileName);
        downloadReference = manager.enqueue(request);

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(key_, downloadReference);
        editor.apply();


    }


    public boolean pauseDownload() {
        int updatedRow = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("control", 1);

        try {
            updatedRow = context.getContentResolver().update(Uri.parse("content://downloads/my_downloads"), contentValues, "title=?", new String[]{imageName});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0 < updatedRow;
    }


    public boolean resumeDownload() {
        int updatedRow = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("control", 0);

        try {
            updatedRow = context.getContentResolver().update(Uri.parse("content://downloads/my_downloads"), contentValues, "title=?", new String[]{imageName});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0 < updatedRow;


    }

}