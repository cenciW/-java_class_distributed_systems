package src.Ficha11.Ex1;

import src.Ficha11.Numbers;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
    public float add(Numbers numbers) throws RemoteException;
    public float subtract(Numbers numbers) throws RemoteException;
    public float multiply(Numbers numbers) throws RemoteException;
    public float divide(Numbers numbers) throws RemoteException;
}
