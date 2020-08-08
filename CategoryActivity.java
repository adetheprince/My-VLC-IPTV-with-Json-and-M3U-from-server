package com.tianshaokai.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tianshaokai.video.adapter.CategorySelectionListAdapter;
import com.tianshaokai.video.util.NetworkConnectionStatus;
import com.tianshaokai.video.util.PrefUtils;

import org.videolan.libvlc.util.PreferenceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.tianshaokai.video.util.Constants.BASE_URL;
import static com.tianshaokai.video.util.Constants.CATEGORY_EXTRA;


/**
 * A simple activity that allows the user to select a
 * video to play
 */
public class CategoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView categoryList;
    private String username = "", password = "", playlistJSON = "";
    private final String TAG = "VideoSelectionActivity";
    private List<Video> videoList = new ArrayList<>();
    private List<String> allCategories = new ArrayList<>();
    private ProgressBar progressBar;
    private Map<String, List<Video>> categoryMap = new HashMap<>();
    private View previousItemView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }


        mContext=this;


   //     final Intent intent = getIntent();
//        username = intent.getStringExtra(USERNAME_EXTRA);
//        password = intent.getStringExtra(PASSWORD_EXTRA);

        username = PrefUtils.getUsername(this);
        password = PrefUtils.getPassword(this);


        categoryList = (ListView) findViewById(R.id.categoryList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);



        categoryList.setOnItemClickListener(this);


        getPlaylistFile();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final int viewId = parent.getId();
        String categoryName = "";


        switch (viewId) {
            case R.id.categoryList:

                categoryName = allCategories.get(position);
                videoList = categoryMap.get(categoryName);

                PreferenceUtils.setCategoryName(mContext,categoryName);

                MyApplication myApplication= (MyApplication)getApplicationContext();
                myApplication.setVideoList(videoList);

                Intent videoSelectionIntent = new Intent(CategoryActivity.this, VideoSelectionActivity.class);
                videoSelectionIntent.putExtra(CATEGORY_EXTRA,categoryName);
                startActivity(videoSelectionIntent);


                break;

        }


    }


    /**
     * Get m3u playlist file
     */
    private void getPlaylistFile() {
        //http://chilani.selfip.com:8000/get.php?username=test&password=1234&type=m3u

        if (NetworkConnectionStatus.isConnnected(this)) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(AFHttpClient.getHttpClient())
                    .build();

            VideoService videoService = retrofit.create(VideoService.class);
            Call<ResponseBody> call = videoService.getVLCPlaylist(username, password, "m3u");

            progressBar.setVisibility(View.VISIBLE);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String jsonString = null;
                    try {
                        jsonString = response.body().string();

                        Log.i(TAG, "" + jsonString);

                        //parsePlaylist(jsonString);
                        loadCategories(jsonString);
                        progressBar.setVisibility(View.GONE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("wxl", "onFailure=" + t.getMessage());
                }
            });

        } else {
            Toast.makeText(this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
        }
    }



    private void loadCategories(String output) {
        PreferenceUtils.setM3UFile(this,output);
        String videoName = "", videoUri = "";
        Video videoItem;

        String regularExpression = "\n";
        String category = "";

        String[] links = output.split(regularExpression);
        int playListLength = links.length - 1;

        List<Video> videos;
        int lineCount = 1;

        while (lineCount < playListLength) {


            if (links[lineCount].contains("///")) {
                category = links[lineCount].replace("#EXTINF:-1,","").replace(" ///","");
                allCategories.add(category);



              ++lineCount;


            } else {

                videoName = links[lineCount];
                videoUri = links[lineCount+1];


                videoItem = new Video();
                if(links[lineCount].contains("http")) {
                    videoItem.setUri(videoUri);

                }

                if ((!videoName.contains("///"))&&(!videoName.contains("http"))){
                    videoItem.setName(videoName.replace("#EXTINF:-1,",""));
                }


                if (categoryMap.containsKey(category)) {
                    videos = categoryMap.get(category);

                   // if(!TextUtils.isEmpty(videoItem.getName())&&!TextUtils.isEmpty(videoItem.getUri())){
                        videos.add(videoItem);
                    //}



                } else {


                    //if(!TextUtils.isEmpty(videoItem.getName())&&!TextUtils.isEmpty(videoItem.getUri())){
                        videos = new ArrayList<>();
                        videos.add(videoItem);

                        categoryMap.put(category, videos);
                    //}

                }


            }

            ++lineCount;

        }
        // exampleList.setAdapter(new VideoSelectionListAdapter(this,videoList));
        categoryList.setAdapter(new CategorySelectionListAdapter(this, allCategories));
    }


    private void loadCategoriesOnly(String output) {
        PreferenceUtils.setM3UFile(this,output);

        String regularExpression = "\n";
        String category = "";

        String[] links = output.split(regularExpression);
        int playListLength = links.length;

        int lineCount = 0;

        while (lineCount < playListLength) {


            if (links[lineCount].contains("///")) {
                category = links[lineCount].replace("#EXTINF:-1,","").replace(" ///","");
                allCategories.add(category);



            }

            ++lineCount;

        }
        // exampleList.setAdapter(new VideoSelectionListAdapter(this,videoList));
        categoryList.setAdapter(new CategorySelectionListAdapter(this, allCategories));
    }



    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        return super.dispatchKeyEvent(event);
    }


}