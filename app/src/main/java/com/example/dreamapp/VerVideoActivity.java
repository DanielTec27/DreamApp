package com.example.dreamapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.dreamapp.videoFrame.AdapterFrame;
import com.example.dreamapp.videoFrame.ModelFrame;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class VerVideoActivity extends AppCompatActivity {
    private ArrayList<ModelFrame> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_video);
        ListView listView = findViewById(R.id.listView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String path = Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera";
        Log.d("Files", "Path" + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        reverse(files);
        Log.d("Files", "Size"+ files.length);
        for (int i = 0; i < files.length; i++){
            if (files[i].getName().contains(".mp4")){
                Log.d("Files", "FileName:"+ files[i].getName());
                items.add(new ModelFrame(files[i].getPath(), files[i].getName()));
            }
        }

        AdapterFrame adapterFrame = new AdapterFrame(this, R.layout.preview_layout, items);
        listView.setAdapter(adapterFrame);
    }

    private void reverse(File myArray[]) {
        Collections.reverse(Arrays.asList(myArray));

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }









}