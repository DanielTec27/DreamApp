package com.example.dreamapp.videoFrame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dreamapp.R;

import java.util.ArrayList;

public class AdapterFrame extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<ModelFrame>modelFrames;

    public AdapterFrame(Context context, int layout, ArrayList<ModelFrame> modelFrames) {
        this.context = context;
        this.layout = layout;
        this.modelFrames = modelFrames;
    }

    @Override
    public int getCount() {
        return this.modelFrames.size();
    }

    @Override
    public Object getItem(int i) {
        return modelFrames.get(i);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View v = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        if (convertView == null) {
            v = layoutInflater.inflate(this.layout, null);
        }else{
            v = layoutInflater.inflate(this.layout, null);
        }
        String currentPath = this.modelFrames.get(i).getPath();
        String currentName = this.modelFrames.get(i).getName();
        TextView textView = v.findViewById(R.id.textViewFr);
        ImageView imageView = v.findViewById(R.id.imageViewFr);
        textView.setText(currentName);

        Glide.with(this.context).load(currentPath).centerCrop().into(imageView);
        return null;
    }
}
