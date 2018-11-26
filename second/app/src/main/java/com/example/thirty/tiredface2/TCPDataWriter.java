package com.example.thirty.tiredface2;

import android.util.Log;

import com.example.thirty.tiredface2.JsonDataSender.JsonDataSender;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class TCPDataWriter implements JsonDataSender {

    Socket sendSocket;
    String defaultEvent;
    private BufferedWriter networkWriter;
    private OutputStreamWriter streamWriter;
    private JSONObject jsonObject;

    public TCPDataWriter(Socket sendSocket){
        this.sendSocket = sendSocket;
        try {
            streamWriter = new java.io.OutputStreamWriter(sendSocket.getOutputStream());
            networkWriter = new java.io.BufferedWriter(streamWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendData(JSONObject jsonObject){
        this.jsonObject = jsonObject;

        SocketWriterThread socketWriterThread = new SocketWriterThread();

        socketWriterThread.start();
        try {
            socketWriterThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("DevelopLog","joined");
    }

    public class SocketWriterThread extends Thread{
        public void run() {
            try {
                //streamWriter.write(jsonObject.toString());
                //streamWriter.flush();
                BufferedWriter networkWriter;
                OutputStreamWriter streamWriter;
                streamWriter = new java.io.OutputStreamWriter(sendSocket.getOutputStream());
                networkWriter = new java.io.BufferedWriter(streamWriter);
                networkWriter.write(jsonObject.toString());
                networkWriter.flush();
                Log.i("DevelopLog", "sending data : " + jsonObject.toString());

            } catch (Exception e) {

            }
        }
    }

}
