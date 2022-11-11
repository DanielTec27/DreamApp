package com.example.dreamapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void Dibujo(View view){
        Intent intent = new Intent(this, DibujoActivity.class);
        intent.putExtra("tipo", "Dibujo");
        startActivity(intent);
    }

    public void Grabar(View view){
        Intent intent = new Intent(this, GrabarActivity.class);
        intent.putExtra("tipo", "Dibujo");
        startActivity(intent);
    }

    public void Foto(View view){
        Intent intent = new Intent(this, FotoActivity.class);
        intent.putExtra("tipo", "Dibujo");
        startActivity(intent);
    }


}