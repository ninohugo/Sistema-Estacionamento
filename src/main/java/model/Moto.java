package model;

public class Moto extends Veiculo {

    public Moto(String placa, String modelo, String cor) {
        super(placa, modelo, cor);
    }

    @Override
    public double calcularValor(double horas) {
        return calcularBase(horas) * 0.5;
    }
}