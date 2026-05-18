package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Movimentacao {

    private Veiculo veiculo;

    private Vaga vaga;

    private LocalDateTime dataEntrada;

    private LocalDateTime dataSaida;

    private double valorPago;

    // CONSTRUTOR
    public Movimentacao(
            Veiculo veiculo,
            Vaga vaga
    ) {

        this.veiculo = veiculo;
        this.vaga = vaga;

        this.dataEntrada =
                LocalDateTime.now();
    }

    // REGISTRAR SAÍDA
    public void registrarSaida() {

        this.dataSaida =
                LocalDateTime.now();

        calcularPagamento();
    }

    // CALCULAR PAGAMENTO
    private void calcularPagamento() {

        long horas = Duration.between(
                dataEntrada,
                dataSaida
        ).toHours();

        // cobrança mínima de 1 hora
        if (horas == 0) {
            horas = 1;
        }

        valorPago = horas * 5.0;
    }

    // VERIFICA SE A MOVIMENTAÇÃO FINALIZOU
    public boolean finalizada() {

        return dataSaida != null;
    }

    // RESUMO DA MOVIMENTAÇÃO
    public String resumo() {

        return
                "Placa: "
                        + veiculo.getPlaca()

                        + " | Modelo: "
                        + veiculo.getModelo()

                        + " | Vaga: "
                        + vaga.getNumero()

                        + " | Entrada: "
                        + dataEntrada

                        + " | Saída: "
                        + dataSaida

                        + " | Valor: R$ "
                        + valorPago;
    }

    // GETTERS
    public Veiculo getVeiculo() {
        return veiculo;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public double getValorPago() {
        return valorPago;
    }

    @Override
    public String toString() {

        return resumo();
    }
}