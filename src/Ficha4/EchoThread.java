package src.Ficha4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoThread extends Thread {
    private final Socket clientSocket;

    public EchoThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        //try especial que aceita parametros
        //parametros do try
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String fromClient;
            do {
                System.out.println("Thread " + this.getName() + ": à espera de uma mensagem.");
                fromClient = in.readLine();
                System.out.println("Thread " + this.getName() + ": Recebeu a mensagem: " + fromClient);

                out.println("Thread " + this.getName() + ": " + fromClient.length() + " caracteres");

            } while (!fromClient.equalsIgnoreCase("Adeus"));

            System.out.println("Thread " + this.getName() + ": Terminou.");

        } catch (IOException e) {
            System.out.println("Thread "+ this.getName() + "\nErro: " + e.getMessage());
        }
    }
}
