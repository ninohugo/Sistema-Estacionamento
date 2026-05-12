package dao;

import database.Conexao;
import model.Veiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class VeiculoDAO {

    public void salvar(Veiculo v) {

        String sql =
                "INSERT INTO veiculos " +
                        "(placa, modelo, cor, tipo) " +
                        "VALUES (?, ?, ?, ?)";

        try {

            Connection conn = Conexao.conectar();

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

            System.out.println(
                    "Erro ao salvar: " +
                            e.getMessage()
            );
        }
    }
}