
package Util;

import BancoDeDados.ConexaoBD;
import Entidades.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import static BancoDeDados.ClienteDAO.*;
import static BancoDeDados.CriptografiaDAO.Criptografia;


public class GerenciadorDeClientes {
    private static int contadorClientes = 2;

    public static void confirmarDadosClientes(Cliente cliente) {
        System.out.println("\n" +
                "Seus dados estão corretos?\n" +
                "===========================\n" +
                "Usuário: " + cliente.usuario + "\n" +
                "Senha: " + cliente.senha + "\n" +
                "Nome: " + cliente.nome + "\n" +
                "CPF: " + cliente.cpf + "\n" +
                "Telefone: " + cliente.telefone + "\n" +
                "Endereço: " + cliente.endereco + "\n" +
                "===========================");
    }

    private static void checkadordeClientes(Cliente novoCliente, List<Cliente> clientesExistentes) {
        Scanner scanner = new Scanner(System.in);
        boolean usuarioExistente;

        do {
            usuarioExistente = false;

            do {
                System.out.print("Digite o usuario: ");
                novoCliente.usuario = scanner.nextLine().trim();

                if (novoCliente.usuario.isEmpty()) {
                    System.out.println("[ERROR] Usuário não pode ser vazio ou apenas espaços!");
                }
            } while (novoCliente.usuario.isEmpty());

            for (Cliente cliente : clientesExistentes) {
                if (cliente != novoCliente && cliente.usuario.equals(novoCliente.usuario)) {
                    System.out.println("[ERROR] Usuário já cadastrado. Tente novamente.");
                    usuarioExistente = true;
                    break;
                } else {
                    break;
                }
            }

        } while (usuarioExistente);
    }

    public static int loginCliente(List<Cliente> clientes) {
        Scanner scanner = new Scanner(System.in);

        String usuario, senha;
        Cliente loginCliente = null;

        do {
            System.out.print("Digite seu usuário: ");
            usuario = scanner.nextLine();

            for (Cliente cliente : clientes) {
                if (cliente.usuario.equals(usuario)) {

                    loginCliente = cliente;
                    break;
                }
            }
            if (loginCliente == null) {
                System.out.println("Usuário não encontrado! Digite novamente.");
            }
        } while (loginCliente == null);

        do {
            System.out.print("Digite sua senha: ");
            senha = scanner.nextLine();

            if (!loginCliente.senha.equals(senha)) {
                System.out.println("Senha incorreta! Digite novamente.");
            }
        } while (!loginCliente.senha.equals(senha));
        System.out.println("Logado com sucesso!");

        return loginCliente.id_cliente;
    }

    public static Cliente editarCliente(int id) {
        Scanner scanner = new Scanner(System.in);
        Cliente clienteAtual = new Cliente();

        boolean usuarioExistente = true;

        String senhaLogin, senha;

        clienteAtual.id_cliente = id;

        checkadorDeClienteDAO(clienteAtual);

        do {
            Map<String, String> resultado = Criptografia();

            clienteAtual.senha = resultado.get("SenhaCriptografada");
            senha = resultado.get("senha");

            String sql = "SELECT senha FROM piramide.clientes WHERE id_cliente = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    senhaLogin = rs.getString("senha");

                    if(senhaLogin.equals(clienteAtual.senha)){
                        usuarioExistente = false;
                    }else{
                        System.out.println("Senha incorreta! Digite novamente.");
                    }
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } while (usuarioExistente);

        do {
            System.out.print("Digite seu nome: ");
            clienteAtual.nome = scanner.nextLine();

            while (clienteAtual.nome.length() > 100) {
                System.out.println("[ERROR] Nome não pode ser maior que 100 caracteres.");
                System.out.print("Digite seu nome: ");
                clienteAtual.nome = scanner.nextLine();
            }
            if (clienteAtual.nome.isEmpty()) {
                System.out.println("[ERROR] Nome não pode ser vazio ou apenas espaços!");
            }
        } while (clienteAtual.nome.isEmpty());

        System.out.print("Digite seu CPF: ");
        clienteAtual.cpf = scanner.nextLine();

        while (clienteAtual.cpf.length() != 11) {
            System.out.println("[ERROR] CPF tem que ser igual a 11 digitos.");
            System.out.print("Digite seu CPF: ");
            clienteAtual.cpf = scanner.nextLine();
        }

        System.out.print("Digite seu telefone: ");
        clienteAtual.telefone = scanner.nextLine();

        while (clienteAtual.telefone.length() < 9 || clienteAtual.nome.length() > 14) {
            System.out.println("[ERROR] Telefone tem que ter ao menos 9 digitos.");
            System.out.print("Digite seu telefone: ");
            clienteAtual.telefone = scanner.nextLine();
        }

        do {
            System.out.print("Digite o endereço para entrega: ");
            clienteAtual.endereco = scanner.nextLine();

            while (clienteAtual.endereco.length() > 100) {
                System.out.println("[ERROR] Endereço não pode ser maior que 100 caracteres.");
                System.out.print("Digite o endereço para entrega: ");
                clienteAtual.endereco = scanner.nextLine();
            }
            if (clienteAtual.endereco.isEmpty()) {
                System.out.println("[ERROR] Endereço não pode ser vazio ou apenas espaços!");
            }
        } while (clienteAtual.endereco.isEmpty());

        System.out.println("\nUsuário atual: " + clienteAtual);

        System.out.println("\nUsuário atual: " +
                "===========================" +
                "Usuario: " + clienteAtual.usuario + "\n" +
                "Senha: " + senha + "\n" +
                "Nome: " + clienteAtual.nome + "\n" +
                "Cpf: " + clienteAtual.cpf + "\n" +
                "Endereço: " + clienteAtual.endereco + "\n" +
                "Telefone: " + clienteAtual.telefone + "\n");
        System.out.println("===========================");

        return clienteAtual;
    }

    public static int opcaoMenuCliente() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
                    System.out.println("""
                            1 | FAZER PEDIDO
                            2 | HISTÓRICO DE PEDIDO
                            3 | EDITAR PERFIL
                            4 | DESLOGAR""");

                    String opcaoCheckadora = scanner.nextLine();
                    opcao = Integer.parseInt(opcaoCheckadora);
                    if (opcao < 1 || opcao > 4) {
                        System.out.println("Erro! Opção invalida. Digite novamente");
                    }
                } while (opcao < 1 || opcao > 4);
                break;
            } catch (NumberFormatException erro) {
                System.out.println("Erro! Opção invalida. Digite novamente");
            }
        } while (true);
        return opcao;
    }
}