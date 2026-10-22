package jp.co.benesse.touch.common.anrsendservice;

import android.content.Context;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.File;
import jp.co.benesse.touch.common.anrsendservice.util.Logger;

public class AnrSendWorker extends Worker {
    private static final String ANR_LOG_PATH = "/factory/anrlog/anr.log";
    private static final String TAG = AnrSendWorker.class.getSimpleName();
    private AnalyticsAdapter mAnalytics;
    private AnrSendNotification mNotification;

    public AnrSendWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
        this.mAnalytics = new AnalyticsAdapter(context);
        this.mNotification = new AnrSendNotification();
    }

    @Override
    public ListenableWorker.Result doWork() {
        Logger.d(TAG, "doWork 001");
        Context applicationContext = getApplicationContext();
        this.mNotification.makeNotification(applicationContext);
        this.mAnalytics.sendAnrLogFile(applicationContext, new File(ANR_LOG_PATH));
        this.mNotification.cancelNotification(applicationContext);
        Logger.d(TAG, "doWork 002");
        return ListenableWorker.Result.success();
    }
}
