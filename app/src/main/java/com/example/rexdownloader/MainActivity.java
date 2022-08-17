package com.example.rexdownloader;



import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private   RecyclerView recyclerView;

    public  static Runnable runnable;
    public static Handler handler;

   String url1 = "https://png.pngtree.com/png-vector/20190927/ourmid/pngtree-lovely-bat-clipart-vector-png-element-png-image_1749074.jpg";

   String url2 = "https://firebasestorage.googleapis.com/v0/b/lazy-loading-fa843.appspot.com/o/What%20is%20Physics_.mp4?alt=media&token=ba2cca42-4d6d-4cd9-9e7e-d017995c2ece";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler(Looper.getMainLooper());
       // LocalBroadcastManager.getInstance(this).registerReceiver(mMessagereciever, new IntentFilter("custom_msg"));

        registerReceiver(onComplete, new IntentFilter((DownloadManager.ACTION_DOWNLOAD_COMPLETE)));




        // for storage runtime pwrmsion
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

            }
        }





        List<Model> models = new ArrayList<>();

        models.add(new Model("Powell", url1, "key1"));
        models.add(new Model("James", url1, "key2"));
        models.add(new Model("Lucky", url1, "key3"));
        models.add(new Model("John", url1, "key4"));
        models.add(new Model("Kelly", url2, "key5"));
        models.add(new Model("Henry", url2, "key6"));


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        DLAdapter adapter = new DLAdapter(models, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }





 public  static    BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

              //  Toast.makeText(context, "Broad Cast is complete", Toast.LENGTH_LONG).show();



                //handler.removeCallbacks(runnable);

                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 1000);




            }
        };




    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable);
       handler.postDelayed(runnable, 1000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);

    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
        handler.removeCallbacks(runnable);
        unregisterReceiver(onComplete);



    }


}

