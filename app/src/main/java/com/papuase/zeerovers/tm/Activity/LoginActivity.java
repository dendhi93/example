package com.papuase.zeerovers.tm.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import com.papuase.zeerovers.tm.R;
import com.papuase.zeerovers.tm.Utils.SharedPrefManager;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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

    private void dummyLogin() {
        mUsernameEditText.setText("zeerovers");
        mPasswordEditText.setText("tm12345");
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
                                    Toasty.error(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT, true).show();
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
}