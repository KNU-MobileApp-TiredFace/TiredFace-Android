package com.example.thirty.tiredface.JsonDataProcessor.TCPSocket;

import android.util.Log;

import com.example.thirty.tiredface.JsonDataProcessor.JsonObjectEventObserver.JsonObjectEventObserver;
import com.example.thirty.tiredface.JsonDataProcessor.JsonObjectEventObserver.JsonObjectEventSubject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//TCP 소켓으로 데이터를 수신하는 클래스
//일반 소켓으로도 가능하고, 서버소켓으로도 이용 가능
public class TCPJsonSocketReceiver implements JsonObjectEventSubject {
    private Socket socket = null;
    private ServerSocket listeningServerSocket = null;
    private BufferedReader reader;
    private ArrayList<JsonObjectEventObserver> observers;
    private JSONObject receivedString;

    //일반 소켓을 이용하도록 생성
    public TCPJsonSocketReceiver(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        observers = new ArrayList<JsonObjectEventObserver>();
    }

    //서버 소켓을 이용하도록 생성
    public TCPJsonSocketReceiver(ServerSocket socket) throws IOException {
        this.listeningServerSocket = socket;
        observers = new ArrayList<JsonObjectEventObserver>();
    }

    //서버소켓을 이용하여 응답 대기
    public JSONObject waitForAnswer_ServerSocket() {
        serverSocketListen.start();
        try {
            serverSocketListen.join();
            listeningServerSocket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receivedString;
    }

    //일반소켓을 이용하여 응답 대기
    public JSONObject waitForAnswer() {
        listenSocket.start();
        try {
            listenSocket.join();
            socket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receivedString;
    }

    //관찰자 등록
    public void registerObserver(JsonObjectEventObserver observer) {
        observers.add(observer);
    }

    //관찰자 제거
    public void removeObserver(JsonObjectEventObserver observer) {
        observers.remove(observer);
    }

    public void notifyObserver(JSONObject jsonObject) {
        for (JsonObjectEventObserver observer : observers)
            observer.update(jsonObject);
    }

    //서버소켓을 이용하여 응답을 수신하는 쓰래드 클래스
    private Thread serverSocketListen = new Thread() {

        public void run() {
            while (true) {
                String line = null;
                String cattedLine = "";

                Socket socket = null;
                try {
                    socket = listeningServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if (reader.ready()) {
                        Log.i("DevelopLog", "Ready to read");

                        while (true) {
                            Log.i("DevelopLog", "Lets read");
                            char chbuf[] = new char[8192];
                            int size = reader.read(chbuf, 0, 8192);
                            String buf = new String(chbuf);
                            buf = buf.substring(0, size);
                            Log.i("DevelopLog", "read done");
                            Log.i("DevelopLog", "catting : " + new String(buf));
                            cattedLine += new String(buf);
                            if (buf.charAt(buf.length() - 1) == '}')
                                break;

                            Log.i("DevelopLog", "catted : " + cattedLine);
                        }

                        try {
                            receivedString = new JSONObject(cattedLine);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("DevelopLog", "toJson : " + receivedString.toString());
                        Log.i("DevelopLog", "received Message Size : " + receivedString.toString().length());
                    }
                } catch (IOException e) {
                    Log.i("DevelopLog", "receving error");
                    e.printStackTrace();
                }
            }
        }
    };

    //일반 소켓을 이용하여 응답을 수신하는 클래스
    private Thread listenSocket = new Thread() {

        public void run() {
            String line = null;
            String cattedLine = "";
            Log.i("DevelopLog", "String Thread");

            try {
                Log.i("DevelopLog", "Ready to read");

                while (true) {
                    Log.i("DevelopLog", "Lets read");
                    char chbuf[] = new char[8192];
                    int size = reader.read(chbuf, 0, 8192);
                    String buf = new String(chbuf);
                    buf = buf.substring(0, size);
                    Log.i("DevelopLog", "read done");
                    Log.i("DevelopLog", "catting : " + new String(buf));
                    cattedLine += new String(buf);
                    if (buf.charAt(buf.length() - 1) == '}')
                        break;
                    String objJsonWord = "[object JSON]";
                    if (cattedLine.length() >= objJsonWord.length() && cattedLine.substring(cattedLine.length() - objJsonWord.length()).contains(objJsonWord)) {
                        cattedLine = cattedLine.substring(0, cattedLine.length() - objJsonWord.length());
                        break;
                    }

                    Log.i("DevelopLog", "Last word : " + buf.charAt(buf.length() - 1));

                    Log.i("DevelopLog", "catted : " + cattedLine);
                }

                try {
                    if(cattedLine.contains("b'"))
                    {
                        cattedLine = cattedLine.replace("b'","");
                    }
                    receivedString = new JSONObject(cattedLine);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("DevelopLog", "toJson : " + receivedString.toString());
                Log.i("DevelopLog", "received Message Size : " + receivedString.toString().length());
                //Log.i("DevelopLog", cattedLine);
            } catch (IOException e) {
                Log.i("DevelopLog", "receving error");
                e.printStackTrace();
            }
        }

    };
}
