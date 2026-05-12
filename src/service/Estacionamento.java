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

        boolean existe = veiculos.stream()
                .anyMatch(x ->
                        x.getPlaca()
                                .equalsIgnoreCase(v.getPlaca()));

        if (existe) {

            throw new RuntimeException(
                    "Placa já cadastrada!"
            );
        }

        veiculos.add(v);

        veiculoDAO.salvar(v);

        System.out.println(
                "Veículo cadastrado!"
        );
    }

    // REGISTRAR ENTRADA
    public void registrarEntrada(String placa, String numeroVaga) {

        if (estaEstacionado(placa)) {
            throw new RuntimeException("Veículo já estacionado!");
        }

        Veiculo veiculo = buscarVeiculo(placa);

        Vaga vaga = buscarVaga(numeroVaga);

        if (vaga.isOcupada()) {
            throw new RuntimeException("Vaga já está ocupada!");
        }

        vaga.ocupar();

        Movimentacao mov =
                new Movimentacao(
                        veiculo,
                        vaga
                );

        movimentacoes.add(mov);

        movimentacaoDAO.registrarEntrada(
                placa,
                vaga.getNumero()
        );

        System.out.println("Entrada registrada com sucesso!");
    }

    // REGISTRAR SAÍDA
    public void registrarSaida(String placa) {

        Movimentacao mov = movimentacoes.stream()
                .filter(m ->
                        m.getVeiculo().getPlaca().equalsIgnoreCase(placa)
                                && !m.finalizada())
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Veículo não está estacionado!"));

        mov.registrarSaida();
        movimentacaoDAO.registrarSaida(placa, mov.getValorPago()
        );

        System.out.println("Saída registrada!");
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