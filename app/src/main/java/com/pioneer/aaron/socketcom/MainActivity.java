package com.pioneer.aaron.socketcom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pioneer.aaron.socketcom.activities.SClient;
import com.pioneer.aaron.socketcom.activities.SServer;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @OnClick({R.id.button_client, R.id.button_server})
    public void switchfunction(View view) {
        switch (view.getId()) {
            case R.id.button_client:
                startActivity(new Intent(this, SClient.class));
                break;
            case R.id.button_server:
                startActivity(new Intent(this, SServer.class));
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadView();
    }

    private void loadView() {

    }

}
