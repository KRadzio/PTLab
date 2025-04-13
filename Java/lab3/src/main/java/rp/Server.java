package rp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private boolean run = true;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");
            while (run) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket cliSocket;

        public ClientHandler(Socket cliSocket) {
            this.cliSocket = cliSocket;

        }

        public void run() {
            try {
                try (var os = new ObjectOutputStream(cliSocket.getOutputStream());
                        var is = new ObjectInputStream(cliSocket.getInputStream())) {
                    System.out.println("Client connected");
                    os.writeObject("ready");
                    var numberOfMessages = (int) is.readObject();
                    System.out.println(String.format("waiting for %d messages", numberOfMessages));
                    os.writeObject("ready for messages");
                    for (var i = 0; i < numberOfMessages; i++) {
                        var msg = (Message) is.readObject();
                        System.out.println(String.format("received message %d %s", msg.getNumber(), msg.getContent()));
                    }
                    os.writeObject("finish");
                }
                cliSocket.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Client disconnected");
        }
    }

    public static void main(String[] args) {
        new Server(2137);
    }
}
