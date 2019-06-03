package com.github.ulez.calltest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean havePermissons = true;
    private String TAG = "MainActivity";
    private int permission;
    private String[] PERMISSIONS = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("lcy", "endCall");
            CallUtil.endCall(MainActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(PERMISSIONS, 4);
                havePermissons = false;
            }
        }
        if (havePermissons) onSuccessPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Ts("授权成功");
            onSuccessPermission();
        } else {
            Ts("授权失败");
        }
    }

    protected void Ts(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void onSuccessPermission() {
        CallUtil.makeCall(this, "152764563450");
        handler.sendEmptyMessageDelayed(1, 10000);
    }
}
