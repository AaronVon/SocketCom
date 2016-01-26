package com.pioneer.aaron.socketcom.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pioneer.aaron.socketcom.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Aaron on 1/21/16.
 */
public class SClient extends AppCompatActivity {
    @Bind(R.id.tv_response)
    TextView tv_response;
    @Bind(R.id.et_server_address)
    EditText et_server_address;
    @Bind(R.id.et_server_port)
    EditText et_server_port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sclient);
        ButterKnife.bind(this);

        loadView();
    }

    private void loadView() {

    }

    @OnClick(R.id.bt_connect)
    public void connect(Button button) {
//        ClientTask clientTask = new ClientTask(et_server_address.getText().toString(), Integer.parseInt(et_server_port.getText().toString()));
        ClientTask clientTask = new ClientTask("192.168.31.107", 1234);

        clientTask.execute();
    }

    @OnClick(R.id.bt_clear)
    public void clear(Button button) {
        tv_response.setText("");
    }

    private class ClientTask extends AsyncTask {
        String server_address;
        int server_port;
        String response = "";
        int buffersize = 1024;

        public ClientTask(String server_address, int server_port) {
            this.server_address = server_address;
            this.server_port = server_port;

        }

        @Override
        protected Object doInBackground(Object[] params) {
            Socket socket = null;
            try {
                socket = new Socket(server_address, server_port);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(buffersize);
                byte[] buffer = new byte[buffersize];

                int byteRead;
                InputStream inputStream = socket.getInputStream();

                while ((byteRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, byteRead);
                    response += byteArrayOutputStream.toString("utf-8");
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();

            } catch (IOException e) {
                response = "IOException: " + e.toString();
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
            tv_response.setText(response);
            Log.d("response", response);

            super.onPostExecute(o);
        }
    }
}
