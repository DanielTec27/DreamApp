package com.example.dreamapp;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dreamapp.Common.Common;
import com.example.dreamapp.adapters.ToolsAdapters;
import com.example.dreamapp.interfaces.ToolsListener;
import com.example.dreamapp.model.ToolsItem;
import com.example.dreamapp.widget.PaintView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DibujoActivity extends AppCompatActivity implements ToolsListener {
    private static final int REQUEST_PERMISSION = 1001;
    private static final int PICK_IMAGE = 1000;
    private int defaultColor;

    PaintView mPaintView;
    int colorBackground, colorBrush;
    int brushSize, eraserSize;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dibujo);


        initTools();

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

    private void initTools() {

        colorBackground = Color.WHITE;
        colorBrush = Color.BLACK;

        eraserSize = brushSize = 12;
        mPaintView = findViewById(R.id.paint_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_tools);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        ToolsAdapters toolsAdapters = new ToolsAdapters(loadTools(), this);
        recyclerView.setAdapter(toolsAdapters);
    }

    private List<ToolsItem> loadTools() {

        List<ToolsItem> result = new ArrayList<>();

        result.add(new ToolsItem(R.drawable.brush_24, Common.BRUSH));
        result.add(new ToolsItem(R.drawable.eraser, Common.ERASER));
        result.add(new ToolsItem(R.drawable.image_24, Common.IMAGEN));
        result.add(new ToolsItem(R.drawable.palette_24, Common.COLORS));
        result.add(new ToolsItem(R.drawable.paint, Common.BACKGROUND));

        return result;
    }

    public void finisPaint(View view) {
        finish();
    }

    public void showFiles(View view) {
        startActivity(new Intent(this, ListFileActivity.class));
    }

    public void saveFilet(View view) {

        if (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION);
        }
        else {
            saveBitmap();
        }
    }

    private void saveBitmap() {
        Bitmap bitmap = mPaintView.getBitmap();
        String file_name= UUID.randomUUID() + ".png";

        File folder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ File.separator+getString(R.string.app_name));

        if (!folder.exists()){
            folder.mkdir();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(folder+File.separator+file_name);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            Toast.makeText(this, "Guardar pintura", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            saveBitmap();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSelected(String name) {
        switch (name){
            case Common.BRUSH:
                mPaintView.desableEraser();
                showDialogSize(false);
                break;
            case Common.ERASER:
                mPaintView.enableEraser();
                showDialogSize(true);
                break;

            case Common.BACKGROUND:
                updateColor(name);
                break;
            case Common.COLORS:
                updateColor(name);
                break;

            case Common.IMAGEN:
                getImage();
                break;
        }

    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Selecciona imagen"), PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE && data != null && resultCode == RESULT_OK){
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            mPaintView.setImage(bitmap);

            cursor.close();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateColor(String name) {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(DibujoActivity.this, "Unavaliable", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                if (name.equals(Common.BACKGROUND)){
                    colorBackground = defaultColor;
                    mPaintView.setColorBackground(color);
                }else{
                    colorBrush = defaultColor;
                    mPaintView.setBrushColor(color);
                }

            }
        });

        ambilWarnaDialog.show();


    }

    private void showDialogSize(boolean b) {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null, false);

        TextView toolsSelected =  view.findViewById(R.id.status_tool_selected);
        TextView statusSize = view.findViewById(R.id.status_Sizes);
        ImageView ivTools = view.findViewById(R.id.iv_tools);
        SeekBar seekBar = view.findViewById(R.id.seekBar_size);
        seekBar.setMax(99);

        if (b){
            toolsSelected.setText("Tamaño de la goma");
            ivTools.setImageResource(R.drawable.eraser);
            statusSize.setText("Selecciona tamaño"+brushSize);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    eraserSize = i+1;
                    statusSize.setText("Selecciona tamaño"+eraserSize);
                    mPaintView.setSizeEraser(eraserSize);
                }else{
                    brushSize = i+1;
                    statusSize.setText("Selecciona tamaño"+brushSize);
                    mPaintView.setSizeBrush(brushSize);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setView(view);
        builder.show();

    }



}