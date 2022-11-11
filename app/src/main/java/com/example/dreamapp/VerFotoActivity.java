package com.example.dreamapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.List;

public class VerFotoActivity extends AppCompatActivity {
    String []archivos3={"image/*"};
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_foto);
        archivos3=fileList();
        recyclerView=findViewById(R.id.recyclerViewFoto);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new AdaptadorFotos());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class AdaptadorFotos extends RecyclerView.Adapter<AdaptadorFotos.AdaptadorFotosHolder> {
        @NonNull
        @Override
        public AdaptadorFotosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorFotosHolder(getLayoutInflater().inflate(R.layout.layout_foto,parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorFotosHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return archivos3.length;
        }

        class AdaptadorFotosHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;

            public AdaptadorFotosHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageFo);
                textView = itemView.findViewById(R.id.textViewFo);
            }

            


            public void imprimir(int position) {
                textView.setText("Nombre del Archivo" + archivos3[position]);
              

                try {
                    FileInputStream fileInputStream = openFileInput(archivos3[position]);

                    Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                    imageView.setImageBitmap(bitmap);

                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        }
    }
