package dao;

import database.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VagaDAO {

    public boolean existeVaga(String numeroVaga) {

        String sql =
                "SELECT id FROM vagas " +
                        "WHERE numero = ?";

        try (
                Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, numeroVaga);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao verificar vaga: "
                            + e.getMessage()
            );
        }
    }

    public boolean vagaDisponivel(String numeroVaga) {

        String sql =
                "SELECT v.numero " +
                        "FROM vagas v " +
                        "WHERE v.numero = ? " +
                        "AND NOT EXISTS ( " +
                        "    SELECT 1 " +
                        "    FROM movimentacoes m " +
                        "    WHERE m.vaga_id = v.numero " +
                        "    AND m.data_saida IS NULL " +
                        ")";

        try (
                Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, numeroVaga);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao verificar disponibilidade da vaga: "
                            + e.getMessage()
            );
        }
    }

    public void listarVagasDisponiveis() {

        String sql =
                "SELECT v.numero " +
                        "FROM vagas v " +
                        "WHERE NOT EXISTS ( " +
                        "    SELECT 1 " +
                        "    FROM movimentacoes m " +
                        "    WHERE m.vaga_id = v.numero " +
                        "    AND m.data_saida IS NULL " +
                        ") " +
                        "ORDER BY v.numero";

        try (
                Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            System.out.println("\n=== VAGAS DISPONÍVEIS ===");

            boolean encontrou = false;

            while (rs.next()) {
                encontrou = true;
                System.out.println(rs.getString("numero"));
            }

            if (!encontrou) {
                System.out.println("Nenhuma vaga disponível.");
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao listar vagas disponíveis: "
                            + e.getMessage()
            );
        }
    }

    public void quantidadeVagasDisponiveis() {

        String sql =
                "SELECT " +
                        "COUNT(*) AS total_livres, " +
                        "(SELECT COUNT(*) FROM vagas) AS total_vagas " +
                        "FROM vagas v " +
                        "WHERE NOT EXISTS ( " +
                        "    SELECT 1 " +
                        "    FROM movimentacoes m " +
                        "    WHERE m.vaga_id = v.numero " +
                        "    AND m.data_saida IS NULL " +
                        ")";

        try (
                Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            if (rs.next()) {

                int livres = rs.getInt("total_livres");
                int total = rs.getInt("total_vagas");

                System.out.println("Vagas livres: " + livres + "/" + total);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao contar vagas disponíveis: "
                            + e.getMessage()
            );
        }
    }
}