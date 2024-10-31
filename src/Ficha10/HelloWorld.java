package src.Ficha10;

import java.rmi.RemoteException;

public class HelloWorld implements Hello{

    @Override
    public String sayHello(int a, int b) /*throws RemoteException*/ {
        System.out.println("O método sayHello foi executado. " + a + b);
        return "Hello, World! " + (a + b);
    }
}
