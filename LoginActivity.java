package com.tianshaokai.video;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tianshaokai.video.R;
import com.tianshaokai.video.util.Constants;
import com.tianshaokai.video.util.NetworkConnectionStatus;
import com.tianshaokai.video.util.PrefUtils;

import org.videolan.libvlc.util.PreferenceUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.tianshaokai.video.util.Constants.BASE_URL;
import static com.tianshaokai.video.util.Constants.PASSWORD_EXTRA;
import static com.tianshaokai.video.util.Constants.PLAYLIST_EXTRA;
import static com.tianshaokai.video.util.Constants.USERNAME_EXTRA;

/**
 * A simple activity for authenticating the user.
 */
public class LoginActivity extends AppCompatActivity {
    private Context mContext;
    private ProgressDialog progressDialog;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = LoginActivity.this;

        usernameEditText=(EditText)findViewById(R.id.usernameEditText);
        passwordEditText=(EditText)findViewById(R.id.passwordEditText);

        Button loginButton=(Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameEditText.getText().toString();
                String password=passwordEditText.getText().toString();

                if(TextUtils.isEmpty(username)){
                    Toast.makeText(mContext,"Username is required",Toast.LENGTH_LONG).show();

                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(mContext,"Password is required",Toast.LENGTH_LONG).show();

                }else {
                    login(username,password);
                }

            }
        });
    }

    private void login(final String username, final String password) {
        //Http://chilani.selfip.com:8000/get.php?username=test&password=1234&type=m3u

        if(NetworkConnectionStatus.isConnnected(this)) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.authenticating));
            progressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(AFHttpClient.getHttpClient())
                    .build();

            VideoService videoService = retrofit.create(VideoService.class);
            Call<ResponseBody> call = videoService.getVLCPlaylist(username, password, "m3u");



            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String jsonString = null;
                    try {
                        jsonString = response.body().string();

                        progressDialog.dismiss();

                        if(TextUtils.isEmpty(jsonString)){
                            Toast.makeText(mContext, R.string.invalid_credentials, Toast.LENGTH_LONG).show();

                            usernameEditText.setText("");
                            passwordEditText.setText("");

                        }else{

                            PrefUtils.setFirstTimeLogin(mContext, false);
                            PrefUtils.setUsername(mContext, username);
                            PrefUtils.setPassword(mContext,password);

                            PreferenceUtils.setM3UFile(mContext, jsonString);


                            Intent categoryIntent = new Intent(mContext, CategoryActivity.class);
//                            categoryIntent.putExtra(USERNAME_EXTRA,username);
//                            categoryIntent.putExtra(PASSWORD_EXTRA,password);
//                            categoryIntent.putExtra(PLAYLIST_EXTRA, jsonString);
                            startActivity(categoryIntent);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("wxl", "onFailure=" + t.getMessage());
                }
            });

        }else{
            Toast.makeText(this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
        }
    }


}
