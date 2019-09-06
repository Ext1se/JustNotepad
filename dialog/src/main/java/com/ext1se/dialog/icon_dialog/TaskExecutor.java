package com.ext1se.dialog.icon_dialog;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

public class TaskExecutor {

    private boolean isRunning;
    private Thread backgroundThread;
    private Handler uiThreadHandler;

    public TaskExecutor() {
        uiThreadHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Execute a runnable task in background then call a callback on UI thread
     * @param r runnable task
     * @param cb callback to call when done
     */
    void execute(final Runnable r, @Nullable final Callback cb) {
        isRunning = true;
        backgroundThread = new Thread("taskExecutor") {
            @Override
            public void run() {
                r.run();
                isRunning = false;
                if (cb != null) {
                    uiThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.onDone();
                        }
                    });
                }
            }
        };
        backgroundThread.start();
    }

    void interrupt() {
        if (backgroundThread != null && backgroundThread.isAlive()) {
            backgroundThread.interrupt();
        }
        isRunning = false;
    }

    boolean isRunning() {
        return isRunning;
    }

    interface Callback {
        void onDone();
    }

}
