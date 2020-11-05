package com.example.ariademo;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Aria.download(this).register();
    }

    public void singleDownload(View view) {
        Toast.makeText(this, "single", Toast.LENGTH_LONG).show();
        PermissionsUtils.getInstance().checkPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsUtils.IPermissionsResult() {
            @Override
            public void passPermissions() {
                String path = SDUtils.getSDCardCacheDir(MainActivity.this) + "/demos/file/demo.apk";
                FileUtils.CreateFile(path);
                long taskId = Aria.download(this)
                        .load(Url.URL1)     //读取下载地址
                        .setFilePath(path) //设置文件保存的完整路径
                        .create();   //创建并启动下载
            }

            @Override
            public void forbidPermissions() {

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void multiDownload(View view) {
        Toast.makeText(this, "multiple", Toast.LENGTH_LONG).show();
    }

    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        if (task.getKey().equals(Url.URL1)) {
            Log.e(TAG, "Percent===========" + task.getPercent());
        }
        int p = task.getPercent();    //任务进度百分比
        String speed = task.getConvertSpeed();    //转换单位后的下载速度，单位转换需要在配置文件中打开
        long speed1 = task.getSpeed(); //原始byte长度速度
    }

    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        //在这里处理任务完成的状态
        if (task.getKey().equals(Url.URL1)) {
            Log.e(TAG, "Over===========" + task.getPercent());
        }
    }
}