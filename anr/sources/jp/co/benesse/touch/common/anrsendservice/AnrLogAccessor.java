package jp.co.benesse.touch.common.anrsendservice;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import jp.co.benesse.touch.common.anrsendservice.util.Logger;

public class AnrLogAccessor {
    private static final String TAG = AnrLogAccessor.class.getSimpleName();
    protected RandomAccessFile mAccessFile;
    protected FileLock mFileLock;

    public synchronized void open(File file) throws IOException {
        if (this.mFileLock != null || this.mAccessFile != null) {
            Logger.w(TAG, "Already opened.");
            return;
        }
        RandomAccessFile randomAccessFile = null;
        try {
            RandomAccessFile randomAccessFile2 = new RandomAccessFile(file, "rw");
            try {
                this.mFileLock = randomAccessFile2.getChannel().tryLock();
                this.mAccessFile = randomAccessFile2;
            } catch (Exception e) {
                e = e;
                randomAccessFile = randomAccessFile2;
                Logger.e(TAG, e);
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                throw new IOException(e);
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public synchronized void close() {
        if (this.mFileLock != null) {
            try {
                this.mFileLock.release();
            } catch (IOException e) {
                Logger.e(TAG, e);
            }
            this.mFileLock = null;
            if (this.mAccessFile != null) {
                try {
                    this.mAccessFile.close();
                } catch (IOException e2) {
                    Logger.e(TAG, e2);
                }
                this.mAccessFile = null;
            }
        } else if (this.mAccessFile != null) {
            this.mAccessFile.close();
            this.mAccessFile = null;
        }
        throw th;
    }

    public synchronized String readLine() throws IOException {
        String line;
        line = null;
        if (this.mFileLock != null && this.mAccessFile != null) {
            line = this.mAccessFile.readLine();
        }
        return line;
    }

    public synchronized void clear() throws IOException {
        if (this.mFileLock != null && this.mAccessFile != null) {
            this.mAccessFile.seek(0L);
            this.mAccessFile.setLength(0L);
        }
    }
}
