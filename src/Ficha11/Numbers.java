package src.Ficha11;

import java.io.Serializable;

public class Numbers implements Serializable {
    //add sub div multi between 2 numbers
    private float num1, num2;

    public Numbers(float num1, float num2) {
        this.num1 = num1;
        this.num2 = num2;
    }
    public Numbers(){}

    public float getNum1() {
        return num1;
    }

    public void setNum1(float num1) {
        this.num1 = num1;
    }

    public float getNum2() {
        return num2;
    }
    public void setNum2(float num2) {
        this.num2 = num2;
    }
}
