package com.tianshaokai.video;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tianshaokai.video.util.PrefUtils;

import static com.tianshaokai.video.util.Constants.PASSWORD_EXTRA;
import static com.tianshaokai.video.util.Constants.PLAYLIST_EXTRA;
import static com.tianshaokai.video.util.Constants.USERNAME_EXTRA;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        if(PrefUtils.isFirstTimeLogin(this)){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);

        }else {
            Intent mainActivityIntent = new Intent(this, CategoryActivity.class);
            mainActivityIntent.putExtra(USERNAME_EXTRA,PrefUtils.getUsername(this));
            mainActivityIntent.putExtra(PASSWORD_EXTRA,PrefUtils.getPassword(this));
            startActivity(mainActivityIntent);
        }
    }
}
