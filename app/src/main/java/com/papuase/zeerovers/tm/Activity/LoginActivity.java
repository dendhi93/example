package com.papuase.zeerovers.tm.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.papuase.zeerovers.tm.Api.BaseUrl;
import com.papuase.zeerovers.tm.Api.Mysingleton;
import com.papuase.zeerovers.tm.R;
import com.papuase.zeerovers.tm.Utils.SharedPrefManager;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
//    int counter = 0;

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private ProgressDialog pDialog;
    private String ResultWS;
    private SharedPrefManager sharedPrefManager;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameEditText = findViewById(R.id.txt_login_un);
        mPasswordEditText = findViewById(R.id.txt_login_pass);

        // Initialize the SharedPreferences
        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getSPUserSudahLogin()){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
//        dummyLogin();
    }


    public void loginBtn(View view) {
        Animation fadein = new AlphaAnimation(0,1);
        fadein.setDuration(50);
        Button login = findViewById(R.id.btnLogin);
        validate();
        login.startAnimation(fadein);
    }

    public void validate(){
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);

        String userName = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (userName.matches("")) {
            mUsernameEditText.setError("Your User Name Empty");
        }
        else if (password.matches("")) {
            mPasswordEditText.setError("Your Password Empty");
        }
        else if (validateLogin(userName,password)){

            String url = BaseUrl.getPublicIp + BaseUrl.login +userName+"/"+password+"";

            Log.i(TAG, "login Url : " + url);
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject paramObject = new JSONObject(response);
                                ResultWS = paramObject.getString("Result");
                                Log.i("LoginResault", "Result: "+ResultWS);
                                if (ResultWS.equals("True")){
                                    JSONArray jsonArray = paramObject.getJSONArray("Raw");
                                    Log.i("LoginResault", "Raw + True: "+jsonArray);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Log.i("LoginResault", "Obj Data: " + jsonObject);
                                        Log.i("LoginResault", "Obj UserName: " + jsonObject.getString("UserName"));
                                        Log.i("LoginResault", "Obj Password: " + jsonObject.getString("Password"));
                                        Log.i("LoginResault", "Obj Nama: " + jsonObject.getString("Nama"));
                                        Log.i("LoginResault", "Obj Email: " + jsonObject.getString("Email"));

                                        String nik = jsonObject.getString("UserName");
                                        String nama = jsonObject.getString("Nama");
                                        String email = jsonObject.getString("Email");

                                        Toasty.success(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT, true).show();
                                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_USER_SUDAH_LOGIN, true);
                                        sharedPrefManager.saveSPString(SharedPrefManager.SP_USER_NAME, nama);
                                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NIK, nik);
                                        sharedPrefManager.saveSPString(SharedPrefManager.SP_EMAIL, email);

                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }

                                }else {
                                    String failedReason = paramObject.getString("Raw");
                                    Toasty.error(LoginActivity.this, "Login Failed\n"+failedReason, Toast.LENGTH_SHORT, true).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toasty.error(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT, true).show();
                            Log.i("TAG", "onResponse: " + error.toString());
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
    }

    private boolean validateLogin(String username, String password){
        if(username == null || username.trim().length() == 0){
            Toasty.warning(LoginActivity.this, "Username is required!", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toasty.warning(LoginActivity.this, "Password is required!", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    public void forgetPassword(View view) {
        Animation fadein = new AlphaAnimation(0,1);
        fadein.setDuration(50);
        Button forget = findViewById(R.id.forget_password);
        forget.startAnimation(fadein);
        Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void singUp(View view) {
        Animation fadein = new AlphaAnimation(0,1);
        fadein.setDuration(50);
        Button singUp = findViewById(R.id.sing_up);
        singUp.startAnimation(fadein);
        Toasty.info(this, "Contact Admin", Toast.LENGTH_SHORT, true).show();
    }

    @Override public void onResume(){
        super.onResume();
        statusCheck();
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        assert manager != null;

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> startActivity(new
                            Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            final AlertDialog alert = builder.create();
            alert.show();
        }

        else if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void buildAlertMessageNoGps() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("GPS");
            if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
                permissionsNeeded.add("Location");
            if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add("Camera");
            if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
                permissionsNeeded.add("External Storage Read");
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("External Storage Write");

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }
}
