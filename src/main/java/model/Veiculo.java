package model;

public abstract class Veiculo {

    protected String placa;
    protected String modelo;
    protected String cor;

    public Veiculo(String placa, String modelo, String cor) {
        this.placa = placa;
        this.modelo = modelo;
        this.cor = cor;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public String getCor() {
        return cor;
    }

    public abstract double calcularValor(double horas);

    protected double calcularBase(double horas) {

        if (horas <= 1) {
            return 5.0;
        }

        return 5.0 + (Math.ceil(horas - 1) * 3.0);
    }
}