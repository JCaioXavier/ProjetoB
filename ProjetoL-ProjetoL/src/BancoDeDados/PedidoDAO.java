package BancoDeDados;

import Entidades.Cliente;
import Entidades.Pedido;

import java.sql.*;
import java.util.Scanner;

import static BancoDeDados.ConexaoBD.conn;
import static Util.Data.data;
import static Util.Data.dataPrint;

public class PedidoDAO {

    public static void pedidosDAO(int idCliente) {
        String sql = "SELECT p.id_pedido, p.data_pedido, dp.id_detalhe_pedido, p2.nome_produto, dp.quantidade, dp.preco, p.total " +
                "FROM piramide.clientes c " +
                "JOIN piramide.pedidos p ON p.id_cliente = c.id_cliente " +
                "JOIN piramide.detalhes_pedidos dp ON dp.id_pedido = p.id_pedido " +
                "JOIN piramide.produtos p2 ON p2.id_produto = dp.id_produto " +
                "WHERE c.id_cliente = ?";  // Usei "c.id_cliente" aqui para especificar a tabela clientes

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da consulta
            stmt.setInt(1, idCliente);

            ResultSet rs = stmt.executeQuery();
            //System.out.println("\nPedido: " + rs.getInt("id_pedido") +
            //" Data: " + rs.getDate("data_pedido"));
            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Pedido: " + rs.getInt("id_pedido") +
                        "\nItem: " + rs.getInt("id_detalhe_pedido") +
                        "\nProduto: " + rs.getString("nome_produto") +
                        "\nQuantidade: " + rs.getInt("quantidade") +
                        "\nPreço: " + rs.getDouble("preco"));
            }
            System.out.println("Data: "+dataPrint());
            System.out.println("Total:" + rs.getDouble("total"));
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*public static Pedido novoPedidoDAO(int idUsuario, int x) {
        Pedido novoPedido = new Pedido();

        if (x == 0){
            novoPedido.id_cliente = idUsuario;
            novoPedido.tipo = 'E';
        } else if (x == 1) {
            novoPedido.id_funcionario = idUsuario;
            novoPedido.tipo = 'P';
        }
        novoPedido.data = data();

        String sql = "INSERT INTO piramide.pedidos (id_cliente, tipo, total, data_pedido) VALUES (?, ?, ?, CURRENT_DATE)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if(x == 0){
                pstmt.setInt(1, novoPedido.id_cliente);
            } else {
                pstmt.setInt(1, novoPedido.id_funcionario);
            }

            pstmt.setString(2, String.valueOf(novoPedido.tipo));
            pstmt.setDouble(3, novoPedido.total);

            // Executa a inserção e recupera o ID gerado automaticamente
            int rowsAffected = pstmt.executeUpdate();

            // Recupera o ID gerado automaticamente
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        novoPedido.id_pedido = rs.getInt(1); // O ID gerado automaticamente
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir o pedido: " + e.getMessage());
        }
        return novoPedido;
    }*/

    public static Pedido novoPedidoDAO(int idUsuario, int x) {
        Pedido novoPedido = new Pedido();

        if (x == 0) {
            novoPedido.id_cliente = idUsuario;
            novoPedido.tipo = 'E';
        } else if (x == 1) {
            novoPedido.id_funcionario = idUsuario;
            novoPedido.tipo = 'P';
        }
        novoPedido.data = data();

        String sql = "INSERT INTO piramide.pedidos (id_cliente, tipo, total, data_pedido) VALUES (?, ?, ?, CURRENT_DATE)";


        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (x == 0) {
                pstmt.setInt(1, novoPedido.id_cliente);
            } else {
                pstmt.setInt(1, novoPedido.id_funcionario);
            }

            pstmt.setString(2, String.valueOf(novoPedido.tipo));
            pstmt.setDouble(3, novoPedido.total);

            int rowsAffected = pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir o pedido: " + e.getMessage());
        }
        return novoPedido;

    }

    public static int tamanhoPedidoDAO(int idCliente) {
        String sql = "SELECT COUNT(*) AS tamanho FROM piramide.pedidos WHERE id_cliente = ?";
        int tamanhoPedido = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tamanhoPedido = rs.getInt("tamanho");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter tamanho do pedido: " + e.getMessage());
        }

        return tamanhoPedido;
    }

    public static int pegarPedidoDAO(Pedido pedidos, int idCliente, int x) {
        int idPedido = 0; // Valor inicial indicando "não encontrado"
        String sql = null;
        pedidos.id_cliente = idCliente;

        // Consulta para contar os pedidos do cliente
        sql = "SELECT COUNT(*) FROM piramide.pedidos WHERE id_cliente = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pedidos.id_cliente);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                rs.getInt(1);
            } else {
                System.out.println("Pedido não encontrado! Digite um ID válido.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco: " + e.getMessage());
        }

        // Consulta para buscar o último pedido do cliente
        sql = "SELECT id_pedido FROM piramide.pedidos WHERE id_cliente = ? ORDER BY id_pedido DESC LIMIT 1";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pedidos.id_cliente); // Define o parâmetro com o id do cliente
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idPedido = rs.getInt("id_pedido");
            } else {
                // Se não encontrar nenhum pedido, cria um novo ID
                idPedido = 1;  // Ou um outro valor que faça sentido, dependendo de como você cria novos pedidos
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco: " + e.getMessage());
        }


        if (x != -1) {
            idPedido = x;
        }

        return idPedido;
    }
}



    /*public static int pegarPedidoDAO(Pedido pedidos, int idCliente) {
        int idPedido = 0; // Valor inicial indicando "não encontrado"
        pedidos.id_cliente = idCliente;

        // Consulta para buscar o último pedido do cliente
        String sql = "SELECT id_pedido FROM piramide.pedidos WHERE id_cliente = ? AND tipo = ? ORDER BY id_pedido DESC LIMIT 1";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Define os parâmetros
            pstmt.setInt(1, pedidos.id_cliente);
            pstmt.setString(2, a == 0 ? "E" : "P"); // Filtra pelo tipo de pedido: 'E' ou 'P'

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idPedido = rs.getInt("id_pedido"); // Reutiliza o último pedido encontrado

            } else {
                // Se não encontrar nenhum pedido, cria um novo
                idPedido = criarNovoPedido(pedidos, idCliente); // Função auxiliar para criar o novo pedido
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar o banco: " + e.getMessage());
        }


        return idPedido;
    }

    // Função auxiliar para criar um novo pedido
    private static int criarNovoPedido(Pedido pedidos, int idCliente, int a) {
        int novoIdPedido = 0;

        String sql = "INSERT INTO piramide.pedidos (id_cliente, tipo, total, data_pedido) VALUES (?, ?, ?, CURRENT_DATE)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, idCliente);
            pstmt.setString(2, a == 0 ? "E" : "P"); // Define o tipo de pedido
            pstmt.setDouble(3, 0.0); // Total inicial como 0.0

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        novoIdPedido = rs.getInt(1); // Recupera o ID gerado
                    }
                }
            } else {
                System.out.println("Erro: nenhum pedido foi criado.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao criar o pedido: " + e.getMessage());
        }

        return novoIdPedido;
    }*/

