package com.papuase.zeerovers.tm.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.papuase.zeerovers.tm.Adapter.ListPhotoAdapter;
import com.papuase.zeerovers.tm.Api.BaseUrl;
import com.papuase.zeerovers.tm.Api.Mysingleton;
import com.papuase.zeerovers.tm.Model.ListPhotoModel;
import com.papuase.zeerovers.tm.R;
import com.papuase.zeerovers.tm.Utils.SharedPrefDataTask;
import com.papuase.zeerovers.tm.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class GalleryPhotoActivity extends AppCompatActivity {

    private static final String TAG = "GalleryPhotoActivity";

    FloatingActionButton fabAddUpload;
    private List<ListPhotoModel> listPhotoModels = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String jsonStr,ResultWS;
    String id, vid, noTask, statusTask;
    SharedPrefManager sharedPrefManager;
    SharedPrefDataTask sharedPrefDataTask;
    TextView mDataNull;
    Button mRerty;
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("PHOTO");

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefDataTask = new SharedPrefDataTask(this);

        mDataNull = findViewById(R.id.tvDataKosongGaleryPhoto);
        mRerty = findViewById(R.id.btnRetryGaleryPhoto);
        mProgress = findViewById(R.id.progress_galery_foto);

        Intent in = getIntent();
        id = sharedPrefDataTask.getIDDATABARANGSAVE();
        vid = in.getStringExtra(SharedPrefDataTask.VID);
        noTask = in.getStringExtra(SharedPrefDataTask.NoTask);
        statusTask = in.getStringExtra(SharedPrefDataTask.idJenisTask);

        Log.i(TAG, "id: " + id);
        Log.i(TAG, "vid: " + vid);
        Log.i(TAG, "statusTask: " + statusTask);
        Log.i(TAG, "noTask: " + noTask);

        recyclerView = findViewById(R.id.gridView);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(GalleryPhotoActivity.this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ListPhotoAdapter(getApplicationContext(), listPhotoModels);

        recyclerView.setAdapter(adapter);

        fabAddUpload = findViewById(R.id.fabAddUploads);
        fabAddUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadPhotoActivity.class);
                intent.putExtra(SharedPrefDataTask.VID,vid);
                intent.putExtra(SharedPrefDataTask.NoTask,noTask);
                intent.putExtra(SharedPrefDataTask.idJenisTask, statusTask);
                startActivity(intent);
            }
        });


        new AsyingGaleryPhoto().execute();


    }


    private class AsyingGaleryPhoto extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getGaleryPhoto();

        }

        @Override
        protected String doInBackground(Void ... arg0) {
            return "OK";
        }
    }

    @SuppressLint("RestrictedApi")
    public void getGaleryPhoto(){
        mProgress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        fabAddUpload.setVisibility(View.GONE);
        String url = BaseUrl.getPublicIp + BaseUrl.listFoto + vid;
        Log.i(TAG, "NoTask DataSurvey: " + statusTask);
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "Url GaleryPhoto: " + url);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            ResultWS = jsonObj.getString("Result");

                            if (ResultWS.equals("True")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                Log.d("TAG", "onResponse: " + jsonArray);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    Log.i(TAG, "status : "+statusTask);
                                    if (statusTask.equals("1")) {
                                        Log.i(TAG, "listPhoto: " + data);
                                        Log.i(TAG, "listPhoto: " + data.getString("file_url"));
                                        Log.i(TAG, "listPhoto: " + data.getString("VID"));
                                        Log.i(TAG, "listPhoto: " + data.getString("Description"));
                                        Log.i(TAG, "listPhoto: " + data.getString("Keterangan"));
                                        ListPhotoModel md = new ListPhotoModel(
                                                data.getString("file_url"),
                                                data.getString("Keterangan")
                                        );
                                        Log.i(TAG, "modelSpd: " + md);
                                        listPhotoModels.add(md);
                                        mProgress.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        fabAddUpload.setVisibility(View.VISIBLE);
                                    }
                                    else if (statusTask.equals("4")) {
                                        Log.i(TAG, "listPhoto: " + data);
                                        Log.i(TAG, "listPhoto: " + data.getString("file_url"));
                                        Log.i(TAG, "listPhoto: " + data.getString("VID"));
                                        Log.i(TAG, "listPhoto: " + data.getString("Description"));
                                        Log.i(TAG, "listPhoto: " + data.getString("Keterangan"));
                                        ListPhotoModel md = new ListPhotoModel(
                                                data.getString("file_url"),
                                                data.getString("Keterangan")
                                        );
                                        Log.i(TAG, "modelSpd: " + md);
                                        listPhotoModels.add(md);
                                        mProgress.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        fabAddUpload.setVisibility(View.GONE);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }else {
                                String Responsecode = jsonObj.getString("Data1");
                                Log.i(TAG, "else Response True: " + Responsecode);
                                Toasty.info(GalleryPhotoActivity.this, Responsecode, Toast.LENGTH_LONG).show();
                                mDataNull.setText("Data Belum Di Tambahkan Foto");
                                recyclerView.setVisibility(View.GONE);
                                fabAddUpload.setVisibility(View.VISIBLE);
                                mProgress.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "JSONException : " + e.getMessage());
                            mDataNull.setText("Cek Jaringan Anda");
                            mRerty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            fabAddUpload.setVisibility(View.GONE);
                            mProgress.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                        Log.i(TAG, "onErrorResponse: " + error.getMessage());
                        mDataNull.setText("Cek Jaringan Anda");
                        mRerty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        fabAddUpload.setVisibility(View.GONE);
                        mProgress.setVisibility(View.GONE);

                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Header", "Dota2");
                return headers;
            }
        };

        Mysingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
//            onBackPressed();
            Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(SharedPrefManager.SP_ID, id);
            intent.putExtra(SharedPrefDataTask.VID, vid);
            intent.putExtra(SharedPrefDataTask.NoTask, noTask);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
