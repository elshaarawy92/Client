package com.example.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    static final int MSG_SAY_HELLO = 1;
    Messenger messenger = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                messenger = new Messenger(iBinder);
                Log.d(TAG, "onServiceConnected: ");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d(TAG, "onServiceDisconnected: ");
            }
        };
        Intent intent = new Intent();
        intent.setAction("com.elliang.messenger");
        intent.setPackage("com.example.servicelearn");
       boolean isBindSuccess =  bindService(intent, conn, BIND_AUTO_CREATE);
        Log.d(TAG, "bindService: :"+isBindSuccess);
        Button button = findViewById(R.id.send_message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sayHello();
                unbindService(conn);
            }
        });
    }

    public void sayHello() {
        Message message = Message.obtain(null, MSG_SAY_HELLO, 0, 0);
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
