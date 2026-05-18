package dao;

import database.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MovimentacaoDAO {

    // REGISTRAR ENTRADA
    public void registrarEntrada(
            int veiculoId,
            String vaga_id
    ) {

        String sql =
                "INSERT INTO movimentacoes " +
                        "(veiculo_id, vaga_id, data_entrada) " +
                        "VALUES (?, ?, NOW())";

        try {

            Connection conn =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setInt(1, veiculoId);
            stmt.setString(2, vaga_id);

            stmt.executeUpdate();

            System.out.println(
                    "Entrada salva no banco!"
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro entrada: "
                            + e.getMessage()
            );
        }
    }

    // REGISTRAR SAÍDA
    public void registrarSaida(
            int veiculoId,
            double valorPago
    ) {

        String sql =
                "UPDATE movimentacoes " +
                        "SET data_saida = NOW(), " +
                        "valor_pago = ? " +
                        "WHERE veiculo_id = ? " +
                        "AND data_saida IS NULL";

        try {

            Connection conn =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setDouble(1, valorPago);
            stmt.setInt(2, veiculoId);

            stmt.executeUpdate();

            System.out.println(
                    "Saída salva no banco!"
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro saída: "
                            + e.getMessage()
            );
        }
    }
}