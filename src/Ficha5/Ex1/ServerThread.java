package src.Ficha5.Ex1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private final Socket clientSocket;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    public void run() {
        //initializing buffer and writer
        try(PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ){

            //what the client will send
            //we receive and convert into integer
            int value = Integer.parseInt(in.readLine());

            //converting value to chips
            int currentChips = value/5;

            //sending message
            System.out.println("Thread Name: " + this.getName() + ": O Cliente investiu: "+ value + " euros. E obteve: "
                    + currentChips + " chips");

            //sending to the Client - Information the number of chips
            out.println(currentChips);

            //stay waiting for the Time Exception
            try{
                wait();
            }catch (InterruptedException e){
                System.err.println("Thread: " + this.getName() +
                        ": vai ser informado que o jogo vai começar" +
                        ". " +
                        e.getMessage());
                out.println("Terminou o período para conexão de jogadores novos. \nFaça sua Aposta.");
            }

            //game definition
            //time to game
            int number = 0, chipsToBet = 0;
            while(true){
                //number of chips to bet
                number = Integer.parseInt(in.readLine());
                System.out.println("Thread Name: " + this.getName() + ": O Cliente quer apostar no número: " + number);
                chipsToBet = Integer.parseInt(in.readLine());
                System.out.println("Thread Name: " + this.getName() + ": O Cliente quer apostar " + chipsToBet + " chips");

                //decrement my value of chips when bet
                currentChips -= chipsToBet;

                //TODO: bet logics

            }


        }catch (Exception e){
            //exceptions
            System.err.println("Thread: " + this.getName() +
                    ": Erro de criação dos buffers do socket" +
                    ". ERRO: " +
                    e.getMessage());
            System.exit(3);
        }
    }

}
