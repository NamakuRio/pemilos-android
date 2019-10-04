package io.github.namakurio.pemilos.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import io.github.namakurio.pemilos.R;
import io.github.namakurio.pemilos.data.SessionManager;
import io.github.namakurio.pemilos.fragment.CandidateFragment;
import io.github.namakurio.pemilos.fragment.HomeFragment;
import io.github.namakurio.pemilos.fragment.ProfileFragment;
import io.github.namakurio.pemilos.fragment.VoiceFragment;
import io.github.namakurio.pemilos.utils.ToastUtils;
import io.github.namakurio.pemilos.utils.Tools;

public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private NavigationView nav_view;
    private SessionManager sessionManager;
    private Dialog dialog;

    static MainActivity mainActivity;

    public static MainActivity getInstance() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        initToolbar();
        initDrawerMenu();
        initFragment();

        sessionManager = new SessionManager(getApplicationContext());
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.app_name);
//        Tools.setSystemBarColor(this, android.R.color.white);
//        Tools.setSystemBarLight(this);
    }

    private void initDrawerMenu() {
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                onItemSelected(item.getItemId());
                //drawer.closeDrawers();
                return true;
            }
        });
        nav_view.setItemIconTintList(getResources().getColorStateList(R.color.nav_state_list));
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // init fragment voice
        VoiceFragment voiceFragment = new VoiceFragment();
        fragmentTransaction.replace(R.id.frame_content_voice, voiceFragment);
        // init fragment candidate
        CandidateFragment candidateFragment = new CandidateFragment();
        fragmentTransaction.replace(R.id.frame_content_candidate, candidateFragment);

        fragmentTransaction.commit();
    }

    public boolean onItemSelected(int id) {
        Intent i;
        switch (id) {
            //sub menu
            case R.id.nav_home:
//                i = new Intent(this, ActivityShoppingCart.class);
//                startActivity(i);
                break;
            case R.id.nav_profile:
                i = new Intent(this, ProfileActivity.class);
                startActivity(i);
                break;
            case R.id.nav_about:
                Tools.showDialogAbout(this);
                break;
            case R.id.nav_logout:
                try{
                    ToastUtils.customToast(getLayoutInflater(), getApplicationContext(), MainActivity.this, Color.WHITE, "Berhasil Keluar", R.color.app_green);
                    sessionManager.logout();
                    finish();
                }catch (Exception e){
                    Log.e("e", "error" + e);
                    dialog.dismiss();
                }
                break;
            default: break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            doExitApp();
        }
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "Klik 2x untuk keluar", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}
