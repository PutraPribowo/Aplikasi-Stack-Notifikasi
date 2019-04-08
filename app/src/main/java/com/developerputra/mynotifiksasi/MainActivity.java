package com.developerputra.mynotifiksasi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
        private static final CharSequence CHANNEL_NAME = "Developer Putra Pribowo";
        private final static String GROUP_KEY_EMAILS = "group_key_emails";
        private final static int NOTIF_REQUEST_CODE = 200;
        private EditText Sender;
        private EditText Message;
        private Button btnKirim;
        private int idNotif = 0;
        private int maxNotif = 2;

        private List<Notifikasi> stackNotif = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Sender = findViewById(R.id.edtSender);
            Message = findViewById(R.id.edtMessage);
            btnKirim = findViewById(R.id.btnSend);

            btnKirim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sender = Sender.getText().toString();
                    String message = Message.getText().toString();
                    if (sender.isEmpty() || message.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Data harus diisi", Toast.LENGTH_SHORT).show();
                    } else {
                        stackNotif.add(new Notifikasi(idNotif, sender, message));
                        sendNotif();
                        idNotif++;
                        Sender.setText("");
                        Message.setText("");
                        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
            });
        }

        @Override
        protected void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
            stackNotif.clear();
            idNotif = 0;
        }

        private void sendNotif() {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notifikasi);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIF_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder;


            String CHANNEL_ID = "channel_01";
            if (idNotif < maxNotif) {
                mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("New Email from " + stackNotif.get(idNotif).getSender())
                        .setContentText(stackNotif.get(idNotif).getMessage())
                        .setSmallIcon(R.drawable.email)
                        .setLargeIcon(largeIcon)
                        .setGroup(GROUP_KEY_EMAILS)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
            } else {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                        .addLine("New Email from " + stackNotif.get(idNotif).getSender())
                        .addLine("New Email from " + stackNotif.get(idNotif - 1).getSender())
                        .setBigContentTitle(idNotif + " new emails")
                        .setSummaryText("putra.pribowo1005@students.unila.ac.id");
                mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle(idNotif + " new emails")
                        .setContentText("putra.pribowo1005@students.unila.ac.id")
                        .setSmallIcon(R.drawable.email)
                        .setGroup(GROUP_KEY_EMAILS)
                        .setGroupSummary(true)
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle)
                        .setAutoCancel(true);
            }

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);

                mBuilder.setChannelId(CHANNEL_ID);

                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(channel);
                }
            }

            Notification notification = mBuilder.build();

            if (mNotificationManager != null) {
                mNotificationManager.notify(idNotif, notification);
            }
        }
}
