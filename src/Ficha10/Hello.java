package src.Ficha10;

//import do RMI
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {
    String sayHello(int a, int b) throws RemoteException, IOException;
}
