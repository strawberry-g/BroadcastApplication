package com.example.broadcastapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private EditText editText;
    private Button reset;

    Context myContext;
    private MyBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myContext = getApplicationContext();
        initView();
        receiver = new MyBroadcastReceiver();
    }

    public void initView(){
        textView = findViewById(R.id.tv);
        editText = findViewById(R.id.edit);
        //radioGroup = findViewById(R.id.radio_group);
        reset = findViewById(R.id.btn_reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("广播输出结果显示区");
                editText.setText("");
                editText.setTextColor(getResources().getColor(android.R.color.black));
            }
        });

        //应用程序可以通过广播来配置系统扫描头使能数据输出模式
        //广播名:ACTION_BAR_SCANCFG
        Intent intent = new Intent("ACTION_BAR_SCANCFG");
        //1:直接填充模式、2:虚拟按键模式、3:广播输出模式
        intent.putExtra("EXTRA_SCAN_MODE",3);
        //发送广播
        myContext.sendBroadcast(intent);

        /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Intent intent = new Intent("ACTION_BAR_SCANCFG");
                switch (i){
                    case R.id.radio_broadcast:
                        intent.putExtra("EXTRA_SCAN_MODE",3);
                        break;
                    case R.id.radio_emulate_key:
                        intent.putExtra("EXTRA_SCAN_MODE",2);
                        break;
                    case R.id.radio_fill:
                        intent.putExtra("EXTRA_SCAN_MODE",1);
                        break;
                }
                myContext.sendBroadcast(intent);
            }
        });*/
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //扫描结果1参数
            final String scanResult1 = intent.getStringExtra("SCAN_BARCODE1");
            //扫描结果2参数
            final String scanResult2 = intent.getStringExtra("SCAN_BARCODE2");
            //-1:未知类型,码值类型
            final int barcodeType = intent.getIntExtra("SCAN_BARCODE_TYPE",-1);
            //扫码状态(ok、fail)
            final String scanStatus = intent.getStringExtra("SCAN_STATE");

            if ("ok".equals(scanStatus)){
                //成功
                editText.setText(scanResult1);
            }else{
                //失败fail
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        //添加想监听的广播名
        intentFilter.addAction("nlscan.action.SCANNER_RESULT");
        receiver = new MyBroadcastReceiver();
        //注册广播
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}
