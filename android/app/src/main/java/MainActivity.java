package com.example.health;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@RequiresApi(api= Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
    BluetoothSocket bluetoothSocket=null;
    InputStream inputStream;
    int i,HeartRate,O2_saturation,Temperature;
    String BP,sdfstring;
    String deviceName="";
    @SuppressLint("NewApi")
    SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy-hh-mm--ss");


    public Button button1,button2;
    Random rand=new Random();
    FirebaseDatabase data;
    DatabaseReference refer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        try{
            BluetoothDevice hc05=bluetoothAdapter.getRemoteDevice("00:18:E4:00:43:6C");
            bluetoothSocket=hc05.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<BluetoothDevice> pairedDevices=bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size()>0){
            for(BluetoothDevice device : pairedDevices) {
                if (device.getAddress().equals("00:18:E4:00:43:6C")) {
                    deviceName += device.getName();
                    break;
                }
            }
        }
        Thread th=new Thread(){


            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(30000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    inputStream = bluetoothSocket.getInputStream();
                                    byte[] buffer = new byte[40];
                                    inputStream.read(buffer);
                                    for (i = 29; buffer[i] != 35 && buffer[i] != 64; i--) ;
                                    HeartRate = 0;
                                    O2_saturation = 0;
                                    if (buffer[i] == 64) {
                                        for (i--; buffer[i] != 35; i--)
                                            HeartRate = (HeartRate * 10) + (buffer[i] - 48);
                                        for (i--; buffer[i] != 64; i--)
                                            O2_saturation = (O2_saturation * 10) + (buffer[i] - 48);
                                    } else {
                                        for (i--; buffer[i] != 64; i--)
                                            HeartRate = (HeartRate * 10) + (buffer[i] - 48);
                                        for (i--; buffer[i] != 35; i--)
                                            O2_saturation = (O2_saturation * 10) + (buffer[i] - 48);


                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Temperature = 34;
                                BP = 121 + "/" + 80;
                                textView.setText(String.valueOf(HeartRate));
                                textView2.setText(String.valueOf(Temperature));
                                textView3.setText(String.valueOf(O2_saturation));
                                textView4.setText(String.valueOf(BP));
                                sdfstring = sdf.format(new Date());

                                data = FirebaseDatabase.getInstance();
                                refer = data.getReference("EmployeesHealth").child("Device Name").child(deviceName).child(sdfstring);
                                UsersData Data = new UsersData(HeartRate, O2_saturation, Temperature, BP);
                                refer.setValue(Data);
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        th.start();




        button1=(Button) findViewById(R.id.button1 );
        button2=(Button) findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,MainActivity3.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
    }
}