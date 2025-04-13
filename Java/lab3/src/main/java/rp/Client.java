package rp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket = null;

    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            try (var os = new ObjectOutputStream(socket.getOutputStream());
                    var is = new ObjectInputStream(socket.getInputStream())) {
                Scanner sc = new Scanner(System.in);
                String serverResponse;
                serverResponse = (String) is.readObject();
                System.out.println(serverResponse);
                var numberOfMessages = sc.nextInt();
                os.writeObject(numberOfMessages);
                serverResponse = (String) is.readObject();
                System.out.println(serverResponse);
                for (var i = 0; i < numberOfMessages; i++) {
                    os.writeObject(new Message(i, "Warol Kojtyla"));
                }
                serverResponse = (String) is.readObject();
                System.out.println(serverResponse);
                sc.close();
            }
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client("127.0.0.1", 2137);
    }
}
