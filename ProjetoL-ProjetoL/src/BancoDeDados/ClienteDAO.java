package BancoDeDados;

import Entidades.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static BancoDeDados.CriptografiaDAO.Criptografia;

public class ClienteDAO {
    public static void clientesDAO() {
        String sql = "SELECT id_cliente, nome_cliente, usuario, cpf, telefone, endereco FROM piramide.clientes ORDER BY id_cliente ASC";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Id_cliente: " + rs.getString("id_cliente") +
                        "\nNome: " + rs.getString("nome_cliente") +
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
        String nome_cliente = novo.nome;
        String cpf = novo.cpf;
        String telefone = novo.telefone;
        String endereco = novo.endereco;

        String sql = "INSERT INTO piramide.clientes (usuario, senha, nome_cliente, cpf, telefone, endereco) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            pstmt.setString(3, nome_cliente);
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

        int idC = update.id_cliente;

        String usuario = update.usuario;
        String senha = update.senha;
        String nome_cliente = update.nome;
        String cpf = update.cpf;
        String telefone = update.telefone;
        String endereco = update.endereco;

        String sql = "UPDATE piramide.clientes SET usuario = ?, senha = ?, nome_cliente = ?, cpf = ?, telefone = ?, endereco = ? WHERE id_cliente = " + idC;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            pstmt.setString(3, nome_cliente);
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
        String usuario, senha, senhaLogin, senhaProvisoria;
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
            Map<String, String> resultado = Criptografia();

            clientes.senha = resultado.get("SenhaCriptografada");

            String sql = "SELECT senha FROM piramide.clientes WHERE id_cliente = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idUsuario);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    senhaLogin = rs.getString("senha");

                    if(senhaLogin.equals(clientes.senha)){
                        usuarioExistente = false;
                    }else{
                        System.out.println("Senha incorreta! Digite novamente.");
                    }
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } while (usuarioExistente);

        return idUsuario;
    }

    public static void perfilCliente(int id) {//LOGIN DO CLIENTE *****************************************

        String sql = "SELECT id_cliente, nome_cliente, usuario, cpf, telefone, endereco FROM piramide.clientes WHERE id_cliente = " + id;
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("===========================");
                System.out.println("Nome: " + rs.getString("nome_cliente") +
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

    public static Cliente cadastrarClienteDAO() {//CADASTRAR NOVO CLIENTE ******************
        Cliente novoCliente = new Cliente();
        Scanner scanner = new Scanner(System.in);

        checkadorDeClienteDAO(novoCliente);// Passando a lista de clientes existentes

        Map<String, String> resultado = Criptografia();

        String senha = resultado.get("senha");
        novoCliente.senha = resultado.get("SenhaCriptografada");

        do {
            System.out.print("Digite seu nome: ");
            novoCliente.nome = scanner.nextLine();

            while (novoCliente.nome.length() > 100) {
                System.out.println("[ERROR] Nome não pode ser maior que 100 caracteres.");
                System.out.print("Digite sua senha: ");
                novoCliente.senha = scanner.nextLine();
            }
            if (novoCliente.nome.isEmpty()) {
                System.out.println("[ERROR] Nome não pode ser vazio ou apenas espaços!");
            }
        } while (novoCliente.nome.isEmpty());

        System.out.print("Digite seu CPF: ");
        novoCliente.cpf = scanner.nextLine();

        while (novoCliente.cpf.length() != 11) {
            System.out.println("[ERROR] CPF tem que ser igual a 11 digitos.");
            System.out.print("Digite seu CPF: ");
            novoCliente.cpf = scanner.nextLine();
        }

        System.out.print("Digite seu telefone: ");
        novoCliente.telefone = scanner.nextLine();

        while (novoCliente.telefone.length() < 9 || novoCliente.telefone.length() > 11) {
            System.out.println("[ERROR] Telefone tem que ter ao menos 9 digitos.");
            System.out.print("Digite seu telefone: ");
            novoCliente.telefone = scanner.nextLine();
        }

        do {
            System.out.print("Digite seu endereço: ");
            novoCliente.endereco = scanner.nextLine();

            while (novoCliente.endereco.length() > 100) {
                System.out.println("[ERROR] Endereço não pode ser maior que 100 caracteres.");
                System.out.print("Digite seu endereço: ");
                novoCliente.endereco = scanner.nextLine();
            }
            if (novoCliente.endereco.isEmpty()) {
                System.out.println("[ERROR] Endereço não pode ser vazio ou apenas espaços!");
            }
        } while (novoCliente.endereco.isEmpty());

        confirmarDadosClientesDAO(novoCliente, senha);

        return novoCliente;
    }

    public static void confirmarDadosClientesDAO(Cliente cliente, String senha) { //PEDE A CONFIRMAÇÃO DOS DADOS ************************
        System.out.println("\n===========================" +
                "Seus dados estão corretos?\n" +
                "Usuário: " + cliente.usuario + "\n" +
                "Senha: " + senha + "\n" +
                "Nome: " + cliente.nome + "\n" +
                "CPF: " + cliente.cpf + "\n" +
                "Telefone: " + cliente.telefone + "\n" +
                "Endereço: " + cliente.endereco + "\n" +
                "===========================");
    }
}
