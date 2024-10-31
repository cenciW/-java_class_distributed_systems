package src.Ficha10.Server;

import src.Ficha10.Hello;
import src.Ficha10.HelloWorld;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) {
        //criar a interface
        //implementar a interface
        //instanciar e exportar o objeto
        try {
            HelloWorld helloWorldObj = new HelloWorld();
            Hello stub = (Hello) UnicastRemoteObject.exportObject(helloWorldObj, 0);
            //registrar o server RMI
            LocateRegistry.createRegistry(1099);

            //nome que dou ao stub para ligar la no cliente
            Naming.rebind("Hello", stub);

            System.out.println("Servidor ligado.");
        } catch (RemoteException e) {
            System.err.println("Erro ao tentar exportar o objeto: " + e.getMessage());
        } catch (MalformedURLException e) {
            System.err.println("URL mal definido: " + e.getMessage());
        }

    }
}
