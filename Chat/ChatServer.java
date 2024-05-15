package unit12.Chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    //use array to send message to all clients
    private List<Duplexer> clients = new ArrayList<>();

    public static void main(String[] args){
        //start server
        ChatServer server = new ChatServer();
        server.start(1234);
    }

    public void start(int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server started");
            while(true){
                //accepting all new clients (i could add Map to handle all nicknames but left it on clients)
                Socket clientSocket = serverSocket.accept();
                Duplexer duplexer = new Duplexer(clientSocket);
                String name = duplexer.read();
                System.out.println(name + " connected");
                clients.add(duplexer);
                //notify other clients that new guy connected
                for (Duplexer client : clients) {
                    if (client != duplexer) {
                        client.send(name + " connected");
                    }
                }
                //using thread to recieve all messages without stepblocks
                new Thread(() -> handleClient(duplexer)).start();
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }
    //recieving messages from clients and send them to others
    private void handleClient(Duplexer duplexer) {
        try {
            String message = "";

            while (true) {
                message = duplexer.read();
                if (message == null || message.isEmpty()){
                    break;
                }
                System.out.println(message);
                for (Duplexer client : clients) {
                    if (client != duplexer) {
                        client.send(message);
                    }
                }
            }
            //if client closed program server remove him from array 
            duplexer.close();
            clients.remove(duplexer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}