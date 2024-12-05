package BancoDeDados;

import Entidades.Cliente;
import Entidades.Pedido;

import java.sql.*;
import java.util.Scanner;

import static BancoDeDados.ConexaoBD.conn;
import static Util.Data.data;
import static Util.Data.dataPrint;



public class PedidoDAO {

    private static int contador = 1;

    public static void pedidosDAO(int idCliente) {
        String sql = "SELECT p.id_pedido, p.data_pedido, " +
                "STRING_AGG(p2.nome_produto || ' (' || dp.quantidade || ')', E'\n') AS produtos, " +
                "SUM(dp.preco * dp.quantidade) AS total " +
                "FROM piramide.clientes c " +
                "JOIN piramide.pedidos p ON p.id_cliente = c.id_cliente " +
                "JOIN piramide.detalhes_pedidos dp ON dp.id_pedido = p.id_pedido " +
                "JOIN piramide.produtos p2 ON p2.id_produto = dp.id_produto " +
                "WHERE c.id_cliente = ? " +
                "GROUP BY p.id_pedido, p.data_pedido " +
                "ORDER BY p.data_pedido DESC";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Pedido: " + contador);
                System.out.println("Data: " + rs.getDate("data_pedido"));
                System.out.println("Produtos:\n" + rs.getString("produtos"));
                System.out.println("Total: " + rs.getDouble("total"));
                contador++;
            }
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println("Erro ao executar consulta: " + e.getMessage());
        }

    }

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
        int idPedido = 0;
        String sql = null;
        pedidos.id_cliente = idCliente;

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

        sql = "SELECT id_pedido FROM piramide.pedidos WHERE id_cliente = ? ORDER BY id_pedido DESC LIMIT 1";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pedidos.id_cliente);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idPedido = rs.getInt("id_pedido");
            } else {
                idPedido = 1;
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


