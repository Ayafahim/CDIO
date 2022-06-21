package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public String fromImageRec;
    public static Socket client;
    static ServerSocket server;

    public static void main(String[] args) throws Exception {
        server = new ServerSocket(8080);
        System.out.println("wait for connection on port 8080");
        client = server.accept();

        System.out.println("got connection on port 8080");
        new Game().startGame();
    }

    public String test() throws IOException {
        //while (true) { //basically while game is running

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        fromImageRec = in.readLine();

        System.out.println(fromImageRec);
        // }
        return fromImageRec;
    }
}
