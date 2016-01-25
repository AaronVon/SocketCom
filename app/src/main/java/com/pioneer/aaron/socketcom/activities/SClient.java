package com.pioneer.aaron.socketcom.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pioneer.aaron.socketcom.R;

import butterknife.ButterKnife;

/**
 * Created by Aaron on 1/21/16.
 */
public class SClient extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sclient);
        ButterKnife.bind(this);

        loadView();
    }

    private void loadView() {

    }
}
