package BancoDeDados;

import Entidades.Funcionario;
import Entidades.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProdutoDAO {

    public static void produtosDAO() {
        String sql = "SELECT id_produto, nome, estoque, preco FROM piramide.produtos ORDER BY id_produto ASC";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Id_produto: " + rs.getString("id_produto") +
                        "\nNome: " + rs.getString("nome") +
                        "\nEstoque: " + rs.getInt("estoque") +
                        "\nPreço: " + rs.getDouble("preco"));
            }
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static int pegarProdutoDAO() {
        Scanner scan = new Scanner(System.in);
        boolean produtoExistente = false;
        int idProduto = -1;

        do {
            System.out.print("Digite o ID do produto: ");
            idProduto = scan.nextInt();

            String sql = "SELECT nome, estoque, preco FROM piramide.produtos WHERE id_produto = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idProduto); // Define apenas o primeiro parâmetro
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    produtoExistente = true;
                } else {
                    System.out.println("Produto não encontrado! Digite um ID válido.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } while (!produtoExistente);

        return idProduto;
    }

    public static void inserirProdutoDAO(Produto novo) {

        String nome = novo.nome;
        int estoque = novo.estoque;
        double preco = novo.preco;

        String sql = "INSERT INTO piramide.produtos (nome, estoque, preco) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, estoque);
            pstmt.setDouble(3, preco);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Produto inserido com sucesso!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void checkadordeProdutosDAO(Produto novoProduto ) {
        Scanner scanner = new Scanner(System.in);
        boolean produtoExistente = true;

        do {

            System.out.print("Digite o nome do produto: ");
            novoProduto.nome = scanner.nextLine().trim();

            if (novoProduto.nome.isEmpty()) {
                System.out.println("Produto não pode ser vazio ou apenas espaços! Digite novamente.");
                continue;
            }
            String sql = "SELECT nome FROM piramide.produtos WHERE nome = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, novoProduto.nome);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Nome já cadastrado! Digite novamente.");
                } else {
                    produtoExistente = false;
                }

            } catch (SQLException e) {
                System.out.println("Erro ao verificar Nome: " + e.getMessage());
            }

        } while (produtoExistente);
    }

    public static void updateProdutosDAO(Produto update) {

        int idp = update.id_produto;

        String nome = update.nome;
        int estoque = update.estoque;
        double preco = update.preco;

        System.out.println(update);

        String sql = "UPDATE piramide.produtos SET nome = ?, estoque = ?, preco = ? WHERE id_produto = " + idp;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, estoque);
            pstmt.setDouble(3, preco);


            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Produto editado com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public static void removerProdutoDAO(int id) {

        String sql = "DELETE FROM piramide.produtos WHERE id_produto = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate(); // Executa a consulta DELETE

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int tamanhoProdutoDAO() {
        String sql = "SELECT COUNT(*) as total FROM piramide.produtos";
        int totalProdutos = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                totalProdutos = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return totalProdutos;
    }

}