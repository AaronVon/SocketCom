package com.pioneer.aaron.socketcom.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.pioneer.aaron.socketcom.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private static final int SERVER_PORT = 1234;
    private static final String SERVER_IP = "192.168.31.107";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sclient_msg);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.bt_send)
    public void send_msg() {
        new SocketClientTask(SERVER_IP, SERVER_PORT, et_client_msg.getText().toString()).execute();

    }

    private class SocketClientTask extends AsyncTask {
        String server_ip;
        int server_port;
        String msg;
        BufferedReader input;

        public SocketClientTask(String server_ip, int server_port, String msg) {
            this.server_ip = server_ip;
            this.server_port = server_port;
            this.msg = msg;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Socket socket = null;
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                writer.println(msg);

                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                try {
                    Log.d("REPLY----", input.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

}
