package com.papuase.zeerovers.tm.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.papuase.zeerovers.tm.Api.BaseUrl;
import com.papuase.zeerovers.tm.Api.Mysingleton;
import com.papuase.zeerovers.tm.Helper.DatePickerView;
import com.papuase.zeerovers.tm.R;
import com.papuase.zeerovers.tm.Utils.SharedPrefDataTask;
import com.papuase.zeerovers.tm.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class GeneralInfoActivity extends AppCompatActivity {

    private static final String TAG = "GeneralInfoActivity";

    DatePickerView mTglSelesai, mTglBerangkat, mTglPulang, mTglStatusPerbaikan;
    EditText mTask, mVid, mSid, mOrder,mIdAtm, mIpLan, mLaporan, mStatusCheck, mCatatan;
    Button mBtnSave;
    String jsonStr,ResultWS, statusGeneralInfo;
    Boolean statusGI = false;
    SharedPrefManager sharedPrefManager;
    SharedPrefDataTask sharedPrefDataTask;

    String id, noTask;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("GENERAL INFO");

        Intent in = getIntent();
        id = in.getStringExtra(SharedPrefManager.SP_ID);
        noTask = in.getStringExtra(SharedPrefDataTask.NoTask);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefDataTask = new SharedPrefDataTask(this);
        progressDialog = new ProgressDialog(this);
        mTask = findViewById(R.id.no_task);
        mTask.setEnabled(false);
        mVid = findViewById(R.id.no_vid);
        mVid.setEnabled(false);
        mSid = findViewById(R.id.no_sid);
        mSid.setEnabled(false);
        mLaporan = findViewById(R.id.laporan_pengaduan);
        mLaporan.setEnabled(false);
        mStatusCheck = findViewById(R.id.status_check_koordinator);
        mStatusCheck.setEnabled(false);
        mOrder = findViewById(R.id.order);
        mOrder.setEnabled(false);
        mIpLan = findViewById(R.id.ip_lan);
        mIdAtm = findViewById(R.id.id_atm);
        mTglBerangkat = findViewById(R.id.tgl_berangkat);
        mTglSelesai = findViewById(R.id.tgl_selesai);
        mTglPulang = findViewById(R.id.tgl_pulang);
        mTglStatusPerbaikan = findViewById(R.id.tgl_status_perbaikan);
        mCatatan = findViewById(R.id.catatan_koordinator);
        mCatatan.setEnabled(false);
        mBtnSave = findViewById(R.id.save_general_info);


        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGeneralInfo();
            }
        });

        new AsyingGeneralInfo().execute();
//        dataGeneralInfo();
    }

    private class AsyingGeneralInfo extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dataGeneralInfo();

        }

        @Override
        protected String doInBackground(Void ... arg0) {
            return "OK";
        }
    }

    public void dataGeneralInfo(){
        progressDialog.show();
        Log.i(TAG, "NoTask General Info: " + id);
        String url = BaseUrl.getPublicIp + BaseUrl.detailTask+id+"/"+sharedPrefManager.getSPUserName();
        Log.i(TAG, "dataGeneralInfo: " + url);
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new Handler().postDelayed(new Runnable() {
                            @SuppressLint("SimpleDateFormat")
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    ResultWS = jsonObj.getString("Result");

                                    if (ResultWS.equals("True")) {
                                        JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                        Log.d("TAG", "onResponse: " + jsonArray);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject data = jsonArray.getJSONObject(i);

                                            String statusGeneralInfo = data.getString("IdStatusPerbaikan");
                                            String statusGI = data.getString("FlagGeneralInfo");
                                            Log.i("TAG", "run: "+ statusGI);

                                            Log.i("TAG", "status : "+statusGeneralInfo);
                                            if (statusGeneralInfo.equals("1")) {
                                                Log.i(TAG, "GeneralInfo: " + data.getString("NoTask"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("VID1"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("SID"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IPLAN"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IdATM"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("LaporanPengaduan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IdStatusKoordinator"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("idJenisTask1"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglBerangkat"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglSelesaiKerjaan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglPulang"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglStatusPerbaikan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("CatatanKoordinator"));

                                                if (data.getString("NoTask").equals("null") || data.getString("NoTask").equals(null)){
                                                    String noTask = "";
                                                    mTask.setText(noTask);
                                                }else {
                                                    String noTask = data.getString("NoTask");
                                                    mTask.setText(noTask);
                                                }
                                                if (data.getString("VID1").equals("null") || data.getString("VID1").equals(null)){
                                                    String vid = "";
                                                    mVid.setText(vid);
                                                }else {
                                                    String vid = data.getString("VID1");
                                                    mVid.setText(vid);
                                                }
                                                if (data.getString("SID").equals("null") || data.getString("SID").equals(null)){
                                                    String noSid = "";
                                                    mSid.setText(noSid);
                                                }else {
                                                    String noSid = data.getString("SID");
                                                    mSid.setText(noSid);
                                                }
                                                if (data.getString("IPLAN").equals("null") || data.getString("IPLAN").equals(null)){
                                                    String ipLan = "";
                                                    mIpLan.setText(ipLan);
                                                }else {
                                                    String ipLan = data.getString("IPLAN");
                                                    mIpLan.setText(ipLan);
                                                }
                                                if (data.getString("IdATM").equals("null") || data.getString("IdATM").equals(null)){
                                                    String idAtm = "";
                                                    mIdAtm.setText(idAtm);
                                                }else {
                                                    String idAtm = data.getString("IdATM");
                                                    mIdAtm.setText(idAtm);
                                                }
                                                if (data.getString("LaporanPengaduan").equals("null") || data.getString("LaporanPengaduan").equals(null)){
                                                    String LaporanPengaduan = "";
                                                    mLaporan.setText(LaporanPengaduan);
                                                }else {
                                                    String LaporanPengaduan = data.getString("LaporanPengaduan");
                                                    mLaporan.setText(LaporanPengaduan);
                                                }
                                                if (data.getString("IdStatusKoordinator").equals("null") || data.getString("IdStatusKoordinator").equals(null)){
                                                    String StatusCheck = "";
                                                    mStatusCheck.setText(StatusCheck);
                                                }else {
                                                    String StatusCheck = data.getString("IdStatusKoordinator");
                                                    mStatusCheck.setText(StatusCheck);
                                                }
                                                if (data.getString("idJenisTask1").equals("null") || data.getString("idJenisTask1").equals(null)){
                                                    String order = "";
                                                    mOrder.setText(order);
                                                }else {
                                                    String order = data.getString("idJenisTask1");
                                                    mOrder.setText(order);
                                                }
                                                if (data.getString("CatatanKoordinator").equals("null") || data.getString("CatatanKoordinator").equals(null)){
                                                    String CatatanKoordinator = "";
                                                    mCatatan.setText(CatatanKoordinator);
                                                }else {
                                                    String CatatanKoordinator = data.getString("CatatanKoordinator");
                                                    mCatatan.setText(CatatanKoordinator);
                                                }
                                                String tglBerangkat = data.getString("TglBerangkat");
                                                String tglSelesai = data.getString("TglSelesaiKerjaan");
                                                String tglPulang = data.getString("TglPulang");
                                                String tglStatusPerbaikan = data.getString("TglStatusPerbaikan");
                                                if (tglBerangkat.equals("null") && tglSelesai.equals("null") && tglPulang.equals("null") && tglStatusPerbaikan.equals("null") || tglBerangkat.equals(null) && tglSelesai.equals(null) && tglPulang.equals(null) && tglStatusPerbaikan.equals(null)){
                                                    mTglBerangkat.setText("");
                                                    mTglSelesai.setText("");
                                                    mTglPulang.setText("");
                                                    mTglStatusPerbaikan.setText("");
                                                } else {
                                                    Date date2 = null;
                                                    Date date3 = null;
                                                    Date date4 = null;
                                                    Date date5 = null;
                                                    try {
                                                        date2 = new SimpleDateFormat("dd-MM-yyyy").parse(tglBerangkat);
                                                        date3 = new SimpleDateFormat("dd-MM-yyyy").parse(tglSelesai);
                                                        date4 = new SimpleDateFormat("dd-MM-yyyy").parse(tglPulang);
                                                        date5 = new SimpleDateFormat("dd-MM-yyyy").parse(tglStatusPerbaikan);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    String dateString1 = new SimpleDateFormat("yyyy-MM-dd").format(date2);
                                                    String dateString2 = new SimpleDateFormat("yyyy-MM-dd").format(date3);
                                                    String dateString3 = new SimpleDateFormat("yyyy-MM-dd").format(date4);
                                                    String dateString4 = new SimpleDateFormat("yyyy-MM-dd").format(date5);

                                                    mTglBerangkat.setText(dateString1);
                                                    mTglSelesai.setText(dateString2);
                                                    mTglPulang.setText(dateString3);
                                                    mTglStatusPerbaikan.setText(dateString4);
                                                }
                                            }

                                            if (statusGeneralInfo.equals("4")) {
                                                Log.i(TAG, "GeneralInfo: " + data.getString("NoTask"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("VID1"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("SID"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IPLAN"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IdATM"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("LaporanPengaduan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IdStatusKoordinator"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("idJenisTask1"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglBerangkat"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglSelesaiKerjaan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglPulang"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglStatusPerbaikan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("CatatanKoordinator"));

                                                if (data.getString("NoTask").equals("null") || data.getString("NoTask").equals(null)){
                                                    String noTask = "";
                                                    mTask.setText(noTask);
                                                    mTask.setEnabled(false);
                                                }else {
                                                    String noTask = data.getString("NoTask");
                                                    mTask.setText(noTask);
                                                    mTask.setEnabled(false);
                                                }
                                                if (data.getString("VID1").equals("null") || data.getString("VID1").equals(null)){
                                                    String vid = "";
                                                    mVid.setText(vid);
                                                    mVid.setEnabled(false);
                                                }else {
                                                    String vid = data.getString("VID1");
                                                    mVid.setText(vid);
                                                    mVid.setEnabled(false);
                                                }
                                                if (data.getString("SID").equals("null") || data.getString("SID").equals(null)){
                                                    String noSid = "";
                                                    mSid.setText(noSid);
                                                    mSid.setEnabled(false);
                                                }else {
                                                    String noSid = data.getString("SID");
                                                    mSid.setText(noSid);
                                                    mSid.setEnabled(false);
                                                }
                                                if (data.getString("IPLAN").equals("null") || data.getString("IPLAN").equals(null)){
                                                    String ipLan = "";
                                                    mIpLan.setText(ipLan);
                                                    mIpLan.setEnabled(false);
                                                }else {
                                                    String ipLan = data.getString("IPLAN");
                                                    mIpLan.setText(ipLan);
                                                    mIpLan.setEnabled(false);
                                                }
                                                if (data.getString("IdATM").equals("null") || data.getString("IdATM").equals(null)){
                                                    String idAtm = "";
                                                    mIdAtm.setText(idAtm);
                                                    mIdAtm.setEnabled(false);
                                                }else {
                                                    String idAtm = data.getString("IdATM");
                                                    mIdAtm.setText(idAtm);
                                                    mIdAtm.setEnabled(false);
                                                }
                                                if (data.getString("LaporanPengaduan").equals("null") || data.getString("LaporanPengaduan").equals(null)){
                                                    String LaporanPengaduan = "";
                                                    mLaporan.setText(LaporanPengaduan);
                                                    mLaporan.setEnabled(false);
                                                }else {
                                                    String LaporanPengaduan = data.getString("LaporanPengaduan");
                                                    mLaporan.setText(LaporanPengaduan);
                                                    mLaporan.setEnabled(false);
                                                }
                                                if (data.getString("IdStatusKoordinator").equals("null") || data.getString("IdStatusKoordinator").equals(null)){
                                                    String StatusCheck = "";
                                                    mStatusCheck.setText(StatusCheck);
                                                    mStatusCheck.setEnabled(false);
                                                }else {
                                                    String StatusCheck = data.getString("IdStatusKoordinator");
                                                    mStatusCheck.setText(StatusCheck);
                                                    mStatusCheck.setEnabled(false);
                                                }
                                                if (data.getString("idJenisTask1").equals("null") || data.getString("idJenisTask1").equals(null)){
                                                    String order = "";
                                                    mOrder.setText(order);
                                                    mOrder.setEnabled(false);
                                                }else {
                                                    String order = data.getString("idJenisTask1");
                                                    mOrder.setText(order);
                                                    mOrder.setEnabled(false);
                                                }
                                                if (data.getString("CatatanKoordinator").equals("null") || data.getString("CatatanKoordinator").equals(null)){
                                                    String CatatanKoordinator = "";
                                                    mCatatan.setText(CatatanKoordinator);
                                                    mCatatan.setEnabled(false);
                                                }else {
                                                    String CatatanKoordinator = data.getString("CatatanKoordinator");
                                                    mCatatan.setText(CatatanKoordinator);
                                                    mCatatan.setEnabled(false);
                                                }
                                                String tglBerangkat = data.getString("TglBerangkat");
                                                String tglSelesai = data.getString("TglSelesaiKerjaan");
                                                String tglPulang = data.getString("TglPulang");
                                                String tglStatusPerbaikan = data.getString("TglStatusPerbaikan");

                                                if (tglBerangkat.equals("null") && tglSelesai.equals("null") && tglPulang.equals("null") && tglStatusPerbaikan.equals("null")){
                                                    mTglBerangkat.setText(tglBerangkat);
                                                    mTglSelesai.setText(tglSelesai);
                                                    mTglPulang.setText(tglPulang);
                                                    mTglStatusPerbaikan.setText(tglStatusPerbaikan);
                                                } else {
                                                    Date date6 = null;
                                                    Date date7 = null;
                                                    Date date8 = null;
                                                    Date date9 = null;
                                                    try {
                                                        date6 = new SimpleDateFormat("dd-MM-yyyy").parse(tglBerangkat);
                                                        date7 = new SimpleDateFormat("dd-MM-yyyy").parse(tglSelesai);
                                                        date8 = new SimpleDateFormat("dd-MM-yyyy").parse(tglPulang);
                                                        date9 = new SimpleDateFormat("dd-MM-yyyy").parse(tglStatusPerbaikan);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    String dateString1 = new SimpleDateFormat("yyyy-MM-dd").format(date6);
                                                    String dateString2 = new SimpleDateFormat("yyyy-MM-dd").format(date7);
                                                    String dateString3 = new SimpleDateFormat("yyyy-MM-dd").format(date8);
                                                    String dateString4 = new SimpleDateFormat("yyyy-MM-dd").format(date9);

                                                    mTglBerangkat.setText(dateString1);
                                                    mTglBerangkat.setEnabled(false);
                                                    mTglSelesai.setText(dateString2);
                                                    mTglSelesai.setEnabled(false);
                                                    mTglPulang.setText(dateString3);
                                                    mTglPulang.setEnabled(false);
                                                    mTglStatusPerbaikan.setText(dateString4);
                                                    mTglStatusPerbaikan.setEnabled(false);
                                                }


                                                mBtnSave.setVisibility(View.GONE);

                                            }
                                        }
                                        progressDialog.dismiss();

                                    }else {
                                        progressDialog.dismiss();
                                        String Responsecode = jsonObj.getString("Data1");
                                        Log.i(TAG, "else Response True: " + Responsecode);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Log.i(TAG, "JSONException : " + e.getMessage());

                                }

                            }
                        },200);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.getMessage();
                        Log.i("TAG", "onErrorResponse: " + error.getMessage());

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

    private void updateGeneralInfo() {
        String url = BaseUrl.getPublicIp+BaseUrl.updateGeneralInfo;
        Log.i("url", "General info: "+ url);

        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime());
        Log.i("Date", "Realtime Date: " + date);
        String nameTeknisi = sharedPrefManager.getSPUserName();
        String vid = mVid.getText().toString();
        String ipLan = mIpLan.getText().toString();
        String idAtm = mIdAtm.getText().toString();
        String tglBerangkat = mTglBerangkat.getText().toString();
        String tglSelesai = mTglSelesai.getText().toString();
        String tglPulang = mTglPulang.getText().toString();
        String tglStatusPerbaikan = mTglStatusPerbaikan.getText().toString();
        Boolean statusGi = statusGI = true;

        if (ipLan.matches("")) {
            mIpLan.setError("isi data ini");
        }
        else if (idAtm.matches("")){
            mIdAtm.setError("isi data ini");
        }
        else if (tglBerangkat.matches("")){
            mTglBerangkat.setError("isi data ini");
        }
        else if (tglSelesai.matches("")){
            mTglSelesai.setError("isi data ini");
        }
        else if (tglPulang.matches("")){
            mTglPulang.setError("isi data ini");
        }
        else if (tglStatusPerbaikan.matches("")){
            mTglStatusPerbaikan.setError("isi data ini");
        }
        else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                ResultWS = jsonResponse.getString("Result");
                                if (ResultWS.equals("True")){
                                    String Data1 = jsonResponse.getString("Data1");
                                    Toasty.success(GeneralInfoActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                    Log.i("TAG", "onResponse: " + Data1);

                                    Log.i("TAG","Ski data from server - ok" + response );

                                    Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra(SharedPrefManager.SP_ID, id);
                                    startActivity(intent);
                                    finish();
                                }
                                Log.i("TAG","Ski data from server - ok" + response );

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toasty.error(GeneralInfoActivity.this, "Failure!", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("TAG","Ski error connect - " + error);
                            Toasty.error(GeneralInfoActivity.this, "Error!", Toast.LENGTH_SHORT, true).show();
                        }
                    }){

                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Header", "Dota2");
                    return headers;
                }

                @Override
                public byte[] getBody() {
//                    String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"WhereDatabaseinYou\": \"NoListTask = '"+id+"'\",\"IdATM\": \""+idAtm+"\",\"IPLAN\": \""+ipLan+"\",\"TglBerangkat\": \""+tglBerangkat+"\",\"TglSelesaiKerjaan\": \""+tglSelesai+"\",\"TglPulang\": \""+tglPulang+"\",\"TglStatusPerbaikan\": \""+tglStatusPerbaikan+"\",\"UserUpdate\": \"ADMIN\",\"DateUpdate\": \"2018-09-28\"}],\"PARAM2\": [{\"WhereDatabaseinYou\": \"VID = '"+vid+"'\",\"IPLAN\": \""+ipLan+"\"}],\"PARAM3\": [{\"WhereDatabaseinYou\": \"VID='"+vid+"' and NoTask = '"+noTask+"'\",\"FlagGeneralInfo\": "+statusGi+"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
                    String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"WhereDatabaseinYou\": "+id+",\"IdATM\": \""+idAtm+"\",\"IPLAN\": \""+ipLan+"\",\"TglBerangkat\": \""+tglBerangkat+"\",\"TglSelesaiKerjaan\": \""+tglSelesai+"\",\"TglPulang\": \""+tglPulang+"\",\"TglStatusPerbaikan\": \""+tglStatusPerbaikan+"\",\"UserUpdate\": \"ADMIN\",\"DateUpdate\": \"2018-09-28\",\"FlagGeneralInfo\": "+statusGi+"}],\"PARAM2\": [{\"WhereDatabaseinYou\": "+vid+",\"IPLAN\": \""+ipLan+"\"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
                    Log.i("sendBody", "sendBody: " + str);
                    return str.getBytes();
                }

                public String getBodyContentType()
                {
                    return "application/x-www-form-urlencoded; charset=utf-8";
                }

            };

            Mysingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
