//GerenciadorDeFuncionarios

package Util;

import BancoDeDados.ConexaoBD;
import Entidades.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static BancoDeDados.CriptografiaDAO.Criptografia;
import static BancoDeDados.FuncionarioDAO.checkadordeFuncionariosDAO;

public class GerenciadorDeFuncionarios {
    private static int contadorFuncionarios = 1;

    public static void confirmarDadosFuncionarios(Funcionario funcionario, String senha) {//PEDE A CONFIRMAÇÃO DOS DADOS -------------
        System.out.println("\n===========================" +
                "Seus dados estão corretos?\n" +
                "Usuario: " + funcionario.usuario + "\n" +
                "Senha: " + senha + "\n" +
                "Nome: " + funcionario.nome + "\n" +
                "CPF: " + funcionario.cpf + "\n" +
                "Telefone: " + funcionario.telefone + "\n" +
                "Endereço: " + funcionario.endereco + "\n" +
                "===========================");

    }

    public static Funcionario cadastrarFuncionarioDAO() { //CADASTRO --------------
        Funcionario novoFuncionario = new Funcionario();
        Scanner scanner = new Scanner(System.in);

        checkadordeFuncionariosDAO(novoFuncionario);

        Map<String, String> resultado = Criptografia();

        String senha = resultado.get("senha");
        novoFuncionario.senha = resultado.get("SenhaCriptografada");

        do {
            System.out.print("Digite o nome do(a) funcionário(a): ");
            novoFuncionario.nome = scanner.nextLine();

            while (novoFuncionario.nome.length() > 100) {
                System.out.println("Nome não pode ser maior que 100 caracteres.");
                System.out.print("Digite o nome do(a) funcionário(a): ");
                novoFuncionario.nome = scanner.nextLine();
            }
            if (novoFuncionario.nome.isEmpty()) {
                System.out.println("Nome não pode ser vazio ou apenas espaços! Digite novamente.");
            }
        } while (novoFuncionario.nome.isEmpty());

        System.out.print("Digite o CPF do(a) funcionário(a): ");
        novoFuncionario.cpf = scanner.nextLine();

        while (novoFuncionario.cpf.length() != 11) {
            System.out.println("CPF tem que ser igual a 11 digitos.");
            System.out.print("Digite o CPF do funcionário(a): ");
            novoFuncionario.cpf = scanner.nextLine();
        }

        System.out.print("Digite telefone do(a) funcionário(a): ");
        novoFuncionario.telefone = scanner.nextLine();

        while (novoFuncionario.telefone.length() < 8 || novoFuncionario.nome.length() > 14) {
            System.out.println("O telefone tem que ter ao menos 9 digitos.");
            System.out.print("Digite telefone do funcionário(a): ");
            novoFuncionario.telefone = scanner.nextLine();
        }

        do {
            System.out.print("Digite o endereço do(a) funcionário(a): ");
            novoFuncionario.endereco = scanner.nextLine();

            while (novoFuncionario.endereco.length() > 100) {
                System.out.println("Endereço não pode ser maior que 100 caracteres.");
                System.out.print("Digite o endereço do(a) funcionário(a): ");
                novoFuncionario.endereco = scanner.nextLine();
            }
            if (novoFuncionario.endereco.isEmpty()) {
                System.out.println("Nome não pode ser vazio ou apenas espaços! Digite novamente.");
            }
        } while (novoFuncionario.endereco.isEmpty());

        confirmarDadosFuncionarios(novoFuncionario, senha);

        return novoFuncionario;
    }

    private static void checkadordeFuncionarios(Funcionario novoFuncionario, List<Funcionario> funcionariosExistentes) {
        Scanner scanner = new Scanner(System.in);
        boolean usuarioExistente;

        do {
            usuarioExistente = false;

            do {
                System.out.print("Digite o usuario do funcionário(a): ");
                novoFuncionario.usuario = scanner.nextLine().trim();

                if (novoFuncionario.usuario.isEmpty()) {
                    System.out.println("Usuário não pode ser vazio ou apenas espaços! Digite novamente.");
                }
            } while (novoFuncionario.usuario.isEmpty());

            for (Funcionario funcionario : funcionariosExistentes) {
                if (funcionario != novoFuncionario && funcionario.usuario.equals(novoFuncionario.usuario)) {
                    System.out.println("Usuário já cadastrado. Tente novamente.");
                    usuarioExistente = true;
                    break;
                } else {
                    break;
                }
            }

        } while (usuarioExistente);
    }


    public static int loginFuncionario(List<Funcionario> funcionarios, int i) {//LOGIN ----------------------------------
        Scanner scanner = new Scanner(System.in);

        String usuario, senha;
        Funcionario loginFuncionario = null;

        do {
            System.out.print("Digite seu usuario: ");
            usuario = scanner.nextLine();

            for (Funcionario funcionario : funcionarios) {
                if (funcionario.usuario.equals(usuario)) {
                    loginFuncionario = funcionario;
                    break;
                }
            }
            if (loginFuncionario == null) {
                System.out.println("Usuario não encontrado! Digite novamente.");
            }
        } while (loginFuncionario == null);

        do {
            System.out.print("Digite sua senha: ");

            senha = scanner.nextLine();

            if (!loginFuncionario.senha.equals(senha)) {
                System.out.println("Senha incorreta! Digite novamente.");
            }
        } while (!loginFuncionario.senha.equals(senha));
        System.out.println("Logado com sucesso!");

        if (loginFuncionario.usuario.equals("admin") && loginFuncionario.senha.equals("admin")) {
            //i = 1;
            return i + 1;
        }
        return i;
    }

    public static int pegarFuncionario(List<Funcionario> funcionarios) {//PEGAR ID DO FUNCIONÁRIO -----------------------
        Scanner scanner = new Scanner(System.in);
        int idFuncionario = 0;
        do {
            try {
                idFuncionario = scanner.nextInt(); //2 -thiago

                if (idFuncionario == 1) {
                    System.out.println("Administrador não pode ser alterado");
                } else if (idFuncionario < 0 || idFuncionario == 0 || idFuncionario > funcionarios.size()) {
                    System.out.println("Não existe funcionário com esse id! Digite novamente.");
                }
                //0-adm, 1-thiago
            } catch (IndexOutOfBoundsException erro) {
                System.out.println("[ERROR], digite uma opção valida. ");
            }
        } while (idFuncionario == 1 || idFuncionario < 0 || idFuncionario == 0 || idFuncionario > funcionarios.size());
        return idFuncionario;
    }

    public static void removerFuncionario(List<Funcionario> funcionarios, int indexFuncionario) {
        if (indexFuncionario >= 0 && indexFuncionario < funcionarios.size()) {
            funcionarios.remove(indexFuncionario);
            // Atualiza os IDs dos funcionários após a remoção
            for (int i = 0; i < funcionarios.size(); i++) {
                funcionarios.get(i).id_funcionario = i + 1;
            }
            System.out.println("Funcionário removido com sucesso!");
        } else {
            System.out.println("Não existe funcionário com esse ID!");
        }
    }

    public static Funcionario editarFuncionario(int id) {
        Scanner scanner = new Scanner(System.in);
        Funcionario funcionarioAtual = new Funcionario();

        String senha, senhaLogin;

        boolean usuarioExistente = true;

        funcionarioAtual.id_funcionario = id;

        checkadordeFuncionariosDAO(funcionarioAtual);

        do {
            Map<String, String> resultado = Criptografia();

            funcionarioAtual.senha = resultado.get("SenhaCriptografada");

            String sql = "SELECT senha FROM piramide.funcionarios WHERE id_funcionario = ?";

            try (Connection conn = ConexaoBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    senhaLogin = rs.getString("senha");

                    if(senhaLogin.equals(funcionarioAtual.senha)){
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
            funcionarioAtual.nome = scanner.nextLine();

            while (funcionarioAtual.nome.length() > 100) {
                System.out.println("[ERROR] Nome não pode ser maior que 100 caracteres.");
                System.out.print("Digite seu nome: ");
                funcionarioAtual.nome = scanner.nextLine();
            }
            if (funcionarioAtual.nome.isEmpty()) {
                System.out.println("[ERROR] Nome não pode ser vazio ou apenas espaços!");
            }
        } while (funcionarioAtual.nome.isEmpty());

        System.out.print("Digite seu CPF: ");
        funcionarioAtual.cpf = scanner.nextLine();

        while (funcionarioAtual.cpf.length() != 11) {
            System.out.println("[ERROR] CPF tem que ser igual a 11 digitos.");
            System.out.print("Digite seu CPF: ");
            funcionarioAtual.cpf = scanner.nextLine();
        }

        System.out.print("Digite seu telefone: ");
        funcionarioAtual.telefone = scanner.nextLine();

        while (funcionarioAtual.telefone.length() < 9 || funcionarioAtual.nome.length() > 14) {
            System.out.println("[ERROR] Telefone tem que ter ao menos 9 digitos.");
            System.out.print("Digite seu telefone: ");
            funcionarioAtual.telefone = scanner.nextLine();
        }

        do {
            System.out.print("Digite o endereço para entrega: ");
            funcionarioAtual.endereco = scanner.nextLine();

            while (funcionarioAtual.endereco.length() > 100) {
                System.out.println("[ERROR] Endereço não pode ser maior que 100 caracteres.");
                System.out.print("Digite o endereço para entrega: ");
                funcionarioAtual.endereco = scanner.nextLine();
            }
            if (funcionarioAtual.endereco.isEmpty()) {
                System.out.println("[ERROR] Endereço não pode ser vazio ou apenas espaços!");
            }
        } while (funcionarioAtual.endereco.isEmpty());

        System.out.println("\nUsuário atual: " + funcionarioAtual);

        System.out.println("\nUsuário atual: " +
                "===========================" +
                "Usuario: " + funcionarioAtual.usuario + "\n" +
                "Senha: " + funcionarioAtual.senha + "\n" +
                "Nome: " + funcionarioAtual.nome + "\n" +
                "Cpf: " + funcionarioAtual.cpf + "\n" +
                "Endereço: " + funcionarioAtual.endereco + "\n" +
                "Telefone: " + funcionarioAtual.telefone + "\n");
        System.out.println("===========================");

        return funcionarioAtual;
    }

    public static int opcaoMenuFuncionario() {//OPÇÃO MENU DO ADMINISTRADOR ---------------------------------------------------
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
                    System.out.println("""
                            1 | GERENCIAR MESAS
                            2 | ADICIONAR PEDIDO
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

    public static Funcionario updateFuncionario(int id) {//-----------------------
        Scanner scanner = new Scanner(System.in);
        Funcionario funcionarioAtual = new Funcionario();

        funcionarioAtual.id_funcionario = id;

        checkadordeFuncionariosDAO(funcionarioAtual);

        do {
            System.out.print("Digite a senha: ");
            funcionarioAtual.senha = scanner.nextLine();

            while (funcionarioAtual.senha.length() > 50) {
                System.out.println("Senha não pode ser maior que 50 caracteres.");
                System.out.print("Digite a senha: ");
                funcionarioAtual.senha = scanner.nextLine();
            }
            if (funcionarioAtual.senha.isEmpty()) {
                System.out.println("Senha não pode ser vazio ou apenas espaços! Digite novamente.");
            }
        } while (funcionarioAtual.senha.isEmpty());

        do {
            System.out.print("Digite o nome do(a) funcionário(a): ");
            funcionarioAtual.nome = scanner.nextLine();

            while (funcionarioAtual.nome.length() > 100) {
                System.out.println("Nome não pode ser maior que 100 caracteres.");
                System.out.print("Digite o nome do(a) funcionário(a): ");
                funcionarioAtual.nome = scanner.nextLine();
            }
            if (funcionarioAtual.nome.isEmpty()) {
                System.out.println("Nome não pode ser vazio ou apenas espaços! Digite novamente.");
            }
        } while (funcionarioAtual.nome.isEmpty());

        System.out.print("Digite o CPF do(a) funcionário(a): ");
        funcionarioAtual.cpf = scanner.nextLine();

        while (funcionarioAtual.cpf.length() != 11) {
            System.out.println("CPF tem que ser igual a 11 digitos.");
            System.out.print("Digite o CPF do funcionário(a): ");
            funcionarioAtual.cpf = scanner.nextLine();
        }

        System.out.print("Digite telefone do(a) funcionário(a): ");
        funcionarioAtual.telefone = scanner.nextLine();

        while (funcionarioAtual.telefone.length() < 8 || funcionarioAtual.nome.length() > 14) {
            System.out.println("O telefone tem que ter ao menos 9 digitos.");
            System.out.print("Digite telefone do funcionário(a): ");
            funcionarioAtual.telefone = scanner.nextLine();
        }

        do {
            System.out.print("Digite o endereço do(a) funcionário(a): ");
            funcionarioAtual.endereco = scanner.nextLine();

            while (funcionarioAtual.endereco.length() > 100) {
                System.out.println("Endereço não pode ser maior que 100 caracteres.");
                System.out.print("Digite o endereço do(a) funcionário(a): ");
                funcionarioAtual.endereco = scanner.nextLine();
            }
            if (funcionarioAtual.endereco.isEmpty()) {
                System.out.println("Nome não pode ser vazio ou apenas espaços! Digite novamente.");
            }
        } while (funcionarioAtual.endereco.isEmpty());

        System.out.println("\nFuncionario atual: " + funcionarioAtual);

        return funcionarioAtual;
    }
    //------------------------------------------------------------------------------------------------------------------
}