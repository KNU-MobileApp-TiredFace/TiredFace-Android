package com.example.thirty.tiredface2;

import android.util.Log;

import com.example.thirty.tiredface2.JsonDataSender.JsonDataSender;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

//TCP 소켓으로 데이터를 전송하는 클래스
public class TCPDataWriter implements JsonDataSender {
    private Socket sendSocket;
    private String jsonString;  //전송할 Json 데이터. 쓰레드에서 사용하기 위해 내부 필드로 선언함

    public TCPDataWriter(Socket sendSocket){
        this.sendSocket = sendSocket;
    }

    //Json으로 데이터 전송
    public void sendData(JSONObject jsonObject){
        this.jsonString = jsonObject.toString();

        //데이터 전송을 위한 쓰레드 생성
        SocketWriterThread socketWriterThread = new SocketWriterThread();
        socketWriterThread.start();
        try {
            socketWriterThread.join();  //쓰레드 작업 완료 대기
            sendSocket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("DevelopLog","joined");
    }

    //String 데이터를 전송
    //꼭 Json 타입이 아니어도 가능
    @Override
    public void sendData(String jsonStr){
        this.jsonString = jsonStr;
        SocketWriterThread socketWriterThread = new SocketWriterThread();

        socketWriterThread.start();
        try {
            socketWriterThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("DevelopLog","joined");
    }

    //데이터를 전송하는 쓰레드 클래스
    public class SocketWriterThread extends Thread{
        public void run() {
            try {
                BufferedWriter networkWriter;
                OutputStreamWriter streamWriter;
                streamWriter = new java.io.OutputStreamWriter(sendSocket.getOutputStream());
                networkWriter = new java.io.BufferedWriter(streamWriter);
                networkWriter.write(jsonString);
                networkWriter.flush();
                Log.i("DevelopLog", "sending data : " + jsonString);
            } catch (Exception e) {

            }
        }
    }

}
