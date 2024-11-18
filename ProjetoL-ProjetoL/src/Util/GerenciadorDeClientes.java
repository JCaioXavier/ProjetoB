//Gerenciador de Clientes
package Util;

import BancoDeDados.ClienteDAO.*;
import Entidades.Cliente;
import Entidades.Funcionario;

import java.util.*;
import static BancoDeDados.ClienteDAO.*;


public class GerenciadorDeClientes {
    private static int contadorClientes = 2;

    public static void confirmarDadosClientes(Cliente cliente) { //PEDE A CONFIRMAÇÃO DOS DADOS ************************
        System.out.println("Seus dados estão corretos?\n" +
                "Usuário: " + cliente.usuario + "\n" +
                "Senha: " + cliente.senha + "\n" +
                "Nome: " + cliente.nome + "\n" +
                "CPF: " + cliente.cpf + "\n" +
                "Telefone: " + cliente.telefone + "\n" +
                "Endereço: " + cliente.endereco + "\n");
    }

    public static Cliente cadastrarCliente(List<Cliente> clientesExistentes) {//CADASTRAR NOVO CLIENTE ******************
        Cliente novoCliente = new Cliente();
        Scanner scanner = new Scanner(System.in);


        checkadorDeClienteDAO(novoCliente);// Passando a lista de clientes existentes

        do {
            System.out.print("Digite sua senha: ");
            novoCliente.senha = scanner.nextLine();

            while (novoCliente.senha.length() > 50) {
                System.out.println("[ERROR] Senha não pode ser maior que 50 caracteres.");
                System.out.print("Digite sua senha: ");
                novoCliente.senha = scanner.nextLine();
            }
            if (novoCliente.senha.isEmpty()) {
                System.out.println("[ERROR]Senha não pode ser vazio ou apenas espaços!");
            }
        } while (novoCliente.senha.isEmpty());

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

        return novoCliente;
    }

    private static void checkadordeClientes(Cliente novoCliente, List<Cliente> clientesExistentes) { //VERIFICA SE USUÁRIO JA EXISTE ***
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

    public static int loginCliente(List<Cliente> clientes) {//LOGIN DO CLIENTE *****************************************
        Scanner scanner = new Scanner(System.in);

        String usuario, senha;
        Cliente loginCliente = null;

        do {
            System.out.print("Digite seu usuário: ");
            usuario = scanner.nextLine();

            for (Cliente cliente : clientes) {//percorre a lsita de usuarios cadastrados
                if (cliente.usuario.equals(usuario)) {//verfica se o usuario inserido está cadastrado

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

            if (!loginCliente.senha.equals(senha)) {//confere se a senha digitada é diferente do usuário inserido (loginCliente = cliente)
                System.out.println("Senha incorreta! Digite novamente.");
            }
        } while (!loginCliente.senha.equals(senha));
        System.out.println("Logado com sucesso!");

        return loginCliente.id_cliente;
    }

    public static Cliente editarCliente(List<Cliente> clientes, int indexCliente) {
        Scanner scanner = new Scanner(System.in);
        Cliente clienteAtual = clientes.get(indexCliente - 1);

        System.out.println("\nUsuário atual: " + clienteAtual);// mostra as informações do clientes escolhido

        checkadordeClientes(clienteAtual, clientes);

        do {
            System.out.print("Digite a nova senha: ");
            clienteAtual.senha = scanner.nextLine();

            if (clienteAtual.senha.length() > 50) {
                System.out.println("[ERROR] Senha não pode ser maior que 50 caracteres.");
                System.out.print("Digite sua nova senha: ");
                clienteAtual.senha = scanner.nextLine();
            }
            if (clienteAtual.senha.isEmpty()) {
                System.out.println("[ERROR]Senha não pode ser vazio ou apenas espaços!");
            }
        } while (clienteAtual.senha.isEmpty());

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

        System.out.println("\nUsuário editado com sucesso!");

        return  clienteAtual;
    }

    public static int opcaoMenuCliente() {//OPÇÃO MENU DO ADMINISTRADOR ---------------------------------------------------
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