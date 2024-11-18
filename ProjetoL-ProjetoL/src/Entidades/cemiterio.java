package Entidades;

import java.sql.*;

public class cemiterio {
    /*
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "5432";
    private static Connection conn;

    public static void conectar() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexão bem-sucedida!");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }

    public void inserirFuncionario(String nome, double preco, int quantidade) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = Main.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setDouble(2, preco);
            pstmt.setInt(3, quantidade);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Produto inserido com sucesso!");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao inserir produto: " + e.getMessage());
        }
    }

    public static void funcionarios() {
        String sql = "SELECT nome, usuario, senha FROM esquema.funcionarios ORDER BY nome ASC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("Funcionários em ordem crescente pelo nome:");
            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Nome: " + rs.getString("nome") + "\nUsuário: " + rs.getString("usuario") + "\nSenha: " + rs.getString("senha"));
            }
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println("Erro ao listar funcionários em ordem: " + e.getMessage());
        }
    }

     */
}
