package BancoDeDados;

import Entidades.DetalhePedido;
import Entidades.Pedido;
import static BancoDeDados.ProdutoDAO.*;
import static Util.RevisarOpcao.simOuNao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DetalhePedidoDAO {
    public static void listaPedidosDAO(int idPedido) {
        String sql = "SELECT dp.id_detalhe_pedido, p2.nome_produto, dp.quantidade, p2.preco " +
                "FROM piramide.clientes c " +
                "JOIN piramide.pedidos p ON p.id_cliente = c.id_cliente " +
                "JOIN piramide.detalhes_pedidos dp ON dp.id_pedido = p.id_pedido " +
                "JOIN piramide.produtos p2 ON p2.id_produto = dp.id_produto " +
                "WHERE p.id_pedido = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Detalhes do Pedido:");

                while (rs.next()) {
                    System.out.println("===========================");
                    System.out.println("Produto: " + rs.getString("nome_produto"));
                    System.out.println("Quantidade: " + rs.getInt("quantidade"));
                    System.out.println("Preço: R$ " + rs.getDouble("preco"));
                }

                System.out.println("===========================");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar pedidos: " + e.getMessage());
        }
    }

    public static DetalhePedido adicionarPedidoDAO(int idPedido) {
        DetalhePedido novoPedido = new DetalhePedido();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Produtos disponíveis:");
        produtosDAO();

        while (true) {
            try {
                System.out.println("Selecione o número do item que deseja adicionar no pedido: ");
                int item = pegarPedidoProdutoDAO();
                novoPedido.id_item = item;


                System.out.print("Qual a quantidade desse item no pedido? ");
                int quantidade = Integer.parseInt(scanner.nextLine());

                if (quantidade <= 0) {
                    System.out.println("Quantidade inválida. Deve ser maior que zero.");
                    continue;
                }

                System.out.println("\nTem certeza que deseja adicionar " + quantidade + " unidades desse item ao pedido?");
                int confirmacao = simOuNao();

                if (confirmacao == 1) {
                    novoPedido.quantidade = quantidade;
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número válido.");
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
            }
        }

        double preco = pegarPedidoPrecoDAO(novoPedido.id_item);
        novoPedido.preco = preco * novoPedido.quantidade;

        String sql = "INSERT INTO piramide.detalhes_pedidos (id_pedido, id_produto, quantidade, preco) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPedido);
            pstmt.setInt(2, novoPedido.id_item);
            pstmt.setInt(3, novoPedido.quantidade);
            pstmt.setDouble(4, novoPedido.preco);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Produto inserido com sucesso no pedido!");
            } else {
                System.out.println("Nenhuma linha foi inserida. Verifique os dados.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir o pedido: " + e.getMessage());
        }

        return novoPedido;
    }


    public static void pedidoProdutosDAO () {
        String sql = "SELECT id_produto, nome_produto, preco FROM piramide.produtos ORDER BY id_produto ASC";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Produto: " + rs.getString("id_produto") +
                        "\nNome: " + rs.getString("nome_produto") +
                        "\nPreço: " + rs.getDouble("preco"));
            }
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int pegarPedidoProdutoDAO () {
        Scanner scan = new Scanner(System.in);
        boolean produtoExistente = false;
        int idProduto = -1;

        do {
            idProduto = scan.nextInt();

            String sql = "SELECT nome_produto, preco FROM piramide.produtos WHERE id_produto = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idProduto);
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

    public static double pegarPedidoPrecoDAO (int idProduto){
        Scanner scan = new Scanner(System.in);
        boolean produtoExistente = false;
        double precoProduto = 0;

            String sql = "SELECT preco FROM piramide.produtos WHERE id_produto = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idProduto);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    precoProduto = rs.getDouble("preco");
                    produtoExistente = true;
                } else {
                    System.out.println("Produto não encontrado! Digite um ID válido.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        return precoProduto;
    }

    public static int pegarProdutoDPDAO(DetalhePedido lista) {
        Scanner scan = new Scanner(System.in);
        boolean ingredienteExistente = false;
        int idDetalhePedido;

        do {
            idDetalhePedido = scan.nextInt();

            String sql = "SELECT id_detalhe_pedido FROM piramide.detalhes_pedidos WHERE id_detalhe_pedido = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idDetalhePedido);
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

        return idDetalhePedido;
    }

    public static int removerPedidoProdutoDAO (int idDetalhePedido, int idPedido) {
        Scanner scan = new Scanner(System.in);
        boolean produtoExistente = false;
        int idProduto = 0;
        String sql = null;


        do {
            idProduto = scan.nextInt();

            if (idPedido == 0){
                idProduto = scan.nextInt();
                sql = "DELETE FROM piramide.detalhes_pedidos WHERE id_detalhe_pedido = ?";
            }else {
                sql = "DELETE FROM piramide.detalhes_pedidos WHERE id_pedido = ?";
            }



            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                if (idPedido == 0){
                    pstmt.setInt(1, idDetalhePedido);
                }else{
                    pstmt.setInt(1, idPedido);
                }


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

    public static double totalPedidoDAO(int idPedido) {
        double total = 0;

        String sql = "SELECT SUM(dp.quantidade * p2.preco) AS total_gasto " +
                "FROM piramide.clientes c " +
                "JOIN piramide.pedidos p ON p.id_cliente = c.id_cliente " +
                "JOIN piramide.detalhes_pedidos dp ON dp.id_pedido = p.id_pedido " +
                "JOIN piramide.produtos p2 ON p2.id_produto = dp.id_produto " +
                "WHERE p.id_pedido = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total_gasto");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular o total do pedido: " + e.getMessage());
        }

        return total;
    }

    public static void voltarCarrinhoPedidoDAO() {
        String sql = "SELECT * FROM piramide.pedidos ORDER BY id_carrinho DESC LIMIT 1";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                rs.getInt("id_detalhe_pedido");
            } else {
                System.out.println("Nenhum pedido encontrado!");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco: " + e.getMessage());
        }

    }

    public static void removerDetalhePedidoDAO(int idPedido) {
        String sqlDetalhes = "DELETE FROM piramide.detalhes_pedidos WHERE id_pedido = ?";

        try (Connection conn = ConexaoBD.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtDetalhes = conn.prepareStatement(sqlDetalhes)) {
                pstmtDetalhes.setInt(1, idPedido);
                int rowsDetalhes = pstmtDetalhes.executeUpdate();
                if (rowsDetalhes > 0) {
                    System.out.println("Detalhes do pedido removidos com sucesso!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao remover pedido: " + e.getMessage());
        }
    }

}