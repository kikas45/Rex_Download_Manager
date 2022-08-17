package com.example.rexdownloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.TimerTask;

public class updaterclass extends MainActivity{

    @SuppressLint("NewApi")
    public static void loadDer(Context ckt, DLAdapter.MyViewHolder holder, int position) {





        runnable = new Runnable() {
            @Override
            public void run() {
                getDnLStatus.getDownloadStatus(ckt, holder, position);
                //Toast.makeText(ckt, "yes", Toast.LENGTH_SHORT).show();
                handler.postDelayed(runnable, 1000);

            }
        };



    }
}
