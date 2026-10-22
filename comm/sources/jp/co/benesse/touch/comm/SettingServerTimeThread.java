package jp.co.benesse.touch.comm;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jp.co.benesse.touch.comm.CommService.ServerTimeListener;

public class SettingServerTimeThread implements Runnable {
    public static final String TAG = "SettingServerTimeThread";
    private CommService commService;

    public SettingServerTimeThread(CommService commService) {
        this.commService = commService;
    }

    @Override
    public void run() {
        Lock waitLock = new ReentrantLock();
        Condition waitCond = waitLock.newCondition();
        waitLock.lock();
        try {
            SettingServerTimeRequest req = new SettingServerTimeRequest();
            CommService commService = this.commService;
            commService.getClass();
            SettingServerTimeResponse res = new SettingServerTimeResponse(commService.new ServerTimeListener(true), waitLock, waitCond);
            this.commService.requestHttpServer(req, res);
            waitCond.awaitNanos(100000000000L);
        } catch (InterruptedException e) {
        } finally {
            waitLock.unlock();
        }
    }
}
