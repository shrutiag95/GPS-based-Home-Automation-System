package shruti.agarwal.org.iotapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Agarwal's on 11/17/2016.
 */
public class service extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        //return super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }
}