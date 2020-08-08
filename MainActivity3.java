package com.tianshaokai.video;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.videolan.vlc.media.MediaUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Action1;

import static com.tianshaokai.video.util.Constants.BASE_URL;
import static com.tianshaokai.video.util.Constants.PASSWORD_EXTRA;
import static com.tianshaokai.video.util.Constants.USERNAME_EXTRA;

public class MainActivity3 extends AppCompatActivity {

    //定义两个List，用来存放控件中Group/Child中的String
    private List<String> groupArray;
    private List<List<VideoLive>> childArray;

    private DrawerLayout drawerLayout;
    private ListView leftDrawer;
    private ArrayAdapter<String> adapter;
    private final String TAG = "MainActivity3";

    private String username = "", password = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawer = (ListView) findViewById(R.id.drawer_left);


        final Intent intent = getIntent();
        username = intent.getStringExtra(USERNAME_EXTRA);
        password = intent.getStringExtra(PASSWORD_EXTRA);


        leftDrawer.setBackgroundColor(Color.WHITE);

        // 设置选择模式为单条选中
        leftDrawer.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // 通过代码：根据重力方向打开指定抽屉
        drawerLayout.openDrawer(Gravity.LEFT);
        // 设置抽屉空余处颜色
        drawerLayout.setScrimColor(Color.GRAY);

        //对这两个List进行初始化，并插入一些数据
        groupArray = new ArrayList<>();
        childArray = new ArrayList<>();

        //getVideoJson();

        openStream();

    }

    private void openStream(){
        final String videoName = "MyPlaylist";
        final String url = Uri.parse(BASE_URL).buildUpon()
                .appendPath("get.php")
                .appendQueryParameter("username", username)
                .appendQueryParameter("password", password)
                .appendQueryParameter("type", "m3u").toString();


        Log.d(TAG, "Url " + url);

        MediaUtils.openStream(this, url, videoName);
    }

    private void getVideoJson() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://satdoc.dyndns.info/")
                .client(AFHttpClient.getHttpClient())
                .build();

        VideoService loginService = retrofit.create(VideoService.class);
        Call<ResponseBody> call = loginService.getJson();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonString = null;
                try {
                    jsonString = response.body().string();

                    Log.i(TAG, "" + jsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Type listType = new TypeToken<ArrayList<VideoInfo>>() {
                }.getType();
                ArrayList<VideoInfo> videoInfoList = new Gson().fromJson(jsonString, listType);

                //Log.d("获取的视频个数: ", "" + videoInfoList.size());
                Log.d(TAG, "" + videoInfoList.size());

                setData(videoInfoList);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("wxl", "onFailure=" + t.getMessage());
            }
        });
    }




    private void setData(ArrayList<VideoInfo> videoInfoList) {
        Observable.from(videoInfoList).subscribe(new Action1<VideoInfo>() {
            @Override
            public void call(VideoInfo videoInfo) {
                groupArray.add(videoInfo.getName());

                childArray.add(videoInfo.getSamples());
            }
        });
        initView();
    }

    private void initView() {
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, groupArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // 设置背填充内容背景色
                TextView tView = (TextView) super.getView(position,
                        convertView, parent);
                tView.setTextColor(Color.BLACK);
                return super.getView(position, convertView, parent);
            }
        };
        leftDrawer.setAdapter(adapter);
        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                leftDrawer.setItemChecked(pos, true);
                VideoFragment videoFragment = VideoFragment.getInstance(childArray.get(pos));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_view, videoFragment).commit();
                // 关闭所有打开的抽屉
                drawerLayout.closeDrawers();
            }
        });

    }
}
