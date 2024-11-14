package src.Ficha11.Ex1;

import src.Ficha11.Numbers;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorImplementation extends UnicastRemoteObject implements Calculator {

    protected CalculatorImplementation() throws RemoteException {
        //chamar o construtor da UnicastRemoteObject
        super();
    }

    public static void main(String[] args) {

        try {
            //criando o registro para exportar o objeto na porta 6666
            LocateRegistry.createRegistry(6666);
            //é a implementacao que quero publicar no rmi
            CalculatorImplementation calculator = new CalculatorImplementation();

            //se existir substitui, se nao existir cria
            Naming.rebind("rmi://localhost:6666/calculator", calculator);

            //tudo ok
            System.out.println("Calculadora preparada.");

        } catch (RemoteException e) {
            System.err.println("Erro ao tentar exportar o objeto: " + e.getMessage());
        } catch (MalformedURLException e) {
            System.err.println("Erro de URL mal definida: " + e.getMessage());
        }

    }

    @Override
    public float add(Numbers numbers) throws RemoteException {
        return numbers.getNum1() + numbers.getNum2();
    }

    @Override
    public float subtract(Numbers numbers) throws RemoteException {
        return numbers.getNum1() - numbers.getNum2();
    }

    @Override
    public float multiply(Numbers numbers) throws RemoteException {
        return numbers.getNum1() * numbers.getNum2();
    }

    @Override
    public float divide(Numbers numbers) throws RemoteException {
        if (numbers.getNum2() == 0) {
            System.err.println("Erro, não pode dividir por zero.");
            return 0;
        }
        return numbers.getNum1() / numbers.getNum2();
//        return 1 / 0;
    }
}
