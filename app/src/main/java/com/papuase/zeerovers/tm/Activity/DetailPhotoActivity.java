package com.papuase.zeerovers.tm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.papuase.zeerovers.tm.R;
import com.squareup.picasso.Picasso;



public class DetailPhotoActivity extends AppCompatActivity {

    ImageView imageView;
    TextView mDescripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Foto");


        imageView = findViewById(R.id.imageViewDetail);
        mDescripsi = findViewById(R.id.tvDescription);

        Intent i=getIntent();
        String imageurl=i.getStringExtra("Image");
        String desc=i.getStringExtra("Desc");

        Log.i("TAG", "onCreate: "+ imageurl);
        Log.i("TAG", "onCreate: "+ desc);
        Picasso.with(this)
                .load(imageurl)
                .into(imageView);
        mDescripsi.setText(desc);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
