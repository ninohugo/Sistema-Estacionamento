package dao;

import database.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

public class MovimentacaoDAO {

    public void registrarEntrada(
            int veiculoId,
            String numeroVaga
    ) {

        if (veiculoEstaEstacionado(veiculoId)) {
            throw new RuntimeException("Este veículo já está estacionado!");
        }

        String sql =
                "INSERT INTO movimentacoes " +
                        "(veiculo_id, vaga_id, data_entrada) " +
                        "VALUES (?, ?, NOW())";

        try (
                Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, veiculoId);
            stmt.setString(2, numeroVaga);

            stmt.executeUpdate();

            System.out.println("Entrada salva no banco!");

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao registrar entrada: "
                            + e.getMessage()
            );
        }
    }

    public void registrarSaidaPorPlaca(String placa) {

        String sqlBusca =
                "SELECT " +
                        "m.id, " +
                        "m.data_entrada, " +
                        "v.tipo " +
                        "FROM movimentacoes m " +
                        "INNER JOIN veiculos v ON v.id = m.veiculo_id " +
                        "WHERE v.placa = ? " +
                        "AND m.data_saida IS NULL";

        try (
                Connection conn = Conexao.conectar();
                PreparedStatement stmtBusca = conn.prepareStatement(sqlBusca)
        ) {

            stmtBusca.setString(1, placa);

            ResultSet rs = stmtBusca.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Não existe entrada aberta para este veículo!");
            }

            int movimentacaoId = rs.getInt("id");

            Timestamp entradaTimestamp = rs.getTimestamp("data_entrada");
            LocalDateTime dataEntrada = entradaTimestamp.toLocalDateTime();
            LocalDateTime dataSaida = LocalDateTime.now();

            String tipo = rs.getString("tipo");

            double valorPago = calcularValor(
                    dataEntrada,
                    dataSaida,
                    tipo
            );

            atualizarSaida(
                    movimentacaoId,
                    valorPago
            );

            System.out.println("Saída salva no banco!");
            System.out.println("Valor pago: R$ " + String.format("%.2f", valorPago));

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao registrar saída: "
                            + e.getMessage()
            );
        }
    }

    private void atualizarSaida(
            int movimentacaoId,
            double valorPago
    ) {

        String sql =
                "UPDATE movimentacoes " +
                        "SET data_saida = NOW(), " +
                        "valor_pago = ? " +
                        "WHERE id = ?";

        try (
                Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setDouble(1, valorPago);
            stmt.setInt(2, movimentacaoId);

            stmt.executeUpdate();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao atualizar saída: "
                            + e.getMessage()
            );
        }
    }

    public boolean veiculoEstaEstacionado(int veiculoId) {

        String sql =
                "SELECT id FROM movimentacoes " +
                        "WHERE veiculo_id = ? " +
                        "AND data_saida IS NULL";

        try (
                Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, veiculoId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao verificar veículo estacionado: "
                            + e.getMessage()
            );
        }
    }

    public void listarEstacionados() {

        String sql =
                "SELECT " +
                        "v.placa, " +
                        "v.modelo, " +
                        "m.vaga_id, " +
                        "m.data_entrada " +
                        "FROM movimentacoes m " +
                        "INNER JOIN veiculos v ON v.id = m.veiculo_id " +
                        "WHERE m.data_saida IS NULL " +
                        "ORDER BY m.data_entrada";

        try (
                Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            System.out.println("\n=== VEÍCULOS ESTACIONADOS ===");

            boolean encontrou = false;

            while (rs.next()) {

                encontrou = true;

                System.out.println(
                        "Placa: " + rs.getString("placa")
                                + " | Modelo: " + rs.getString("modelo")
                                + " | Vaga: " + rs.getString("vaga_id")
                                + " | Entrada: " + rs.getTimestamp("data_entrada")
                );
            }

            if (!encontrou) {
                System.out.println("Nenhum veículo estacionado.");
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao listar estacionados: "
                            + e.getMessage()
            );
        }
    }

    public void historico() {

        String sql =
                "SELECT " +
                        "v.placa, " +
                        "v.modelo, " +
                        "v.tipo, " +
                        "m.vaga_id, " +
                        "m.data_entrada, " +
                        "m.data_saida, " +
                        "m.valor_pago " +
                        "FROM movimentacoes m " +
                        "INNER JOIN veiculos v ON v.id = m.veiculo_id " +
                        "ORDER BY m.data_entrada DESC";

        try (
                Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            System.out.println("\n=== HISTÓRICO ===");

            boolean encontrou = false;

            while (rs.next()) {

                encontrou = true;

                System.out.println(
                        "Placa: " + rs.getString("placa")
                                + " | Modelo: " + rs.getString("modelo")
                                + " | Tipo: " + rs.getString("tipo")
                                + " | Vaga: " + rs.getString("vaga_id")
                                + " | Entrada: " + rs.getTimestamp("data_entrada")
                                + " | Saída: " + rs.getTimestamp("data_saida")
                                + " | Valor: R$ " + rs.getDouble("valor_pago")
                );
            }

            if (!encontrou) {
                System.out.println("Nenhuma movimentação encontrada.");
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao listar histórico: "
                            + e.getMessage()
            );
        }
    }

    private double calcularValor(
            LocalDateTime dataEntrada,
            LocalDateTime dataSaida,
            String tipo
    ) {

        long minutos = Duration.between(
                dataEntrada,
                dataSaida
        ).toMinutes();

        double horas = Math.ceil(minutos / 60.0);

        if (horas < 1) {
            horas = 1;
        }

        double valorBase;

        if (horas <= 1) {
            valorBase = 5.0;
        } else {
            valorBase = 5.0 + (Math.ceil(horas - 1) * 3.0);
        }

        if (tipo.equalsIgnoreCase("Moto")) {
            return valorBase * 0.5;
        }

        if (tipo.equalsIgnoreCase("Caminhonete")) {
            return valorBase * 1.5;
        }

        return valorBase;
    }
}