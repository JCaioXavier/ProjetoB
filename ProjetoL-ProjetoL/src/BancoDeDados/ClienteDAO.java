package BancoDeDados;

import Entidades.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ClienteDAO {
    public static void clientesDAO() {
        String sql = "SELECT id_cliente, nome, usuario, cpf, telefone, endereco FROM piramide.clientes ORDER BY id_cliente ASC";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Id_cliente: " + rs.getString("id_cliente") +
                        "\nNome: " + rs.getString("nome") +
                        "\nUsuário: " + rs.getString("usuario") +
                        "\nCPF: " + rs.getString("cpf") +
                        "\nTelefone: " + rs.getString("telefone") +
                        "\nEndereço: " + rs.getString("endereco"));
            }
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void inserirClienteDAO(Cliente novo) {

        String usuario = novo.usuario;
        String senha = novo.senha;
        String nome = novo.nome;
        String cpf = novo.cpf;
        String telefone = novo.telefone;
        String endereco = novo.endereco;

        String sql = "INSERT INTO piramide.clientes (usuario, senha, nome, cpf, telefone, endereco) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            pstmt.setString(3, nome);
            pstmt.setString(4, cpf);
            pstmt.setString(5, telefone);
            pstmt.setString(6, endereco);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cliente cadastrado com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int pegarClienteDAO() {
        Scanner scan = new Scanner(System.in);

        int idCliente = scan.nextInt();

        String sql = "SELECT * FROM clientes where id_cliente = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return idCliente;
    }

    public static void updateClienteDAO(Cliente update) {

        int idf = update.id_cliente;

        String usuario = update.usuario;
        String senha = update.senha;
        String nome = update.nome;
        String cpf = update.cpf;
        String telefone = update.telefone;
        String endereco = update.endereco;

        String sql = "UPDATE piramide.clientes SET usuario = ?, senha = ?, nome = ?, cpf = ?, telefone = ?, endereco = ? WHERE id_cliente = " + idf;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            pstmt.setString(3, nome);
            pstmt.setString(4, cpf);
            pstmt.setString(5, telefone);
            pstmt.setString(6, endereco);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Perfil editado com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public static void checkadorDeClienteDAO(Cliente novocliente) {
        Scanner scanner = new Scanner(System.in);
        boolean usuarioExistente = true;

        do {
            do {
                System.out.print("Digite o usuario: ");
                novocliente.usuario = scanner.nextLine().trim();

                if (novocliente.usuario.isEmpty()) {
                    System.out.println("Usuário não pode ser vazio ou apenas espaços! Digite novamente.");
                }
            } while (novocliente.usuario.isEmpty());

            String sql = "SELECT usuario FROM piramide.clientes WHERE usuario = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, novocliente.usuario);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Usuário já cadastrado! Digite novamente.");
                } else {
                    usuarioExistente = false;
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } while (usuarioExistente);
    }

    public static int loginClienteDAO(Cliente clientes) {//LOGIN DO CLIENTE *****************************************
        Scanner scanner = new Scanner(System.in);

        int idUsuario = 0;
        String usuario, senha;
        Cliente loginCliente = null;
        boolean usuarioExistente = true;

        do {
            do {
                System.out.print("Digite o usuario: ");
                clientes.usuario = scanner.nextLine().trim();

                if (clientes.usuario.isEmpty()) {
                    System.out.println("Usuário não pode ser vazio ou apenas espaços! Digite novamente.");
                }
            } while (clientes.usuario.isEmpty());

            String sql = "SELECT id_cliente FROM piramide.clientes WHERE usuario = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, clientes.usuario);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    usuarioExistente = false;
                    //pstmt.setInt(1, clientes.id_cliente);
                    idUsuario = rs.getInt("id_cliente");
                } else {
                    System.out.println("Usuário incorreto! Digite novamente.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } while (usuarioExistente);

        usuarioExistente = true;

        do {
            do {
                System.out.print("Digite a senha: ");
                clientes.senha = scanner.nextLine().trim();

                if (clientes.senha.isEmpty()) {
                    System.out.println("Senha não pode ser vazio ou apenas espaços! Digite novamente.");
                }
            } while (clientes.senha.isEmpty());

            String sql = "SELECT senha FROM piramide.clientes WHERE id_cliente = " + idUsuario + " AND senha = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, clientes.senha);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    usuarioExistente = false;
                } else {
                    System.out.println("Senha incorreta! Digite novamente.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } while (usuarioExistente);

        int id = 0;

        return id;
    }
}
