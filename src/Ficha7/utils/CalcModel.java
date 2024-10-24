package src.Ficha7.utils;

import java.io.Serializable;

public class CalcModel implements Serializable {
    //soma = +
    //subtração = -
    //multiplicação = * ou X ou x
    //divisao = / ou :
    private String operador;
    private double resultado, operando1, operando2;

    public CalcModel(double operando1, double operando2, String operador) {
            this.operando1 = operando1;
            this.operando2 = operando2;
            this.operador = operador;
//        if(validateInputs(operando1, operando2, operador)){
//        }else{
//            throw new IllegalArgumentException("Valor invalido");
//        }
    }

//    public boolean validateInputs(double operando1, double operando2, String operador){
//
//        if(operador.contains("/") || operador.contains(":")){
//            return operando2 != 0;
//        }
//        return true;
//    }

    public CalcModel(){}


    public double getResultado() {
        return resultado;
    }
    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public String getOperador() {
        return operador;
    }
    public void setOperador(String operador) {
        this.operador = operador;
    }

    public double getOperando1() {
        return operando1;
    }
    public void setOperando1(double operando1) {
        this.operando1 = operando1;
    }

    public double getOperando2() {
        return operando2;
    }
    public void setOperando2(double operando2) {
        this.operando2 = operando2;
    }

    public double sum(double operando1, double operando2) {
        return operando1 + operando2;
    }

    public double difference(double operando1, double operando2) {
        return operando1 - operando2;
    }

    public double multiply(double operando1, double operando2) {
        return operando1 * operando2;
    }

    public double divide(double operando1, double operando2) {

        if(operando2 == 0){
            throw new ArithmeticException();
        } else{
            return operando1 / operando2;
        }

    }

    @Override
    public String toString() {
        return operando1 + " " + operador + " " + operando2;
    }
}
