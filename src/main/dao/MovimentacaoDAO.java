package dao;

import database.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MovimentacaoDAO {

    // REGISTRAR ENTRADA
    public void registrarEntrada(
            String placa,
            String vaga
    ) {

        String sql =
                "INSERT INTO movimentacoes " +
                        "(placa_veiculo, vaga, data_entrada) " +
                        "VALUES (?, ?, NOW())";

        try {

            Connection conn =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setString(1, placa);
            stmt.setString(2, vaga);

            stmt.executeUpdate();

            System.out.println(
                    "Entrada salva no banco!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro entrada: "
                            + e.getMessage()
            );
        }
    }

    // REGISTRAR SAÍDA
    public void registrarSaida(
            String placa,
            double valorPago
    ) {

        String sql =
                "UPDATE movimentacoes " +
                        "SET data_saida = NOW(), " +
                        "valor_pago = ? " +
                        "WHERE placa_veiculo = ? " +
                        "AND data_saida IS NULL";

        try {

            Connection conn =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setDouble(1, valorPago);
            stmt.setString(2, placa);

            stmt.executeUpdate();

            System.out.println(
                    "Saída salva no banco!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro saída: "
                            + e.getMessage()
            );
        }
    }
}