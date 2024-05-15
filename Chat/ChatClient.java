package unit12.Chat;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private Duplexer duplexer;
    private Scanner userIn;
    private String name;
    //creating new client with his nickname
    public ChatClient(int port) throws IOException{
        System.out.print("Your nickname: ");
        Scanner scan = new Scanner(System.in);
        name = scan.nextLine();
        //connectiong to host
        Socket socket = new Socket("localHost", port);
        duplexer = new Duplexer(socket);
        //send name to the server to notify all other clients
        duplexer.send(name);
        userIn = new Scanner(System.in);
    }

    public void task() throws IOException {
        //using thread to recieve messages from server anytime
        Thread reciveMessage = new Thread(() -> {
            try{
                while (true) {
                    String mes = "";
                    mes = duplexer.read();
                    if (mes != ""){
                        System.out.println(mes);
                        mes = "";
                    }
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }

        });
        reciveMessage.start();
        //sending messages to server and disconect if client typed quit
        String message = "";
        while (true) {
            message = userIn.nextLine();
            if(message.equals("quit")){
                //send to the server that he is disconnecting
                duplexer.send(name + " disconnected");
                break;
            }
            else{
                duplexer.send(name + ": " + message);
            }
        }
        duplexer.close();
        userIn.close();
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient(1234);
        client.task();
    }
    
}
