package com.papuase.zeerovers.tm.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.papuase.zeerovers.tm.Fragment.SpdFragment;
import com.papuase.zeerovers.tm.Fragment.TaskFragment;
import com.papuase.zeerovers.tm.R;
import com.papuase.zeerovers.tm.Utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {

    private ViewGroup content_main;

    private TextView mHelp, mLogOut, navUsername, navEmail;
    int counter = 0;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private SharedPrefManager sharedPrefManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        showFragment(new TaskFragment());
                        setTitle(R.string.title_task);
                        return true;
                    case R.id.navigation_dashboard:
                        showFragment(new SpdFragment());
                        setTitle(R.string.title_spd);
                        return true;
                }
                return false;
            };

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        content_main.animate().alpha(0f).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(R.string.title_task);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sharedPrefManager = new SharedPrefManager(this);

        content_main = findViewById(R.id.content_main);
        showFragment(new TaskFragment());

        drawerLayout = findViewById(R.id.nav_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        mHelp = findViewById(R.id.help);
        mHelp.setOnClickListener(v -> {
            Toasty.info(HomeActivity.this, "Help and FAQ.", Toast.LENGTH_SHORT, true).show();
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        mLogOut = findViewById(R.id.log_out);
        mLogOut.setOnClickListener(v -> {
            logout();
            drawerLayout.closeDrawer(GravityCompat.START);
        });


        String username = sharedPrefManager.getSPUserName();
        String useremail = sharedPrefManager.getSpEmail();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navUsername = headerView.findViewById(R.id.user_name);
        navEmail = headerView.findViewById(R.id.user_email);
        navUsername.setText(username);
        navEmail.setText(useremail);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (item.isChecked()){
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
            if (id == R.id.nav_setting){
                Intent next = new Intent(getApplicationContext(), ChangePasswordActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(next);
            }
//            else if (id == R.id.nav_logOut){
//                logout();
//            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;

            }
        });

    }

    public void logout(){
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are You Sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_USER_SUDAH_LOGIN, false);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        counter++;
        if(counter > 1){
            System.exit(3);
            finish();
        }else{
            Toasty.info(this, "Tekan sekali lagi untuk keluar!", Toast.LENGTH_SHORT, true).show();
        }
        final long DELAY_TIME = 2000L;
        new Thread(new Runnable() {
            public void run(){
                try {
                    Thread.sleep(DELAY_TIME);
                    counter = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
