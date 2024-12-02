package BancoDeDados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MesaDAO {
    public static void mesaDAO() {
        String sql = "SELECT id_mesa_pedido, id_pedido, status FROM piramide.mesas_pedidos ORDER BY id_mesa_pedido ASC";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Mesa: " + rs.getString("id_mesa_pedido") +
                        "\nPedido: " + rs.getString("id_pedido") +
                        "\nStatus: " + rs.getInt("status"));
            }
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
