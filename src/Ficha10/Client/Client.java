package src.Ficha10.Client;

import src.Ficha10.Hello;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) {
        try {
            //instanciando o stub apontando para o Hello do Server.
            Hello stub = (Hello) Naming.lookup("rmi://localhost:1099/Hello");

            String response = stub.sayHello(10, 20);
            System.out.println("Servidor enviou: " + response);
        } catch (NotBoundException e) {
            System.err.println("Nome de registro do stub está diferente do servidor: " + e.getMessage());
        } catch (MalformedURLException e) {
            System.err.println("URL mal formada: " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("Erro ao tentar contatar o registro RMI: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de I/O: " + e.getMessage());
        }


    }
}
