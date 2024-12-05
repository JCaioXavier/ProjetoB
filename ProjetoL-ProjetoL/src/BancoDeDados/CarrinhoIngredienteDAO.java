package BancoDeDados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarrinhoIngredienteDAO {


    public static void finalizarCarrinhoIngrediente() {
        String updateEstoqueSQL = "UPDATE piramide.ingredientes " +
                "SET estoque = ci.estoque " +
                "FROM piramide.carrinhos_ingredientes ci " +
                "WHERE piramide.ingredientes.id_ingrediente = ci.id_ingrediente";

        String updateCarrinhoSQL = "UPDATE piramide.carrinhos_ingredientes " +
                "SET estoque = piramide.ingredientes.estoque " +
                "FROM piramide.ingredientes " +
                "WHERE piramide.ingredientes.id_ingrediente = piramide.carrinhos_ingredientes.id_ingrediente";

        Connection conn = null;
        try {
            conn = ConexaoBD.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement updateStmt1 = conn.prepareStatement(updateEstoqueSQL)) {
                int rowsUpdated1 = updateStmt1.executeUpdate();
            }

            try (PreparedStatement updateStmt2 = conn.prepareStatement(updateCarrinhoSQL)) {
                int rowsUpdated2 = updateStmt2.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            System.err.println("Erro ao finalizar carrinho: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transação revertida devido a um erro.");
                } catch (SQLException rollbackEx) {
                    System.err.println("Erro ao reverter transação: " + rollbackEx.getMessage());
                }
            }
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }




}
