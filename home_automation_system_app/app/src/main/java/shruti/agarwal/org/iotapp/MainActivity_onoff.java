package shruti.agarwal.org.iotapp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import java.util.List;
import java.util.logging.LogRecord;

public class MainActivity_onoff extends AppCompatActivity {
    ProgressDialog pDialog;
    Double distance;
    android.os.Handler h = null;
    Runnable r;
    SwitchCompat switchCompat;
    String urlon="http://androcation.16mb.com/form.php?state=1";
    String urloff="http://androcation.16mb.com/form.php?state=0";
    String urlread="http://androcation.16mb.com/LEDstate.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchCompat = (SwitchCompat)findViewById(R.id.sc);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Changing...");

        read();
        h = new android.os.Handler();
        r = new Runnable() {
            @Override
            public void run() {
                read();
                h.postDelayed(this, 20000);
            }
        };
        h.postDelayed(r, 20000);


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.sc:
                        if (isChecked)
                            on();
                        else
                            off();
                }
            }
        });
    }
    public void on(){
      //  pDialog.show();

        StringRequest strq = new StringRequest(Request.Method.POST, urlon, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // pDialog.hide();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //pDialog.hide();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(strq);

    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

public void read()
{

    //pDialog.show();

     Intent i =getIntent();
    final Bundle bundle = i.getExtras();
    distance = bundle.getDouble("dis");
//    Toast.makeText(getApplicationContext(), String.valueOf(distance), Toast.LENGTH_LONG).show();
    StringRequest strq = new StringRequest(Request.Method.POST, urlread, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //pDialog.hide();
            String read = response.toString();
            if (read.equals("0")) {
                switchCompat.setChecked(false);

                if (distance < 50) {
                    if (isAppIsInBackground(getApplicationContext())) {
//                        Toast.makeText(getApplicationContext(),"App in background",Toast.LENGTH_SHORT).show();
                        createNotification("It seems like you are very near to your home. Shall the lights be switched on?");
                    } else {
                        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity_onoff.this);
                        b.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(),"Positive",Toast.LENGTH_SHORT).show();
                                on();
                                switchCompat.setChecked(true);
                            }
                        });

                        b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            ;

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(),"No Problem",Toast.LENGTH_SHORT).show();
//
                            }
                        });
                        AlertDialog alert = b.create();
                        alert.setMessage("It seems like you are very near to your home. Shall the lights be switched on?");
                        alert.setTitle("Switch the lights on?");
                        alert.show();
                    }

                }
            } else if (read.equals("1")) {
                switchCompat.setChecked(true);

                if (distance >50)

                {
                    //Toast.makeText(getApplicationContext(), String.valueOf(distance), Toast.LENGTH_LONG).show();


                    if (isAppIsInBackground(getApplicationContext())) {
//                        Toast.makeText(getApplicationContext(),"App in background",Toast.LENGTH_SHORT).show();
                        createNotification("It seems like you are far from your home. Shall the lights be switched off?");
                    } else {
                        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity_onoff.this);

                        b.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(),"Positive",Toast.LENGTH_SHORT).show();
                                off();
                                switchCompat.setChecked(false);

                            }
                        });

                        b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            ;

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(),"No Problem",Toast.LENGTH_SHORT).show();
//
                            }
                        });
                        AlertDialog alert = b.create();
                        alert.setMessage("It seems like you are very far from your home. Shall the lights be switched off?");
                        alert.setTitle("Switch the lights off?");
                        alert.show();
                    }
                }
            }
        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            //pDialog.hide();
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    );
    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(strq);
}
    public void off(){
        //pDialog.show();

        StringRequest strq = new StringRequest(Request.Method.POST, urloff, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   pDialog.hide();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //pDialog.hide();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(strq);

    }

    @Override
    protected void onStop() {
        super.onStop();
//        h.removeCallbacks(r);
 }

    public void createNotification(String content) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Smart Home")
                .setContentText(content).setSmallIcon(R.drawable.img)
                .setContentIntent(pIntent)
               .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }


}
