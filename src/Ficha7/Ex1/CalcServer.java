package src.Ficha7.Ex1;

import src.utils.InputValidation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class CalcServer {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //validateIntBetween is inclusive one, so the 6000 and the 6002 are included
        int portNumber = InputValidation.validateIntBetween(sc, "Digite a porta que o servidor vai escutar (entre 6000 e 6002): ", 6000, 6002);

        try(
                ServerSocket serverSocket = new ServerSocket(portNumber);
                )
        {
            System.out.println("O servidor está ativo na porta: " + portNumber + "\n");

            //loop
            while(true){
                System.out.println("À espera de clientes...");

                try(
                    Socket socket = serverSocket.accept();
                    ){
                    //criar thread


                } catch (IOException e){
                    System.err.println("Erro ao criar socket ou ao criar a Thread do cliente: " + e.getMessage());
                }

            }

        } catch (IOException e) {
            System.err.println("Erro de I/O ao criar o ServerSocket: " + e.getMessage());
            System.exit(1);
        }

    }
}
