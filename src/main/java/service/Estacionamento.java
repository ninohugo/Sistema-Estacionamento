package service;

import model.*;

import java.util.ArrayList;
import java.util.List;

import dao.VeiculoDAO;
import dao.MovimentacaoDAO;

public class Estacionamento {

    private VeiculoDAO veiculoDAO = new VeiculoDAO();
    private MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
    private List<Veiculo> veiculos = new ArrayList<>();
    private List<Vaga> vagas = new ArrayList<>();
    private List<Movimentacao> movimentacoes = new ArrayList<>();

    public Estacionamento() {

        vagas.add(new Vaga("Vaga 01"));
        vagas.add(new Vaga("Vaga 02"));
        vagas.add(new Vaga("Vaga 03"));
        vagas.add(new Vaga("Vaga 04"));
        vagas.add(new Vaga("Vaga 05"));
    }

    // CADASTRAR VEÍCULO
    public void cadastrarVeiculo(Veiculo v) {

        veiculos.add(v);

        veiculoDAO.salvar(v);

        System.out.println(
                "Veículo cadastrado!"
        );
    }

    // REGISTRAR ENTRADA
    public void registrarEntrada(
            String placa,
            String numeroVaga
    ) {

        Veiculo veiculo = null;

        for (Veiculo v : veiculos) {

            if (v.getPlaca().equalsIgnoreCase(placa)) {

                veiculo = v;
                break;
            }
        }

        if (veiculo == null) {

            throw new RuntimeException(
                    "Veículo não encontrado!"
            );
        }

        Vaga vagaEscolhida = null;

        for (Vaga vaga : vagas) {

            if (
                    vaga.getNumero()
                            .equalsIgnoreCase(numeroVaga)
                            && !vaga.isOcupada()
            ) {

                vagaEscolhida = vaga;
                break;
            }
        }

        if (vagaEscolhida == null) {

            throw new RuntimeException(
                    "Vaga indisponível!"
            );
        }

        vagaEscolhida.ocupar();

        Movimentacao mov =
                new Movimentacao(
                        veiculo,
                        vagaEscolhida
                );

        movimentacoes.add(mov);

        int veiculoId =
                veiculoDAO.buscarIdPorPlaca(
                        placa
                );

        movimentacaoDAO.registrarEntrada(
                veiculoId,
                vagaEscolhida.getNumero()
        );

        System.out.println(
                "Entrada registrada!"
        );
    }

    // REGISTRAR SAÍDA
    public void registrarSaida(String placa) {

        for (Movimentacao mov : movimentacoes) {

            if (
                    mov.getVeiculo()
                            .getPlaca()
                            .equalsIgnoreCase(placa)

                            &&

                            mov.getDataSaida() == null
            ) {

                mov.registrarSaida();

                mov.getVaga().liberar();

                int veiculoId =
                        veiculoDAO.buscarIdPorPlaca(
                                placa
                        );

                movimentacaoDAO.registrarSaida(
                        veiculoId,
                        mov.getValorPago()
                );

                System.out.println(
                        "Saída registrada!"
                );

                return;
            }
        }

        throw new RuntimeException(
                "Veículo não encontrado!"
        );
    }

    // LISTAR VAGAS DISPONÍVEIS
    public void listarVagasDisponiveis() {

        System.out.println("\n=== VAGAS DISPONÍVEIS ===");

        vagas.stream()
                .filter(v -> !v.isOcupada())
                .forEach(v -> System.out.println(v.getNumero()));
    }

    // MOSTRAR QUANTIDADE
    public void quantidadeVagasDisponiveis() {

        long livres = vagas.stream()
                .filter(v -> !v.isOcupada())
                .count();

        System.out.println("Vagas livres: " + livres + "/5");
    }

    // LISTAR ESTACIONADOS
    public void listarEstacionados() {

        System.out.println("\n=== VEÍCULOS ESTACIONADOS ===");

        movimentacoes.stream()
                .filter(m -> !m.finalizada())
                .forEach(m ->
                        System.out.println(
                                m.getVeiculo().getPlaca()
                                        + " -> "
                                        + m.getVaga().getNumero()));
    }

    // HISTÓRICO
    public void historico() {

        System.out.println("\n=== HISTÓRICO ===");

        movimentacoes.forEach(m ->
                System.out.println(m.resumo()));
    }
    // BUSCAR VEÍCULO
    private Veiculo buscarVeiculo(String placa) {

        return veiculos.stream()
                .filter(v -> v.getPlaca().equalsIgnoreCase(placa))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Veículo não encontrado!"));
    }

    // BUSCAR VAGA
    private Vaga buscarVaga(String numero) {

        return vagas.stream()
                .filter(v -> v.getNumero().equalsIgnoreCase(numero))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Vaga inexistente!"));
    }

    // VERIFICAR ESTACIONADO
    private boolean estaEstacionado(String placa) {

        return movimentacoes.stream()
                .anyMatch(m ->
                        m.getVeiculo().getPlaca().equalsIgnoreCase(placa)
                                && !m.finalizada());
    }
}