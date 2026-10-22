package jp.co.benesse.touch.auth;

import java.util.ArrayList;
import java.util.List;

public class SessionUpdateMonitor {
    public static final String TAG = "SessionUpdateMonitor";
    private static final SessionUpdateMonitor monitor = new SessionUpdateMonitor();
    private boolean isUpdating = false;
    List<SessionUpdateListener> listeners = new ArrayList();

    private SessionUpdateMonitor() {
    }

    static SessionUpdateMonitor getInstance() {
        return monitor;
    }

    synchronized void setUpdatingStatus(boolean status) {
        this.isUpdating = status;
    }

    synchronized boolean isUpdating() {
        return this.isUpdating;
    }

    synchronized void registerUpdateListener(SessionUpdateListener listener) {
        this.listeners.add(listener);
    }

    synchronized void notifyUpdateAll() {
        for (SessionUpdateListener listener : this.listeners) {
            try {
                listener.notifyUpdate();
            } catch (Exception e) {
            }
        }
        this.listeners.clear();
    }
}
