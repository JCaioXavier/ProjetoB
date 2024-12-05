
package Util;

import Entidades.Cliente;
import Entidades.Funcionario;

import java.util.List;
import java.util.Scanner;

public class RevisarOpcao {


    public static int opcaoMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
                    System.out.println("""
                            1 | LOGIN
                            2 | CADASTRO
                            3 | SAIR""");

                    String opcaoCheckadora = scanner.nextLine();
                    opcao = Integer.parseInt(opcaoCheckadora);
                    if (opcao < 1 || opcao > 3) {
                        System.out.println("Erro! Opção invalida. Digite novamente");
                    }
                } while (opcao < 1 || opcao > 3);

                break;
            } catch (NumberFormatException erro) {
                System.out.println("Erro! Opção invalida. Digite novamente");
            }
        } while (true);
        return opcao;
    }

    public static int opcaoCadastro() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
                    System.out.println("""
                            1 | CLIENTE
                            2 | VOLTAR""");

                    String opcaoCheckadora = scanner.nextLine();
                    opcao = Integer.parseInt(opcaoCheckadora);
                    if (opcao < 1 || opcao > 2) {
                        System.out.println("Erro! Opção invalida. Digite novamente");
                    }
                } while (opcao < 1 || opcao > 2);
                break;
            } catch (NumberFormatException erro) {
                System.out.println("Erro! Opção invalida. Digite novamente");
            }
        } while (true);
        return opcao;
    }

    public static int opcaoLogin() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
                    System.out.println("""
                            1 | CLIENTE
                            2 | FUNCIONARIO
                            3 | VOLTAR""");

                    String opcaoCheckadora = scanner.nextLine();
                    opcao = Integer.parseInt(opcaoCheckadora);
                    if (opcao < 1 || opcao > 3) {
                        System.out.println("Erro! Opção invalida. Digite novamente");
                    }
                } while (opcao < 1 || opcao > 3);
                break;
            } catch (NumberFormatException erro) {
                System.out.println("Erro! Opção invalida. Digite novamente");
            }
        } while (true);
        return opcao;
    }

    public static int simOuNao() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
                    System.out.println("""
                            1 | SIM
                            2 | NÃO""");

                    String opcaoCheckadora = scanner.nextLine();
                    opcao = Integer.parseInt(opcaoCheckadora);
                    if (opcao < 1 || opcao > 2) {
                        System.out.println("Erro! Opção invalida. Digite novamente");
                    }
                } while (opcao < 1 || opcao > 2);
                break;
            } catch (NumberFormatException erro) {
                System.out.println("Erro! Opção invalida. Digite novamente");
            }
        } while (true);
        return opcao;
    }

    public static int opcaoMenuAdmin() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
                    System.out.println("""
                            1 | GERENCIAR FUNCIONARIOS
                            2 | GERENCIAR CLIENTES
                            3 | GERENCIAR PRODUTOS
                            4 | GERENCIAR INGREDIENTES
                            5 | DESLOGAR""");

                    String opcaoCheckadora = scanner.nextLine();
                    opcao = Integer.parseInt(opcaoCheckadora);
                    if (opcao < 1 || opcao > 5) {
                        System.out.println("Erro! Opção invalida. Digite novamente");
                    }
                } while (opcao < 1 || opcao > 5);
                break;
            } catch (NumberFormatException erro) {
                System.out.println("Erro! Opção invalida. Digite novamente");
            }
        } while (true);
        return opcao;
    }

    public static int opcaoPadraoUm() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
                    String opcaoCheckadora = scanner.nextLine();
                    opcao = Integer.parseInt(opcaoCheckadora);
                    if (opcao != 1) {
                        System.out.println("Erro! Opção invalida. Digite novamente");
                    }
                } while (opcao != 1);
                break;
            } catch (NumberFormatException erro) {
                System.out.println("Erro! Opção invalida. Digite novamente");
            }
        } while (true);
        return opcao;
    }

    public static int opcaoPadraoUmADois() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
                    String opcaoCheckadora = scanner.nextLine();
                    opcao = Integer.parseInt(opcaoCheckadora);
                    if (opcao < 1 || opcao > 2) {
                        System.out.println("Erro! Opção invalida. Digite novamente");
                    }
                } while (opcao < 1 || opcao > 2);
                break;
            } catch (NumberFormatException erro) {
                System.out.println("Erro! Opção invalida. Digite novamente");
            }
        } while (true);
        return opcao;
    }

    public static int opcaoPadraoUmATres() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
                    String opcaoCheckadora = scanner.nextLine();
                    opcao = Integer.parseInt(opcaoCheckadora);
                    if (opcao < 1 || opcao > 3) {
                        System.out.println("Erro! Opção invalida. Digite novamente");
                    }
                } while (opcao < 1 || opcao > 3);
                break;
            } catch (NumberFormatException erro) {
                System.out.println("Erro! Opção invalida. Digite novamente");
            }
        } while (true);
        return opcao;
    }

    public static int opcaoPadraoUmAQuatro() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
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

    public static int opcaoPadraoUmACinco() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                do {
                    String opcaoCheckadora = scanner.nextLine();
                    opcao = Integer.parseInt(opcaoCheckadora);
                    if (opcao < 1 || opcao > 5) {
                        System.out.println("Erro! Opção invalida. Digite novamente");
                    }
                } while (opcao < 1 || opcao > 5);
                break;
            } catch (NumberFormatException erro) {
                System.out.println("Erro! Opção invalida. Digite novamente");
            }
        } while (true);
        return opcao;
    }
}