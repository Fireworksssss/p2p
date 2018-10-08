package com.example.qiangge.torrtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.qiangge.torrtest.core.TorrentEngine;
import com.example.qiangge.torrtest.core.TorrentEngineCallback;
import com.example.qiangge.torrtest.core.utils.SettingsManager;

public class MainActivity extends AppCompatActivity  implements
        TorrentEngineCallback {

    private String man="magnet:?xt=urn:btih:3dbf10b18b1e9630d814dbc086afb8326b6b6223&dn=%E5%A4%A7%E8%AF%9D%E8%A5%BF%E6%B8%B8%E4%B9%8B%E5%A4%A7%E5%9C%A3%E5%A8%B6%E4%BA%B2.mkv&tr=udp%3A%2F%2Ftr.hmyg22.com%3A6969";
    private String url = "magnet:?xt=urn:btih:d633c753e228e34d97a86aefd33f6436089c527e&dn=%E6%B6%88%E5%A4%B1%E7%9A%84%E7%88%B1%E4%BA%BABD%E4%B8%AD%E8%8B%B1%E5%8F%8C%E5%AD%97.rmvb&tr=udp%3A%2F%2Ftr.hmyg22.com%3A6969";
    private TextView text,text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        text2 = findViewById(R.id.text2);
        startService(new Intent(this, TorrentTaskService.class));
//        TorrentEngine.getInstance().setContext(this);
//        TorrentEngine.getInstance().setCallback(this);
//        TorrentEngine.getInstance().setSettings(SettingsManager.readEngineSettings(this));
//        TorrentEngine.getInstance().start();
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TorrentEngine.getInstance().fetchMagnet(url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TorrentEngine.getInstance().fetchMagnet(man);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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
