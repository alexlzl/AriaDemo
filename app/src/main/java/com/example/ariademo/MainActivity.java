package com.example.ariademo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private TextView tv1, tv2, tv3, tv4;
    private Button stopSingleDownloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.task1);
        tv2 = findViewById(R.id.task2);
        tv3 = findViewById(R.id.task3);
        tv4 = findViewById(R.id.task4);
        stopSingleDownloadBtn = findViewById(R.id.stopSingleDownloadBtn);
        Aria.download(this).register();
        Aria.get(this).getDownloadConfig().setMaxTaskNum(3);
    }

    private long apkTaskId;

    public void singleDownload(View view) {
        stopSingleDownloadBtn.setTag("start");
        Toast.makeText(this, "下载apk", Toast.LENGTH_LONG).show();
        PermissionsUtils.getInstance().checkPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsUtils.IPermissionsResult() {
            @Override
            public void passPermissions() {
                String fileName = SDUtils.getSDCardCacheDir(MainActivity.this) + "/demos/file/apk/test.apk";
                String folderName = SDUtils.getSDCardCacheDir(MainActivity.this) + "/demos/file/apk";
                FileUtils.createDir(folderName);
                apkTaskId = Aria.download(this)
                        .load(Url.URL1)     //读取下载地址
                        .setFilePath(fileName) //设置文件保存的完整路径
                        .create();   //创建并启动下载
            }

            @Override
            public void forbidPermissions() {

            }
        });


    }

    private long videoTaskId;

    public void singleDownloadVideo(View view) {
        Toast.makeText(this, "下载视频", Toast.LENGTH_LONG).show();
        PermissionsUtils.getInstance().checkPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsUtils.IPermissionsResult() {
            @Override
            public void passPermissions() {
                String fileName = SDUtils.getSDCardCacheDir(MainActivity.this) + "/demos/file/video/test.mp4";
                String folderName = SDUtils.getSDCardCacheDir(MainActivity.this) + "/demos/file/video";
                FileUtils.createDir(folderName);
                videoTaskId = Aria.download(this)
                        .load(Url.URL2)     //读取下载地址
                        .setFilePath(fileName) //设置文件保存的完整路径
                        .create();   //创建并启动下载
            }

            @Override
            public void forbidPermissions() {

            }
        });
    }

    private long picTaskId;

    public void singleDownloadPic(View view) {
        Toast.makeText(this, "下载图片", Toast.LENGTH_LONG).show();
        PermissionsUtils.getInstance().checkPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsUtils.IPermissionsResult() {
            @Override
            public void passPermissions() {
                String fileName = SDUtils.getSDCardCacheDir(MainActivity.this) + "/demos/file/pic/gome.jpg";
                String folderName = SDUtils.getSDCardCacheDir(MainActivity.this) + "/demos/file/pic";
                FileUtils.createDir(folderName);
                picTaskId = Aria.download(this)
                        .load(Url.URL3)     //读取下载地址
                        .setFilePath(fileName) //设置文件保存的完整路径
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
        startActivity(new Intent(this,MainActivity2.class));
    }

    /**
     * @ describe 预处理的注解，在任务为开始前回调（一般在此处预处理UI界面）
     * @author lzl
     * @ time 2020/11/5 20:26
     * @ param
     * @ return
     */

    @Download.onPre
    protected void downloadPre(DownloadTask task) {
        Log.e(TAG, "Pre===========" + task.getKey());
    }

    /**
     * @ describe 任务开始时的注解，新任务开始时进行回调
     * @author lzl
     * @ time 2020/11/5 20:27
     * @ param
     * @ return
     */
    @Download.onTaskStart
    protected void downloadStart(DownloadTask task) {
        Log.e(TAG, "Start===========" + task.getKey());
    }

    /**
     * @ describe 任务恢复时的注解，任务从停止恢复到运行前进行回调
     * @author lzl
     * @ time 2020/11/5 20:28
     * @ param
     * @ return
     */
    @Download.onTaskResume
    protected void downloadResume(DownloadTask task) {
        Log.e(TAG, "Resume===========" + task.getKey());
    }


    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        Log.e(TAG, "Percent===========" + task.getPercent() + "======" + task.getKey());
        if (Url.URL1.equals(task.getKey())) {
            //任务1
            tv1.setText(String.format("%s%%", task.getPercent()));
        }
        if (Url.URL2.equals(task.getKey())) {
            //任务2
            tv2.setText(String.format("%s%%", task.getPercent()));
        }
        if (Url.URL3.equals(task.getKey())) {
            //任务3
            tv3.setText(String.format("%s%%", task.getPercent()));
        }

        int p = task.getPercent();    //任务进度百分比
        String speed = task.getConvertSpeed();    //转换单位后的下载速度，单位转换需要在配置文件中打开
        long speed1 = task.getSpeed(); //原始byte长度速度
    }

    /**
     * @ describe 队列已经满了，继续创建新任务，将会回调该方法
     * @author lzl
     * @ time 2020/11/5 20:31
     * @ param
     * @ return
     */
    @Download.onWait
    protected void downloadWait(DownloadTask task) {
        Log.e(TAG, "Wait===========" + task.getKey());
    }

    /**
     * @ describe 任务停止时的注解，任务停止时进行回调
     * @author lzl
     * @ time 2020/11/5 20:34
     * @ param
     * @ return
     */
    @Download.onTaskStop
    protected void downloadStop(DownloadTask task) {
        Log.e(TAG, "Stop===========" + task.getKey());
        if (Url.URL1.equals(task.getKey())) {
            stopSingleDownloadBtn.setText("重新下载APK");
            stopSingleDownloadBtn.setTag("stop");
        }
    }

    /**
     * @ describe 任务被删除时的注解，任务被删除时进行回调
     * @author lzl
     * @ time 2020/11/5 20:36
     * @ param
     * @ return
     */
    @Download.onTaskCancel
    protected void downloadCancel(DownloadTask task) {
        Log.e(TAG, "Cancel===========" + task.getKey());
    }

    /**
     * @ describe 任务失败时的注解，任务执行失败时进行回调
     * @author lzl
     * @ time 2020/11/5 20:36
     * @ param
     * @ return
     */
    @Download.onTaskFail
    protected void downloadTaskFail(DownloadTask task) {
        Log.e(TAG, "Fail===========" + task.getKey());
    }

    /**
     * @ describe 任务完成时的注解，任务完成时进行回调
     * @author lzl
     * @ time 2020/11/5 20:37
     * @ param
     * @ return
     */
    @Download.onTaskComplete
    protected void taskComplete(DownloadTask task) {
        //在这里处理任务完成的状态
        Log.e(TAG, "Over===========" + task.getPercent() + "======" + task.getKey());
        if (Url.URL1.equals(task.getKey())) {
            //任务1
            tv1.setText(String.format("%s%%", task.getPercent()));
        }
        if (Url.URL2.equals(task.getKey())) {
            //任务2
            tv2.setText(String.format("%s%%", task.getPercent()));
        }
        if (Url.URL3.equals(task.getKey())) {
            //任务3
            tv3.setText(String.format("%s%%", task.getPercent()));
        }

    }

    public void stopSingleDownload(View view) {
        Toast.makeText(this, "停止下载apk", Toast.LENGTH_LONG).show();
        if ("stop".equals(view.getTag())) {
            //如果之前是停止状态再次下载
            stopSingleDownloadBtn.setText("停止单任务程下载APK");
            stopSingleDownloadBtn.setTag("start");
            Aria.download(this)
                    .load(apkTaskId)
                    .resume();
        } else if("start".equals(view.getTag())){
            //默认停止下载
            Aria.download(this)
                    .load(apkTaskId)
                    .stop();
        }


    }

}