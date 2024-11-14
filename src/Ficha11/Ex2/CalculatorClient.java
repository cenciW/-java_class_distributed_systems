package src.Ficha11.Ex2;

import src.Ficha11.Ex1.Calculator;
import src.Ficha11.Numbers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorClient {

    public static void main(String[] args) {
        try(
                //podemos usar ao invés do scanner
                //mais eficaz que o scanner
                //deve usar principalmente se usarmos threads.
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                ){

            //dentro do try
            //vai fazer a conexão com o rmi
            Calculator calculator = (Calculator) Naming.lookup("rmi://localhost:6666/calculator");

            while(true){
                System.out.println("Introduza \"Sair\" para terminar, ou a operação de cálculo a efetuar: " +
                "<operando1> <operador> <operando2>");

                String input = br.readLine();

                if(input.equalsIgnoreCase("Sair")){
                    break;
                }

                String [] parts = input.split(" ");

                if(parts.length != 3){
                    //acabou
                    System.out.println("Sintaxe inválida.");
                    continue;
                }
                Numbers numbers = new Numbers();

                try{
                    numbers.setNum1(Float.parseFloat(parts[0]));
                    numbers.setNum2(Float.parseFloat(parts[2]));
                    float result;

                    switch (parts[1]){
                        case "+":
                            System.out.println(input + " = " + calculator.add(numbers));
                            break;
                        case "-":
                            System.out.println(input + " = " + calculator.subtract(numbers));
                            break;
                        case "*", "x", "X":
                            System.out.println(input + " = " + calculator.multiply(numbers));
                            break;
                        case "/", ":":
                            try{
                                System.out.println(input + " = " + calculator.divide(numbers));
                            }catch (Exception e){
                                System.err.println("Erro ao Dividir: " + e.getMessage());
                            }
                            break;
                        default:
                            System.out.print("Operador inválido.");
                            break;
                    }

                } catch (NumberFormatException e){
                    System.err.println("Sintaxe inválida: " + e.getMessage());
                    continue;
                }

            }

        } catch (IOException e) {
            System.err.println("Erro ao tentar ler o texto introduzido pelo utilizador: " + e.getMessage());
        } catch (NotBoundException e) {
            System.err.println("O nome não está registrado no servidor RMI: " + e.getMessage());
        }

    }
    
}
