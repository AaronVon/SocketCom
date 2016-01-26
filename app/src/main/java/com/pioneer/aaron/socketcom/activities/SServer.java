package com.pioneer.aaron.socketcom.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.pioneer.aaron.socketcom.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aaron on 1/21/16.
 */
public class SServer extends AppCompatActivity {
    @Bind(R.id.info_port)
    TextView info_port;
    @Bind(R.id.info_ip)
    TextView info_ip;
    @Bind(R.id.msg)
    TextView msg;

    String message = "";
    ServerSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sserver);
        ButterKnife.bind(this);

        loadView();
    }

    private void loadView() {
        info_ip.setText(getIPAddress());

//        Thread socketServerThread = new Thread(new SocketServerThread());
//        socketServerThread.start();
        new SocketServerThread().start();
    }

    private String getIPAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "siteLocalAddress: " + inetAddress.getHostAddress() + "\n";
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            ip += "Something Wrong!\n" + e.toString() + "\n";
        }

        return ip;
    }

    private class SocketServerThread extends Thread {
        static final int SocketServerPort = 1234;
        int count = 0;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SocketServerPort);
                SServer.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        info_port.setText("Listening: " + serverSocket.getLocalPort());
                    }
                });

                while (true) {
                    Socket socket = serverSocket.accept();
                    ++count;
                    message += "#" + count + " from " + socket.getInetAddress() + ":" + socket.getPort() + "\n";
                    SServer.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            msg.setText(message);
                        }
                    });

//                    SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(socket, count);
//                    socketServerReplyThread.run();
//                    new SocketServerReplyThread(socket, count).run();
                    //注意多个线程抢占同一端口
                    
                    CommunicationThread communicationThread = new CommunicationThread(socket);
                    new Thread(communicationThread).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int count;

        public SocketServerReplyThread(Socket hostThreadSocket, int count) {
            this.hostThreadSocket = hostThreadSocket;
            this.count = count;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            String msgReply = "Hello, you are #" + count;

            try {
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();

                message += "replyed: " + msgReply + "\n";

                SServer.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg.setText(message);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                message += "Something Wrong!" + e.toString() + "\n";
            }

            SServer.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msg.setText(message);
                }
            });
        }
    }

    private class CommunicationThread implements Runnable {
        Socket clientSocket;
        BufferedReader input;

        public CommunicationThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    /*final String read = input.readLine();
                    SServer.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SServer.this, read, Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    Log.d("COMMUNICATION----", input.readLine());

                    PrintStream printStream = new PrintStream(clientSocket.getOutputStream());
                    printStream.print("OK");
                    printStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
