package model;

public class Carro extends Veiculo {

    public Carro(String placa, String modelo, String cor) {
        super(placa, modelo, cor);
    }

    @Override
    public double calcularValor(double horas) {
        return calcularBase(horas);
    }
}