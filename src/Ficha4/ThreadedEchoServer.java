package src.Ficha4;

import src.utils.InputValidation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ThreadedEchoServer {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in);) {


            //valida os between e inclui os extremos
            int portNumber = InputValidation.validateIntBetween(sc, "Digite o valor da porta (entre 1024" +
                    " e 65535): ", 1024, 65535);

            try (ServerSocket serverSocket = new ServerSocket(portNumber);) {
                while (true) {
                    System.out.println("A espera da ligação.");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Cliente ligado.");

                    //nesse momento o servidor ja comunica com vários clientes ao mesmo tempo
                    new EchoThread(clientSocket).start();
                }

            } catch (Exception e) {
                System.err.println("Erro genérico: " + e.getMessage());
                System.exit(2);
            }
        }
    }
}