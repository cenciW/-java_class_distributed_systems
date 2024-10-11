package src.Ficha5.Ex2;

import src.utils.InputValidation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Introduza o hostname do servidor ao qual vai se ligar: ");
        String hostName = sc.nextLine();

        int portNumber = InputValidation.validateIntBetween(sc,
                "Introduza a porta em que o server está escutando: ",
                1024, 65535);

        try(
                //client socket to talk with the server - Client x Server
                Socket clientSocket = new Socket(hostName, portNumber);
                //writer
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                //buffer
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            System.out.print("\n\nDigite as fichas que deseja apostar. " +
                    "O valor tem que ser múltiplo de 5\n :> ");

            //getting the chips value
            String textChips = sc.nextLine();
            int valueInt = 0;
            do {
                try{
                    valueInt = Integer.parseInt(textChips);



                }catch (Exception e){
                    System.err.println("Número inválido.");
                    System.exit(1);
                }
            //while it isn't divisible for 5 and isn't greater than 5
            } while(valueInt % 5 != 0 || valueInt < 5);

            //send to the server
            out.println(valueInt);

            //receive from server
            int currentChips = in.read();

            //console print
            System.out.println("Número de fichas investidas: " + currentChips);

            //client receive from server and print the message
            System.out.println(in.readLine());

            int number, chipsToBet;
            while(true){
                number = InputValidation.validateIntBetween(sc,"Introduza o número que quer apostar. Tem de ser um número entre (1 e 36)\n :> ", 1, 36);
                chipsToBet = InputValidation.validateIntBetween(sc, "Introduza quantas fichas deseja apostar no número: "+ number+ ". Tem de ser um número entre (0 e " + currentChips + ") ", 1, currentChips);

                out.println(chipsToBet);

                

            }

        }catch (IOException e) {
            System.out.println("Erro de I/O: " + e.getMessage());
            System.exit(4);
        }catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        sc.close();
    }
}
