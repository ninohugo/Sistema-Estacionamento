package dao;

import database.Conexao;
import model.Veiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VeiculoDAO {

    // SALVAR VEÍCULO
    public void salvar(Veiculo v) {

        String sql =
                "INSERT INTO veiculos " +
                        "(placa, modelo, cor, tipo) " +
                        "VALUES (?, ?, ?, ?)";

        try {

            Connection conn =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setString(1, v.getPlaca());
            stmt.setString(2, v.getModelo());
            stmt.setString(3, v.getCor());

            stmt.setString(
                    4,
                    v.getClass().getSimpleName()
            );

            stmt.executeUpdate();

            System.out.println(
                    "Veículo salvo no banco!"
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao salvar: "
                            + e.getMessage()
            );
        }
    }

    // BUSCAR ID POR PLACA
    public int buscarIdPorPlaca(String placa) {

        String sql =
                "SELECT id FROM veiculos " +
                        "WHERE placa = ?";

        try {

            Connection conn =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setString(1, placa);

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                return rs.getInt("id");
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao buscar veículo: "
                            + e.getMessage()
            );
        }

        throw new RuntimeException(
                "Veículo não encontrado!"
        );
    }
}