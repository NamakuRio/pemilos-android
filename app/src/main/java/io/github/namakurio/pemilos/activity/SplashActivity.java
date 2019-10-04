package io.github.namakurio.pemilos.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

import io.github.namakurio.pemilos.R;
import io.github.namakurio.pemilos.data.SessionManager;
import io.github.namakurio.pemilos.data.SharedPref;
import io.github.namakurio.pemilos.utils.PermissionUtil;
import io.github.namakurio.pemilos.utils.Tools;

public class SplashActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private SharedPref sharedPref;
    private boolean on_permission_result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initToolbar();
        // show the logo
        final ImageView logo = (ImageView) findViewById(R.id.logo);
        Glide.with(this).load(R.drawable.skanida).crossFade().into(logo);

        sessionManager = new SessionManager(this);
        sharedPref = new SharedPref(this);
    }

    private void initToolbar() {
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // permission checker for android M or higher
        if (Tools.needRequestPermission() && !on_permission_result) {
            String[] permission = PermissionUtil.getDeniedPermission(this);
            if (permission.length != 0) {
                requestPermissions(permission, 200);
            } else {
                startProcess();
            }
        } else {
            startProcess();
        }
    }

    private void startProcess() {
        // Check session user
        if(sessionManager.checkLogin()) {
            startMain(MainActivity.class);
        } else {
            startMain(LoginActivity.class);
        }
    }

    private void startMain(final Class too) {
        // Show splash screen for 2 seconds
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, too);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish(); // kill current activity
            }
        };
        new Timer().schedule(task, 3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            for (String perm : permissions) {
                boolean rationale = shouldShowRequestPermissionRationale(perm);
                sharedPref.setNeverAskAgain(perm, !rationale);
            }
            on_permission_result = true;
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
