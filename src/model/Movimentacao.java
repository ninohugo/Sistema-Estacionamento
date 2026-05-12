package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Movimentacao {

    private Veiculo veiculo;
    private Vaga vaga;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private double valorPago;

    public Movimentacao(Veiculo veiculo, Vaga vaga) {
        this.veiculo = veiculo;
        this.vaga = vaga;
        this.entrada = LocalDateTime.now();
    }

    public void registrarSaida() {
        this.saida = LocalDateTime.now();

        double horas =
                Duration.between(entrada, saida).toMinutes() / 60.0;

        valorPago = veiculo.calcularValor(horas);

        vaga.liberar();
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public boolean finalizada() {
        return saida != null;
    }

    public double getValorPago() {
        return valorPago;
    }

    public String resumo() {
        return "Placa: " + veiculo.getPlaca() +
                " | Vaga: " + vaga.getNumero() +
                " | Entrada: " + entrada +
                " | Saída: " + saida +
                " | Valor: R$ " + valorPago;
    }
}