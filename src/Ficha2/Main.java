package src.Ficha2;

import src.utils.InputValidation;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        //starting reader
        Scanner sc = new Scanner(System.in);

        //number of elements in array
        Semaphore livre;
        //Started with 0
        Semaphore  ocups = new Semaphore(0);
        //Started with 1
        Lock mutex = new ReentrantLock();

        Buffer.bufferSize = InputValidation.validateIntGT0(sc, "Introduza o tamanho do buffer a criar: ");
        Buffer.min = InputValidation.validateInt(sc, "Introduza o valor mínimo a colocar no array: ");
        //read the max input, and validate if it's greater than min
        Buffer.max = InputValidation.validateIntGTN(sc, "Introduza o valor máximo a colocar no array: ", Buffer.min);

        //initializing the semaphores freedoms with the bufferSize
        livre = new Semaphore(Buffer.bufferSize);
        Buffer.buffer = new int[Buffer.bufferSize];
        Buffer.isOccupied = new boolean[Buffer.bufferSize];

        //reading the number of threads producers
        //put inside the buffer
        int nProducerThreads = InputValidation.validateIntGT0(sc,"Introduza o número de threads produtoras a carregar: ");

        //remove from buffer
        int nConsumerThreads = InputValidation.validateIntGT0(sc,"Introduza o número de threads consumidora a carregar: ");

        //creating the executor
        //Management of threads (escalonamento)
        ExecutorService executor = Executors.newFixedThreadPool(nProducerThreads + nConsumerThreads);

        //producer threads
        for (int i = 0; i < nProducerThreads; i++) {
            Producer producer = new Producer(livre, ocups, mutex);
            executor.execute(producer);
        }

        //consumers threads
        for (int i = 0; i < nConsumerThreads; i++) {
            Consumer consumer = new Consumer(livre, ocups, mutex);
            executor.execute(consumer);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Erro de interrpução: " + e.getMessage());
        }

        executor.shutdownNow();
        System.out.println("Finalizing threads - Main ended");
        sc.close();
    }
}