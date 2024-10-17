package src.Ficha6.Ex2;

import src.Ficha6.Defines;
import src.Ficha6.Numbers;
import src.utils.InputValidation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static <objIn> void main(String[] args) {
        int port = Defines.port;
        Scanner sc = new Scanner(System.in);
        System.out.print("Digite o nome do servidor ao qual vai-se ligar: ");
        String hostname = sc.nextLine();



        try(
                Socket socket = new Socket(hostname, port);
                ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream())
        ){

            Numbers numbers = (Numbers)objIn.readObject();
            System.out.println(numbers);

            int mult = InputValidation.validateInt(sc, "Digite o valor pelo qual deseja multiplicar os elementos do " +
                    "array: ");

            //multiplicando o array de numbers pelo mult
            numbers.multiply(mult);

            System.out.println(numbers);



        }catch (UnknownHostException e){
            System.err.println("Servidor desconhecido.");
            System.exit(2);

        } catch (IOException e) {
            System.out.println("Erro ao ligar-se ao servidor ou ao receber o objeto.");
            System.exit(3);
        } catch (ClassNotFoundException e) {
            System.err.println("Tipo de objeto desconhecido.");
            System.exit(4);
        }
        finally {
            sc.close();
        }
    }
}
