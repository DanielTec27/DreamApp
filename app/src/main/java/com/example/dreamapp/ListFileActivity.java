package com.example.dreamapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dreamapp.Common.Common;
import com.example.dreamapp.adapters.FilesAdapters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFileActivity extends AppCompatActivity {
    List<File> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_file);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        initViews();
    }




    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_files);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        fileList = loadFile();
        FilesAdapters filesAdapters = new FilesAdapters(this, fileList);
        recyclerView.setAdapter(filesAdapters);
    }

    private List<File> loadFile() {

        ArrayList<File> inFiles = new ArrayList<>();
        File parendtDir= new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ File.separator+getString(R.string.app_name));
        File[] files = parendtDir.listFiles();

        for (File file: files){
            if (file.getName().endsWith(".png"))
                inFiles.add(file);
        }
        TextView textView = findViewById(R.id.status_empaty);
        if (files.length>0){

            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
        }
        return inFiles;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)){
            deleteFile(item.getOrder());
            initViews();
        }
        return true;
    }

    private void deleteFile(int order){
        fileList.get(order).delete();
        fileList.remove(order);
    }
}