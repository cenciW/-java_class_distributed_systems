package src.Ficha3.Ex2;

import src.utils.InputValidation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in);) {

            System.out.print("Introduza o hostname do servidor ao qual vai se ligar: ");
            String hostName = sc.nextLine();

            int portNumber = InputValidation.validateIntBetween(sc, "Digite o valor da porta (entre 1024" +
                    " e 65535) em que o servidor irá escutar: ", 1024, 65535);

            //try especial que aceita parametros
            try (
                    //parametros do try
                    Socket clientSocket = new Socket(hostName, portNumber);
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {
                String toServer;

                do {
                    System.out.println("Introduza uma mensagem ao enviar ao servidor: ");
                    toServer = sc.nextLine();
                    //send message to server
                    out.println(toServer);
                    System.out.println("O servidor respondeu: " + in.readLine());

                    out.flush();

                } while (!toServer.equalsIgnoreCase("Adeus"));

            } catch (UnknownHostException e) {
                System.err.println("Host desconhecido. Erro: " + e.getMessage());
                System.exit(2);

            } catch (IOException e) {
                System.err.println("Erro de I/O ao criar o socket. Erro: " + e.getMessage());
                System.exit(3);

            }
        } catch (Exception e) {
            System.err.println("Erro genérico: " + e.getMessage());
            System.exit(4);
        }
    }
}
