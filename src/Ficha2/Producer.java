package src.Ficha2;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Producer implements Runnable {
    private final Semaphore livre;
    private final Semaphore ocups;
    private final Lock mutex;

    //Interrupt Variable

    //Constructor needs to receive semaphores
    public Producer(Semaphore livre, Semaphore ocups, Lock mutex) {
        this.livre = livre;
        this.ocups = ocups;
        this.mutex = mutex;
    }

    private int produceItem() {
        Random rand = new Random();
        int item = rand.nextInt((Buffer.max - Buffer.min) + 1) + Buffer.min;
        System.out.println("The thread: " + Thread.currentThread().getName() + " produced: " + item);
        return item;
    }

    private void depositItem(int item) {
        for (int i = 0; i < Buffer.bufferSize; i++) {
            if (!Buffer.isOccupied[i]) {
                //found empty position (not occupied)
                Buffer.buffer[i] = item;
                //Write as occupied
                Buffer.isOccupied[i] = true;

                System.out.println("The thread: " + Thread.currentThread().getName()
                        + " deposited the:  " + item
                        + " on position: " + i + " of buffer.");

                //stop at first iteration unoccupied. - 'return or break'
                return;
            }
        }
    }

    //signing contract with interface
    @Override
    public void run() {
        //changing the threads names to differentiate them
        Thread.currentThread().setName("PRODUTORA_" + Thread.currentThread().threadId());
        while (!Thread.currentThread().isInterrupted()) {


            //filling item with a random item
            int item = produceItem();

            //remove 1 from free threads vector.
            livre.acquireUninterruptibly();

            mutex.lock();
            //critical zone

            depositItem(item);

            //end critical zone
            mutex.unlock();

            //plus on semaphore occupied
            ocups.release();


            /* if comments, we can see the while loop with thread flag working
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                System.out.println("The Thread: " + Thread.currentThread().getName() + " received the interrupt signal");
                return;
            }*/
        }
        System.out.println("The Thread: " + Thread.currentThread().getName() + " ended");
    }
}
