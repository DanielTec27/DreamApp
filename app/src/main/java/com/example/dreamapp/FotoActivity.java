package com.example.dreamapp;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FotoActivity extends AppCompatActivity {


    String ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (ContextCompat.checkSelfPermission(FotoActivity.this,
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(FotoActivity.this,
                        CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FotoActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 1000);
        }


    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    final int CAPTURA_IMAGEN = 1;

    public void tomarFoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File fotoArchivo = null;
            try {
                fotoArchivo = GuardarImagen();
            } catch (IOException ex) {
                Log.e("Error", ex.toString());
            }
            if (fotoArchivo != null) {//"com.example.dreamapp.fileprovider/"+fechaactual
                Uri uri = FileProvider.getUriForFile(this, "com.example.dreamapp.fileprovider", fotoArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAPTURA_IMAGEN);
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURA_IMAGEN && resultCode == RESULT_OK) {
            Bitmap imgBitmap = BitmapFactory.decodeFile(ruta);
            try {
                FileOutputStream fos = openFileOutput(crearNombreArchivoJPG(), Context.MODE_PRIVATE);
                imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (Exception e) {

            }
        }
    }

    private String crearNombreArchivoJPG() {
        String fecha = new SimpleDateFormat("yyyyyMMdd_HHmmsss").format(new Date());
        return fecha + ".jpg";
    }

    public void verFotos(View v) {
        Intent intent = new Intent(this, VerFotoActivity.class);

        startActivity(intent);
    }



    private File GuardarImagen() throws IOException {
        String nombreFoto = "foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File foto = File.createTempFile(nombreFoto, ".jpg", directorio);
        ruta = foto.getAbsolutePath();
        return foto;
    }




}