package src.Ficha7.Ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CallClientThread extends Thread{
    private Socket clientSocket;

    public CallClientThread(Socket clientSocket) {
        System.out.println(this.getName() + ": Ligação efetuada com o cliente.");

        this.clientSocket = clientSocket;
    }

    public void run(){
        try(
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));){
        } catch (IOException e) {
            System.err.println(this.getName() + ": Erro ao criar os buffers de comunicação.");
            System.exit(2);
        }

        System.out.println(this.getName()+ ": Ligação com o cliente encerrada.");

    }

}
