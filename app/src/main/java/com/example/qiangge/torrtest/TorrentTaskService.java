package com.example.qiangge.torrtest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.qiangge.torrtest.core.TorrentEngine;
import com.example.qiangge.torrtest.core.TorrentEngineCallback;
import com.example.qiangge.torrtest.core.utils.SettingsManager;

public class TorrentTaskService extends Service implements TorrentEngineCallback {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getApplicationContext();
        TorrentEngine.getInstance().setContext(context);
        TorrentEngine.getInstance().setCallback(this);
        TorrentEngine.getInstance().setSettings(SettingsManager.readEngineSettings(context));
        TorrentEngine.getInstance().start();
        return START_STICKY;
    }

    @Override
    public void onTorrentAdded(String id) {

    }

    @Override
    public void onTorrentStateChanged(String id) {

    }

    @Override
    public void onTorrentFinished(String id) {

    }

    @Override
    public void onTorrentRemoved(String id) {

    }

    @Override
    public void onTorrentPaused(String id) {

    }

    @Override
    public void onTorrentResumed(String id) {

    }

    @Override
    public void onEngineStarted() {

    }

    @Override
    public void onTorrentMoved(String id, boolean success) {

    }

    @Override
    public void onIpFilterParsed(boolean success) {

    }

    @Override
    public void onMagnetLoaded(String hash, byte[] bencode) {

    }

    @Override
    public void onTorrentMetadataLoaded(String id, Exception err) {

    }

    @Override
    public void onRestoreSessionError(String id) {

    }
}
