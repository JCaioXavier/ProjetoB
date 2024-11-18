package BancoDeDados;

import Entidades.Funcionario;
import Entidades.Ingrediente;
import Entidades.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class IngredienteDAO {
    public static void ingredientesDAO() {
        String sql = "SELECT id_ingrediente, nome, estoque FROM piramide.ingredientes ORDER BY id_ingrediente ASC";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Id_Ingrediente: " + rs.getString("id_ingrediente") +
                        "\nNome: " + rs.getString("nome") +
                        "\nEstoque: " + rs.getInt("estoque"));
            }
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int pegarIngredienteDAO() {
        Scanner scan = new Scanner(System.in);
        boolean ingredienteExistente = false;
        int idIngrediente;

        do {
            idIngrediente = scan.nextInt();

            String sql = "SELECT nome, estoque FROM piramide.ingredientes WHERE id_ingrediente = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idIngrediente);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    ingredienteExistente = true;
                } else {
                    System.out.println("Ingrediente não encontrado! Digite um ID válido.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } while (!ingredienteExistente);

        return idIngrediente;
    }

    public static void inserirIngredienteDAO(Ingrediente novo) {

        String nome = novo.nome;
        int estoque = novo.estoque;

        String sql = "INSERT INTO piramide.ingredientes (nome, estoque) VALUES (?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, estoque);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Ingrediente inserido com sucesso!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void checkadordeIngredientesDAO(Ingrediente novoIngrediente ) {
        Scanner scanner = new Scanner(System.in);
        boolean ingredienteExistente = true;

        do {

            System.out.print("Digite o nome do ingrediente: ");
            novoIngrediente.nome = scanner.nextLine().trim();

            if (novoIngrediente.nome.isEmpty()) {
                System.out.println("Ingrediente não pode ser vazio ou apenas espaços! Digite novamente.");
                continue;
            }
            String sql = "SELECT nome FROM piramide.ingredientes WHERE nome = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, novoIngrediente.nome);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Nome já cadastrado! Digite novamente.");
                } else {
                    ingredienteExistente = false;
                }

            } catch (SQLException e) {
                System.out.println("Erro ao verificar Nome: " + e.getMessage());
            }

        } while (ingredienteExistente);
    }
    public static int tamanhoIngredienteDAO() {
        String sql = "SELECT COUNT(*) as total FROM piramide.ingredientes";
        int totalIngredientes = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                totalIngredientes = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return totalIngredientes;
    }

    public static void removerIngredienteDAO(int id) {

        String sql = "DELETE FROM piramide.ingredientes WHERE id_ingrediente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateIngredienteDAO(Ingrediente update) {
        int idi = update.id_ingrediente;

        String nome = update.nome;
        int estoque = update.estoque;

        String sql = "UPDATE piramide.ingredientes SET nome = ?, estoque = ? WHERE id_ingrediente = " + idi;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setInt(2, estoque);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Ingrediente editado com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
