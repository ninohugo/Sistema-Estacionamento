import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TesteConexao {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/estacionamento";
        String usuario = "root";
        String senha = "root123";

        try {
            Connection conexao = DriverManager.getConnection(url, usuario, senha);

            System.out.println("Conexão realizada com sucesso!");

            String sql = "INSERT INTO veiculos (placa, modelo, cor, tipo) VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, "ABC1234");
            stmt.setString(2, "Gol");
            stmt.setString(3, "Branco");
            stmt.setString(4, "carro");

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Veículo cadastrado com sucesso!");
            }

            stmt.close();
            conexao.close();

        } catch (SQLException e) {
            System.out.println("Erro ao conectar ou inserir no banco:");
            System.out.println(e.getMessage());
        }
    }
}