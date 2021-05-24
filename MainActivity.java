package com.example.batterynotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.example.batterynotification.App.CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;

    private TextView textView;
    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = NotificationManagerCompat.from(this);

        textView = findViewById(R.id.displayText);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        BroadcastReceiver broadcastReceiverBattery = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Integer integerBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                textView.setText(integerBatteryLevel.toString());

                if (integerBatteryLevel == 98) {

                    ringtone.play();
                }
                else if (integerBatteryLevel == 20) {
                    if (BatteryManager.BATTERY_STATUS_DISCHARGING == 1){
                        ringtone.play();
                    }
                }
            }
        };
        registerReceiver(broadcastReceiverBattery, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void stopButton(View view) {

        String title = "Battery Level";
        String message = textView.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_battery)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        notificationManager.notify(1, notification);

        ringtone.stop();
    }
}