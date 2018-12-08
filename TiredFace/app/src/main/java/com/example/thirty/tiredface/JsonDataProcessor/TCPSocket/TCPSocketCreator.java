package com.example.thirty.tiredface.JsonDataProcessor.TCPSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//TCP 소켓 생성을 담당하는 클래스
//서버소켓, 일반소켓 생성 가능
public class TCPSocketCreator {
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private  String ip;
    private  int port;

    public TCPSocketCreator(String ip, int port){
            this.ip = ip;
            this.port = port;
    }

    public Socket createSocket(){
        socketCreatorThread.start();
        try {
            socketCreatorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return socket;
    }

    //서버소켓 생성
    public ServerSocket createServerSocket(){

        serverSocketCreatorThread.start();
        try {
            serverSocketCreatorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return serverSocket;
    }

    //소켓 설정
    private void setSocket(String ip, int port) throws IOException {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    //서버 소켓 설정
    private void setServerSocket(String ip, int port) throws IOException {
        try {
            serverSocket = new ServerSocket(3456);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    //서버소켓을 생성하는 쓰레드 클래스
    private Thread serverSocketCreatorThread = new Thread() {

        public void run() {
            try {
                setServerSocket(ip,port);
            } catch (Exception e) {

            }
        }
    };

    //일반소켓을 생성하는 쓰레드 클래스
    private Thread socketCreatorThread = new Thread() {

        public void run() {
            try {
                setSocket(ip,port);
            } catch (Exception e) {

            }
        }
    };
}
