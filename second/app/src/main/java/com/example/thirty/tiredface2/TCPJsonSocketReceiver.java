package com.example.thirty.tiredface2;

import android.util.Log;

import com.example.thirty.tiredface2.JsonObjectEventObserver.JsonObjectEventObserver;
import com.example.thirty.tiredface2.JsonObjectEventObserver.JsonObjectEventSubject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class TCPJsonSocketReceiver implements JsonObjectEventSubject {

    private Socket listeningSocket = null;
    private BufferedReader reader;
    private ArrayList<JsonObjectEventObserver> observers;

    public TCPJsonSocketReceiver(Socket socket) throws IOException {
        this.listeningSocket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        observers = new ArrayList<JsonObjectEventObserver>();
        listenSocket.start();
    }

    public void registerObserver(JsonObjectEventObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(JsonObjectEventObserver observber) {

    }

    public void notifyObserver(JSONObject jsonObject) {

    }

    private Thread listenSocket = new Thread() {

        public void run() {
            while (true) {
                String line = null;
                String cattedLine = "";


                try {
                    if (reader.ready()) {
                        Log.i("DevelopLog", "Ready to read");
                        while (true) {
                            if(!reader.ready())
                                break;
                            char buf[] = new char[100];

                            reader.read(buf);
                            cattedLine += new String(buf);
                            Log.i("DevelopLog", "catted : " + cattedLine);

                        }
                        JSONObject tempObject = null;
                        try {
                            tempObject = new JSONObject(cattedLine);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("DevelopLog","toJson : " +tempObject.toString());
                        //Log.i("DevelopLog", cattedLine);
                    }
                } catch (IOException e) {
                    Log.i("DevelopLog", "receving error");
                    e.printStackTrace();
                }
            }
        }

    };
}
