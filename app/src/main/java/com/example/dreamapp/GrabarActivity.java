package com.example.dreamapp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GrabarActivity extends AppCompatActivity {
    private static final int GRABA_VIDEO = 1;
    private String videoPath = "";
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




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

    public void Grabar(View view){

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, GRABA_VIDEO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GRABA_VIDEO && resultCode == RESULT_OK){

            try{
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream in = videoAsset.createInputStream();
                //FileOutputStream archivo = openFileOutput(crearNombreArchivoMP4(), Context.MODE_PRIVATE);
                byte[] buf = new byte[1024];
                int len;

                while((len = in.read(buf)) > 0){
                  //  archivo.write(buf, 0, len);

                }
                System.out.println("PATH NEW VIDEO");
                Uri vid = data.getData();
                videoPath = getRealPathFromURI(vid);
                System.out.println(videoPath);
            }catch (IOException e){
                Toast.makeText(this, "Problemas en la grabación", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

   /* private String crearNombreArchivoMP4(){
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre = fecha + ".mp4";
        return nombre;
    }*/


    public void verVideos(View v){
       if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
           Intent intent = new Intent(this, VerVideoActivity.class);
           startActivity(intent);
       }else {
           Intent intent = new Intent(this, VerVideoActivity.class);
           startActivity(intent);
       }
    }
}