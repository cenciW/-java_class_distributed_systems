package src.EXERCISE_Forca.Ficha3.Ex1;

import src.utils.InputValidation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //valida os between e inclui os extremos
        int portNumber = InputValidation.validateIntBetween(sc, "Digite o valor da porta (entre 1024" +
                " e 65535): ", 1024, 65535);
        System.out.println("A espera da ligação");

        //

        try (ServerSocket serverSocket = new ServerSocket(portNumber);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
             //só conhecemos o serverSocket aqui dentro do try
             //fica sempre bloqueado até que um cliente se ligue, após se ligar segue o codigo
            String fromClient;

            System.out.println("Cliente ligando");
            do {
                fromClient = in.readLine();
                System.out.println("O cliente enviou: " + fromClient);
                out.println("A frase tem " + fromClient.length() + " caracteres");

            } while (!fromClient.equalsIgnoreCase("Adeus"));


        } catch (Exception e) {
            //output predefinido para erros, ou seja a linha sai em vermelho
            System.err.println("Ocorreu um erro de I/O ao criar o socket ou ler a mensagem. Erro: " + e.getMessage());
            System.exit(0);
        } finally {

        }


        sc.close();
    }

}
