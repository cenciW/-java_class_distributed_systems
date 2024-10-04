package src.Ficha2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Consumer implements Runnable {
    private final Semaphore livre;
    private final Semaphore ocups;
    private final Lock mutex;

    //Interrupt Variable

    //Constructor needs to receive semaphores
    public Consumer(Semaphore livre, Semaphore ocups, Lock mutex) {
        this.livre = livre;
        this.ocups = ocups;
        this.mutex = mutex;
    }

    private void consumeItem(int item) {
        System.out.println("The thread: " + Thread.currentThread().getName() + " consumed the item: " + item);
    }

    //retirar items
    private int removeItem() {
        //that's way are blocking and unlocking each iteration.
        for (int i = 0; i < Buffer.bufferSize; i++) {
            if (Buffer.isOccupied[i]) {
                //critical zone - more than 1 alteration in same variables
                Buffer.isOccupied[i] = false;
                int item = Buffer.buffer[i];

                System.out.println("The thread: " + Thread.currentThread().getName()
                        + " removed the: " + item
                        + " item, on position: " + i + " of buffer.");

                //stop at first iteration unoccupied. - 'return or break'
                return item;
            }
        }
        //return a number out the buffer
        return Buffer.min - 1;
    }

    //signing contract with interface
    @Override
    public void run() {
        //changing the threads names to differentiate them
        Thread.currentThread().setName("CONSUMIDORA_" + Thread.currentThread().threadId());

        while (!Thread.currentThread().isInterrupted()) {

            //block before
            //run without interrupts
            //just can entry bufferSize threads.

            //!!aqui ele já reduz um aos ocupados- deixa de estar ocupado a partir de agora
            ocups.acquireUninterruptibly();

            //from here, just can entry 1 per time
            mutex.lock();

            //CRITICAL ZONE

            //filling item with a random item
            int item = removeItem();

            //libera o mutex - when ends the critical zone
            mutex.unlock();

            //END CRITICAL ZONE

            //unlock after
            //!!now has one more livre.
            livre.release();

            //consume item
            consumeItem(item);

            /*
            try {

                Thread.sleep(10000);

            } catch (InterruptedException e) {

                System.out.println("The Thread: " + Thread.currentThread().getName() + " recieved the interrupt signal");
                return;
            }*/
        }
        System.out.println("The Thread: " + Thread.currentThread().getName() + " ended");
    }
}

