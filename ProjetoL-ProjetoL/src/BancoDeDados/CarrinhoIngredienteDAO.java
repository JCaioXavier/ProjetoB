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
            conn.setAutoCommit(false); // Iniciar transação

            // Atualizar estoque dos ingredientes
            try (PreparedStatement updateStmt1 = conn.prepareStatement(updateEstoqueSQL)) {
                int rowsUpdated1 = updateStmt1.executeUpdate();
                System.out.println("Estoque de ingredientes atualizado para " + rowsUpdated1 + " registros.");
            }

            // Atualizar estoque do carrinho
            try (PreparedStatement updateStmt2 = conn.prepareStatement(updateCarrinhoSQL)) {
                int rowsUpdated2 = updateStmt2.executeUpdate();
                System.out.println("Estoque do carrinho atualizado para " + rowsUpdated2 + " registros.");
            }

            conn.commit(); // Confirmar transação

        } catch (SQLException e) {
            System.err.println("Erro ao finalizar carrinho: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Reverter transação na mesma conexão
                    System.out.println("Transação revertida devido a um erro.");
                } catch (SQLException rollbackEx) {
                    System.err.println("Erro ao reverter transação: " + rollbackEx.getMessage());
                }
            }
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close(); // Garantir que a conexão será fechada
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }




}
