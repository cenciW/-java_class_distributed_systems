package src.Ficha6.Ex1;

import src.Ficha6.Defines;
import src.Ficha6.Numbers;
import src.utils.InputValidation;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        int port = Defines.port;
        String host = "localhost";

        Scanner sc = new Scanner(System.in);
        int min, max, sizeArray;
        int [] array;

        min = InputValidation.validateInt(sc, "Digite o valor mínimo que vai colocar no array:\n>: ");
        max = InputValidation.validateIntGTN(sc, "Digite o valor mínimo que vai colocar no array:\n>: ", (min-1));
        sizeArray = InputValidation.validateIntGT0(sc, "Digite o número de elementos que o array terá:\n>: ");


        Numbers numbers = new Numbers(min, max, sizeArray);
        System.out.println("Estamos enviando o objeto: " + numbers + "\n");

        sc.close();

        while(true) {
            try(
                    ServerSocket serverSocket = new ServerSocket(port);
                    Socket clientSocket = serverSocket.accept();
                    //str para enviar objetos
                    ObjectOutputStream objOut = new ObjectOutputStream(clientSocket.getOutputStream());
                    ){

                objOut.writeObject(numbers);
                objOut.flush();


            }catch (IOException e) {
                System.err.println("Erro ao criar ServerSocket: " + e.getMessage());
                System.exit(2);

            }

        }
    }
}
