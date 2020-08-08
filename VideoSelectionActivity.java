package com.tianshaokai.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tianshaokai.video.adapter.CategorySelectionListAdapter;
import com.tianshaokai.video.adapter.VideoSelectionListAdapter;
import com.tianshaokai.video.util.NetworkConnectionStatus;

import org.videolan.libvlc.util.PreferenceUtils;
import org.videolan.vlc.media.MediaUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.tianshaokai.video.util.Constants.BASE_URL;
import static com.tianshaokai.video.util.Constants.CATEGORY_EXTRA;
import static com.tianshaokai.video.util.Constants.PASSWORD_EXTRA;
import static com.tianshaokai.video.util.Constants.USERNAME_EXTRA;


/**
 * A simple activity that allows the user to select a
 * video to play
 */
public class VideoSelectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView exampleList;
    private final String TAG = "VideoSelectionActivity";
    private View previousItemView;
    private List<Video> selectedVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_selection);




        MyApplication myApplication= (MyApplication)getApplicationContext();
        selectedVideos = myApplication.getVideoList();

        final Intent intent = getIntent();
        String categoryName = intent.getStringExtra(CATEGORY_EXTRA);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }



        TextView categoryTitle=(TextView)findViewById(R.id.category_title);
        categoryTitle.setText(categoryName);

        exampleList = (ListView) findViewById(R.id.selection_activity_list);
        exampleList.setOnItemClickListener(this);
        exampleList.setAdapter(new VideoSelectionListAdapter(this, selectedVideos));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final int viewId = parent.getId();

        switch (viewId) {
            case R.id.selection_activity_list:
                startVideoPlayerActivity(position);
                break;
        }


    }




    private void startVideoPlayerActivity(int selectedIndex) {
        Video video = selectedVideos.get(selectedIndex);

        final String uri = video.getUri().trim();
        final String videoName = video.getName();


        PreferenceUtils.setPlaylistName(this,videoName);
        MediaUtils.openStream(this, uri, videoName);


    }




    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        /*
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            layoutCategory.setVisibility(View.VISIBLE);
            layoutPlaylist.setVisibility(View.GONE);

        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {

        }*/


        return super.dispatchKeyEvent(event);
    }



}