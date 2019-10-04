package io.github.namakurio.pemilos.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chaos.view.PinView;

import java.util.ArrayList;
import java.util.List;

import io.github.namakurio.pemilos.R;
import io.github.namakurio.pemilos.connection.API;
import io.github.namakurio.pemilos.connection.RestAdapter;
import io.github.namakurio.pemilos.connection.callbacks.CallbackLogin;
import io.github.namakurio.pemilos.data.Constant;
import io.github.namakurio.pemilos.data.SessionManager;
import io.github.namakurio.pemilos.model.User;
import io.github.namakurio.pemilos.utils.CallbackDialog;
import io.github.namakurio.pemilos.utils.DialogUtils;
import io.github.namakurio.pemilos.utils.NetworkCheck;
import io.github.namakurio.pemilos.utils.ToastUtils;
import io.github.namakurio.pemilos.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ImageView imgLogin;
    private PinView codeLogin;
    private AppCompatButton btnLogin;
    private Dialog dialog;
    private SessionManager sessionManager;
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar();

        imgLogin = (ImageView) findViewById(R.id.imgLogin);
        codeLogin = (PinView) findViewById(R.id.codeLogin);
        btnLogin = (AppCompatButton) findViewById(R.id.btnLogin);

        sessionManager = new SessionManager(getApplicationContext());

        Glide.with(this).load(R.drawable.img_code_verification).crossFade().into(imgLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeLogin.getText().toString().length() < 4) {
                    ToastUtils.customToast(getLayoutInflater(), getApplicationContext(), LoginActivity.this, Color.WHITE, "Mohon Lengkapi Code Anda", R.color.yellow_primary_dark);
                } else {
                    checkConnection();
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
    }

    private void checkConnection() {
        if(!NetworkCheck.isConnect(getApplicationContext())){
            dialogNoInternet();
        } else {
            requestLogin();
        }

    }

    private void requestLogin(){
        showLoading();
        API api = RestAdapter.createAPI();
        Call<CallbackLogin> callbackCall = api.login(codeLogin.getText().toString(), Constant.PASSWORD_DEFAULT);
        callbackCall.enqueue(new Callback<CallbackLogin>() {
            @Override
            public void onResponse(Call<CallbackLogin> call, Response<CallbackLogin> response) {
                dialog.dismiss();

                String status = response.body() != null ? response.body().getStatus() : null;
                String msg = response.body() != null ? response.body().getMsg() : null;

                if(status.equals("success")) {
                    users = response.body() != null ? response.body().getData() : null;

                    User user = users.get(0);

                    sessionManager.createSession(
                            user.getId(),
                            user.getUsername(),
                            user.getName(),
                            user.getEmail(),
                            user.getApi_token()
                    );

                    ToastUtils.customToast(getLayoutInflater(), getApplicationContext(), LoginActivity.this, Color.WHITE, "Berhasil Masuk", R.color.google_green);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish(); // kill current activity
                } else {
                    ToastUtils.customToast(getLayoutInflater(), getApplicationContext(), LoginActivity.this, Color.WHITE, msg, R.color.google_red);
                }
            }

            @Override
            public void onFailure(Call<CallbackLogin> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                dialog.dismiss();
                dialogServerNotConnect();
            }
        });
    }

    private void showLoading() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void dialogServerNotConnect() {
        Dialog dialog = new DialogUtils(this).buildDialogWarning("Tidak dapat menghubungkan ke server", "Server sedang sibuk", "Ulangi", "Tutup", R.drawable.img_no_connect, new CallbackDialog() {
            @Override
            public void onPositiveClick(Dialog dialog) {
                dialog.dismiss();
                checkConnection();
            }

            @Override
            public void onNegativeClick(Dialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void dialogNoInternet() {
        Dialog dialog = new DialogUtils(this).buildDialogWarning("Tidak ada koneksi internet", "Mohon cek koneksi internet Anda", "Ulangi", "Tutup", R.drawable.img_no_internet, new CallbackDialog() {
            @Override
            public void onPositiveClick(Dialog dialog) {
                dialog.dismiss();
                checkConnection();
            }

            @Override
            public void onNegativeClick(Dialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
