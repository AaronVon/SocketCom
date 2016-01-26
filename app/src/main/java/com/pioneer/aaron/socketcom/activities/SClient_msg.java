package com.pioneer.aaron.socketcom.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.pioneer.aaron.socketcom.R;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Aaron on 1/26/16.
 */
public class SClient_msg extends AppCompatActivity {
    @Bind(R.id.et_client_msg)
    EditText et_client_msg;

    private Socket socket;
    private static final int SERVER_PORT = 1234;
    private static final String SERVEr_IP = "192.168.31.107";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sclient_msg);
        ButterKnife.bind(this);
        
        new SocketClientThread().start();

    }

    @OnClick(R.id.bt_send)
    public void send_msg() {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            writer.println(et_client_msg.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SocketClientThread extends Thread {
        @Override
        public void run() {
            try {
                InetAddress serverAddress = InetAddress.getByName(SERVEr_IP);
                socket = new Socket(serverAddress, SERVER_PORT);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
