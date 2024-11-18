package BancoDeDados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import Entidades.Funcionario;

import static BancoDeDados.ConexaoBD.conn;

public class FuncionarioDAO {
    public static void funcionariosDAO() {
        String sql = "SELECT id_funcionario, nome, usuario, senha, cpf, telefone, endereco FROM piramide.funcionarios ORDER BY id_funcionario ASC";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Id_funcionario: " + rs.getString("id_funcionario") +
                        "\nNome: " + rs.getString("nome") +
                        "\nUsuário: " + rs.getString("usuario") +
                        "\nSenha: " + rs.getString("senha") +
                        "\nCPF: " + rs.getString("cpf") +
                        "\nTelefone: " + rs.getString("telefone") +
                        "\nEndereço: " + rs.getString("endereco"));
            }
            System.out.println("===========================");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void inserirFuncionarioDAO(Funcionario novo) {

        String usuario = novo.usuario;
        String senha = novo.senha;
        String nome = novo.nome;
        String cpf = novo.cpf;
        String telefone = novo.telefone;
        String endereco = novo.endereco;

        String sql = "INSERT INTO piramide.funcionarios (usuario, senha, nome, cpf, telefone, endereco) VALUES (?, ?, ?, ?, ?, ?)";

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
                System.out.println("Funcionário inserido com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int pegarFuncionarioDAO() {
        Scanner scan = new Scanner(System.in);

        int idFuncionario = scan.nextInt();

        String sql = "SELECT * FROM funcionarios where id_funcionarios = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFuncionario);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return idFuncionario;
    }

    public static void updateFuncionarioDAO(Funcionario update) {

        int idf = update.id_funcionario;

        String usuario = update.usuario;
        String senha = update.senha;
        String nome = update.nome;
        String cpf = update.cpf;
        String telefone = update.telefone;
        String endereco = update.endereco;

        System.out.println(update);

        String sql = "UPDATE piramide.funcionarios SET usuario = ?, senha = ?, nome = ?, cpf = ?, telefone = ?, endereco = ? WHERE id_funcionario = " + idf;

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
                System.out.println("Funcionário editado com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public static void checkadordeFuncionariosDAO(Funcionario novoFuncionario) {
        Scanner scanner = new Scanner(System.in);
        boolean usuarioExistente = true;

        do {
            do {
                System.out.print("Digite o usuario do funcionário(a): ");
                novoFuncionario.usuario = scanner.nextLine().trim();

                if (novoFuncionario.usuario.isEmpty()) {
                    System.out.println("Usuário não pode ser vazio ou apenas espaços! Digite novamente.");
                }
            } while (novoFuncionario.usuario.isEmpty());

            String sql = "SELECT usuario FROM piramide.funcionarios WHERE usuario = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, novoFuncionario.usuario);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Usuário já cadastrado! Digite novamente.");
                } else {
                    usuarioExistente = false;
                }

            } catch (SQLException e) {
                System.out.println("Erro ao verificar usuário: " + e.getMessage());
            }
        } while (usuarioExistente);
    }

    public static void removerFuncionarioDAO(int id) {

        String sql = "DELETE FROM piramide.funcionarios WHERE id_funcionario = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate(); // Executa a consulta DELETE

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int tamanhoFuncionarioDAO() {
        String sql = "SELECT COUNT(*) as total FROM piramide.funcionarios";
        int totalFuncionarios = 0;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                totalFuncionarios = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return totalFuncionarios;
    }
}
