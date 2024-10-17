package src.Ficha6;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class Numbers implements Serializable {
    int min, max, sizeArray;
    private int [] array;

    public Numbers(int min, int max, int sizeArray) {
        this.min = min;
        this.max = max;
        this.sizeArray = sizeArray;

        array = new int[sizeArray];
        Random rand = new Random();

        for (int i = 0; i < sizeArray; i++) {
            array[i] = rand.nextInt(min, max + 1);
//            System.out.println("["+ i +"]: "+ array[i]);
        }
    }

    public void multiply(int mult){
        for(int i= 0; i< sizeArray; i++){
            array[i] *= mult;
        }
        min *= mult;
        max *= mult;

        //if multiply by a negative number
        if(min > max){
            int aux = min;
            min = max;
            max = aux;
        }
    }

    @Override
    public String toString() {

        String str =
                "\nMínimo: " + min +
                "\nMáximo: " + max +
                "\nTamanho do Array: " + sizeArray +
                "\nO array: " + Arrays.toString(array) +"\n";

        return str;
    }
}
