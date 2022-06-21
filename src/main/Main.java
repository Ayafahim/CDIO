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
        new Game().startGame();
    }

    public void initiate() throws IOException {
        server = new ServerSocket(8080);
        System.out.println("waiting for connection on port 8080");
        client = server.accept();
        System.out.println("got connection on port 8080");
    }

    public String test() throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println("Awaiting card to be read :)");
        fromImageRec = in.readLine();
        in.close();
        System.out.println("Card read.");

        return fromImageRec;
    }
}
