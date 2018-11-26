package com.example.thirty.tiredface2;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class TCPSocketCreator {
    private Socket socket = null;
    private boolean isCreated = false;
    private  String ip;
    private  int port;

    public TCPSocketCreator(String ip, int port){
            this.ip = ip;
            this.port = port;
    }

    public Socket createSocket(){

        socketCreatorThread.start();
        while(!isCreated)
            ;

        return socket;
    }

    private void setSocket(String ip, int port) throws IOException {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }


    private Thread socketCreatorThread = new Thread() {

        public void run() {
            try {
                setSocket(ip,port);
                isCreated = true;
            } catch (Exception e) {

            }
        }
    };
}
