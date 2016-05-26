package com.loveplusplus.update.sample;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loveplusplus.update.UpdateChecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainActivity extends ActionBarActivity implements MyResultReceiver.Receiver
{
    public static boolean is_hotpatching=false;
    public static boolean is_ADblocker=false;
    public static String result;
    Process localProcess = null;
    private static Context mContext ;
    OutputStream localOutputStream = null;
    public static final String KEY="com.example.max_fzf.root_test";
    protected static final String APP_UPDATE_SERVER_URL = "http://140.112.29.194:3333/update_check";
    public static MyResultReceiver mReceiver;
    private int local_version;
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.getAppManager().addActivity(this);
        mContext=this.getApplicationContext();
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        //读取当前patch的版本号：
        File verson_json=new File("sdcard/Android/data/com.loveplusplus.update.sample/cache/Patching_version.json");
        if(verson_json.exists())
        {
            try {
                InputStream in = new FileInputStream("sdcard/Android/data/com.loveplusplus.update.sample/cache/Patching_version.json");
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStreamReader.close();
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                local_version = jsonObject.getInt("versioncode");
                Log.d("读出的版本号：", String.valueOf(jsonObject.getInt("versioncode")));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            local_version=0;
        }
        if(UpdateChecker.isNetworkAvailable(mContext)) {
            Log.d("注意！","网络连接正常！");
            UpdateChecker.checkForDialog(MainActivity.this, APP_UPDATE_SERVER_URL, MainActivity.this, mReceiver,local_version);
        }
        else{
            Log.d("注意！","网络连接错误！");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("ADF Message");
            builder.setMessage("沒有網絡連接！！");
            builder.setNeutralButton("好的！",null);
            builder.show();
        }
        File version_json=new File("sdcard/Android/data/com.loveplusplus.update.sample/cache/Patching_version.json");
        Button broken= (Button) findViewById(R.id.button);
        final TextView sfff= (TextView) findViewById(R.id.textView);
        broken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Error_test();
            sfff.setText("Successful!!!!");
            }
        });
        Button clean= (Button) findViewById(R.id.button2);
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                localProcess = Runtime.getRuntime().exec("su");
                localOutputStream = localProcess.getOutputStream();
                DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                localDataOutputStream.writeBytes("rm -r /system/df_file\n");
                    localDataOutputStream.writeBytes("rm -r /system/dynamic_framework/hook.apk\n");
                    localDataOutputStream.writeBytes("rm -r /sdcard/Android/data/com.loveplusplus.update.sample/cache/Patching_version.json\n");
                    Toast.makeText(getApplicationContext(),"Reset successful!",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Button reboot= (Button) findViewById(R.id.button3);
        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                localProcess = Runtime.getRuntime().exec("su");
                localOutputStream = localProcess.getOutputStream();
                DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                    localDataOutputStream.writeBytes("reboot\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void Error_test(){
        int a;
        a=6/0;
    }
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.d("注意","onReceiveResult!!!!!!!!!!!!!!!!!!!!!");
        String filename="sdcard/Android/data/com.loveplusplus.update.sample/cache/hook.zip";
        String path="sdcard/Android/data/com.loveplusplus.update.sample/cache/";
        try {
            Unzip.unzip(filename, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("注意","来到了这一步！");
        try {
            localProcess = Runtime.getRuntime().exec("su");
            localOutputStream = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
            localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
            localDataOutputStream.writeBytes("rm -r /system/df_file\n");
            localDataOutputStream.writeBytes("rm -r /system/dynamic_framework/hook.apk\n");
            localDataOutputStream.writeBytes("rm -r sdcard/Android/data/com.loveplusplus.update.sample/cache/hook.zip\n");
            localDataOutputStream.writeBytes("mv sdcard/Android/data/com.loveplusplus.update.sample/cache/hook.apk /system/dynamic_framework/hook.apk\n");
            localDataOutputStream.writeBytes("mv sdcard/Android/data/com.loveplusplus.update.sample/cache/df_file /system/df_file\n");
            localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
            File df_file;
            File hook_apk;
            Log.d("注意！！","來到了判斷之前");
          do{
              df_file=new File("/system/df_file");
              hook_apk=new File("/system/dynamic_framework/hook.apk");
              Log.d("注意！！","不存在！");
          } while(df_file.exists()==false||hook_apk.exists()==false);
            df_file=new File("/system/df_file");
            hook_apk=new File("/system/dynamic_framework/hook.apk");
            if(df_file.exists()&&hook_apk.exists()){
                Log.d("注意！！","df_file 和 apk 都存在！");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("ADF Message");
                builder.setMessage("Hot_Patching Successful !!");
                builder.setPositiveButton("Exit",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // Intent exit=new Intent(MainActivity.this,ExitActivity.class);
                      //  startActivity(exit);
                      //  finish();
                       // int pid=android.os.Process.myPid();
                       // AppManager.getAppManager().AppExit(mContext);
                        int pid=android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                        finish();
                    }
                });
                builder.show();
            }
            else{
                Log.d("注意！！","df_file 和 apk 消失了！！");
            }

            if(df_file.exists()&&hook_apk.exists()){
                Toast.makeText(getApplicationContext(),"ADF",Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void handle(String packagename) throws IOException {



        Process localProcess = null;
        OutputStream localOutputStream = null;
        try {
            localProcess = Runtime.getRuntime().exec("su");
            localOutputStream = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
            localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
            //localDataOutputStream.writeBytes("rm -r /system/df_file\n");
            localDataOutputStream.writeBytes("echo \"" +packagename+"\" >> /system/df_file\n");
            localDataOutputStream.writeBytes("chmod 644 /system/df_file\n");
            localDataOutputStream.writeBytes("mount -o remount,ro /system\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void Delete_df_file() throws IOException {



        Process localProcess = null;
        OutputStream localOutputStream = null;
        try {
            localProcess = Runtime.getRuntime().exec("su");
            localOutputStream = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
            localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
            localDataOutputStream.writeBytes("rm -r /system/df_file\n");
            localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void hotpatching_rewrite(){
        if(is_hotpatching) {
            Log.d("注意！！！","Hotpatching 补丁被重新写入！");
            Process localProcess = null;
            OutputStream localOutputStream = null;
            try {
                localProcess = Runtime.getRuntime().exec("su");
                localOutputStream = localProcess.getOutputStream();
                DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                localDataOutputStream.writeBytes("echo \"" + result + "\" >> /system/df_file\n");
                localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d("注意！！！","Hotpatching 补丁没有被写入！！");
        }
    }
    public static void AD_blocker_rewrite(){
        Process localProcess = null;
        OutputStream localOutputStream = null;
        SharedPreferences spref = mContext.getSharedPreferences("MainActivity",MODE_PRIVATE);
        final  SharedPreferences.Editor editor = spref.edit();
        if(spref.getBoolean("KEY_Boolean",false))
        {
            Log.d("注意！！！","ADBlocker 补丁被重新写入！");
            try {
                localProcess = Runtime.getRuntime().exec("su");
                localOutputStream = localProcess.getOutputStream();
                DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                localDataOutputStream.writeBytes("echo \"Lcom/unity3d/ads/android/UnityAds;,Z,canShow,Lcom/example/max_fzf/hook/MainActivity;,Z,canShow\" >> /system/df_file\n");
                localDataOutputStream.writeBytes("echo \"Lcom/google/android/gms/ads/AdView;,VL,loadAd,Lcom/example/max_fzf/hook/MainActivity;,VL,deleteAd\" >> /system/df_file\n");
                localDataOutputStream.writeBytes("chmod 644 /system/df_file\n");
                localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
                Log.d("ADF!!", "turn on !success!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{Log.d("注意！！！","ADBlocker 没有补丁被写入！！");}
    }


}
