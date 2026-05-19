package service;

import dao.MovimentacaoDAO;
import dao.VagaDAO;
import dao.VeiculoDAO;
import model.Veiculo;

public class Estacionamento {

    private VeiculoDAO veiculoDAO = new VeiculoDAO();
    private VagaDAO vagaDAO = new VagaDAO();
    private MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

    public void cadastrarVeiculo(Veiculo v) {

        veiculoDAO.salvar(v);

        System.out.println("Veículo cadastrado!");
    }

    public void registrarEntrada(
            String placa,
            String numeroVaga
    ) {

        int veiculoId =
                veiculoDAO.buscarIdPorPlaca(placa);

        if (!vagaDAO.existeVaga(numeroVaga)) {
            throw new RuntimeException("Vaga inexistente!");
        }

        if (!vagaDAO.vagaDisponivel(numeroVaga)) {
            throw new RuntimeException("Vaga indisponível!");
        }

        movimentacaoDAO.registrarEntrada(
                veiculoId,
                numeroVaga
        );

        System.out.println("Entrada registrada!");
    }

    public void registrarSaida(String placa) {

        movimentacaoDAO.registrarSaidaPorPlaca(placa);

        System.out.println("Saída registrada!");
    }

    public void listarVagasDisponiveis() {

        vagaDAO.listarVagasDisponiveis();
    }

    public void quantidadeVagasDisponiveis() {

        vagaDAO.quantidadeVagasDisponiveis();
    }

    public void listarEstacionados() {

        movimentacaoDAO.listarEstacionados();
    }

    public void historico() {

        movimentacaoDAO.historico();
    }
}