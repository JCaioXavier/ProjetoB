import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import BancoDeDados.*;
import Entidades.*;

import static BancoDeDados.IngredienteDAO.*;
import static BancoDeDados.ProdutoDAO.*;
import static BancoDeDados.ClienteDAO.*;
import static BancoDeDados.FuncionarioDAO.*;
import static Util.DadosEstaticos.*;
import static Util.GerenciadorDeClientes.*;
import static Util.GerenciadorDeFuncionarios.*;
import static Util.GerenciadorDeFuncionarios.editarFuncionario;
import static Util.GerenciadorDeIngredientes.*;
import static Util.GerenciadorDeMesas.*;
import static Util.GerenciadorDeProdutos.*;
import static Util.GerenciadorDeCarrinho.*;
import static Util.RevisarOpcao.*;
import static Util.GerenciadorDeHistoricoPedido.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Funcionario> funcionarios = gerarFuncionarios();
        List<Cliente> clientes = gerarClientes();
        List<Produto> produtos = gerarProduto();
        List<Ingrediente> ingredientes = gerarIngrediente();
        List<Carrinho> listaDeCompras = gerarCarrinho();
        List<HistoricoPedido> listaPedidosClientes = new ArrayList<>();
        List<Mesa> mesaPedidosFuncionarios = new ArrayList<>();
        FuncionarioDAO funcionarioDAO;

        //TELA MENU INICIAL

        int opcaoLogin, opcaoCadastro, opcaoMenu, simOuNao;
        int idLoginCliente = 0, id_LoginFuncionario = 0;

        boolean menuBoolean = true, cadastroBoolean;

        boolean telaMenu = true, telaCliente = false, telaFuncionario = false, telaAdmin = false;

        do {

            do {// INICIA LOOP TELA MENU -------------------------------------------------------------------------------
                opcaoMenu = opcaoMenu();

                do {//INICIA LOOP TELA LOGIN/ CADASTRO/ E SAIR
                    if (opcaoMenu == 1) {
                        System.out.println("LOGAR COMO:");
                        opcaoLogin = opcaoLogin();

                        if (opcaoLogin == 1) {// 1 | CLIENTE || //implementar o banco de dados
                            Cliente novocliente = new Cliente();

                            //idLoginCliente = loginCliente(clientes);

                            loginClienteDAO(novocliente);

                            //encerra o loop e vai para a tela do cliente
                            menuBoolean = false;
                            telaMenu = false;
                            telaCliente = true;

                        } else if (opcaoLogin == 2) {// 2 | FUNCIONÁRIO // //implementar o banco de dados

                            int telaFadmin = loginFuncionario(funcionarios, 0);

                            menuBoolean = false;
                            telaMenu = false;

                            if (telaFadmin == 1) {//LOGOU COMO ADMINISTRADOR
                                telaAdmin = true;
                            } else {//LOGOU COMO FUNCIONÁRIO
                                telaFuncionario = true;
                            }

                        } else if (opcaoLogin == 3) {// 3 | VOLTAR
                            menuBoolean = false;//encerra o loop e volta para a menu inicial
                        }
                    } else if (opcaoMenu == 2) {//implementar o banco de dados
                        System.out.println("CADASTRAR COMO:");
                        opcaoCadastro = opcaoCadastro();

                        if (opcaoCadastro == 1) {
                            do {//INICIA LOOP DE CADASTRO CLIENTE
                                cadastroBoolean = true;

                                Cliente novoCliente = cadastrarCliente(clientes);

                                confirmarDadosClientes(novoCliente);

                                simOuNao = simOuNao();

                                if (simOuNao == 1) {//SIM
                                    System.out.println("Cliente cadastrado com sucesso!!!!");

                                    clientes.add(novoCliente);
                                    inserirClienteDAO(novoCliente);

                                    cadastroBoolean = false;//ENCERRA LOOP DE CADASTRO CLIENTE
                                }
                            } while (cadastroBoolean);
                            menuBoolean = false;//ENCERRA LOOP E VOLTA PARA O MENU INICIAL
                        } else if (opcaoCadastro == 2) {//Opção voltar
                            menuBoolean = false;//encerra o loop e volta para a menu inicial
                        }
                    } else if (opcaoMenu == 3) {
                        System.out.println("Encerrando o sistema.");
                        return;
                    }
                } while (menuBoolean);
            } while (telaMenu);

            //TELA DO CLIENTE ------------------------------------------------------------------------------------------
            double total;
            int opcaoMenuCliente, opcaoRetorno = 0;
            boolean menuCliente = true, menuAdicionarProduto = true, editarPerfil = true, historicoDePedido = true, adicionarProduto = true;
            List<HistoricoPedido> historicoPedidos = new ArrayList<>();

            while (telaCliente) {
                System.out.println("\nCLIENTE: ");
                opcaoMenuCliente = opcaoMenuCliente();

                if (opcaoMenuCliente == 4) {
                    System.out.println("Deslogado com sucesso!");
                    telaCliente = false;
                    telaMenu = true;
                    break;
                }

                do {
                    if (opcaoMenuCliente == 1) {// 1 | FAZER PEDIDO //implementar o banco de dados
                        do {
                            menuCliente = true;
                            adicionarProduto = true;

                            if (opcaoRetorno == 1) {
                                adicionarProduto = false;
                                opcaoRetorno = 0;
                            }
                            while (adicionarProduto) {
                                System.out.println("PRODUTOS: ");
                                for (Produto produto : produtos) {
                                    System.out.print(produto.id_produto); // id
                                    System.out.println(" - " + produto.nome);
                                    System.out.println("R$ " + produto.preco);
                                    System.out.println(" ");
                                }

                                System.out.println("Selecione o número do Produto que deseja adicionar no pedido: ");
                                int id = pegarProdutos(produtos);
                                System.out.println(adicionarProduto(listaDeCompras, id));

                                System.out.println("Deseja adicionar outro produto?");
                                System.out.println("""
                                        1 | SIM
                                        2 | NÃO
                                        3 | VOLTAR""");

                                int opcao = opcaoPadraoUmATres();

                                if (opcao == 2) {
                                    adicionarProduto = false;
                                } else if (opcao == 3) {
                                    adicionarProduto = false;
                                    menuCliente = false;
                                }
                            }
                            if (!menuCliente) {
                                break;
                            }

                            System.out.println(listaDeCompras);

                            System.out.println("Deseja editar o pedido?");
                            simOuNao = simOuNao();

                            if (simOuNao == 1) { // //implementar o banco de dados
                                System.out.println("""
                                        1 | ADICIONAR PRODUTO
                                        2 | REMOVER PRODUTO""");

                                int opcao = opcaoPadraoUmADois();

                                if (opcao == 1) {
                                    menuAdicionarProduto = false;
                                } else if (opcao == 2) {
                                    System.out.println(listaDeCompras);
                                    System.out.println("Qual item do carrinho deseja remover? ");
                                    int id_Item = pegarItem(listaDeCompras);
                                    removerItem(listaDeCompras, id_Item);
                                }
                            } else {
                                total = precoTotal(listaDeCompras);
                                System.out.println(listaDeCompras);
                                System.out.println("Deseja finalizar o pedido?");
                                System.out.println("Total: R$ " + total);

                                simOuNao = simOuNao();

                                if (simOuNao == 1) {
                                    System.out.println("Pedido Realizado!.\n");
                                    listaPedidosClientes = adicionarHistoricoPedido(historicoPedidos, listaDeCompras, clientes, total, idLoginCliente);
                                    listaDeCompras.clear();

                                    menuAdicionarProduto = false;
                                    menuCliente = false;
                                } else if (simOuNao == 2) {
                                    System.out.println("Deseja apagar o pedido?");

                                    simOuNao = simOuNao();

                                    if (simOuNao == 1) {
                                        listaDeCompras.clear();
                                        menuAdicionarProduto = false;
                                        menuCliente = false;
                                    } else if (simOuNao == 2) {
                                        opcaoRetorno = 1;
                                        menuAdicionarProduto = false;
                                        menuCliente = false;
                                    }
                                }
                            }
                        } while (menuAdicionarProduto);
                    } else if (opcaoMenuCliente == 2) {// 2 | HISTÓRICO DE PEDIDO //implementar o banco de dados
                        int opcao;
                        do {
                            System.out.println("""
                                    1 | HISTÓRICO DE PEDIDOS
                                    2 | VOLTAR""");

                            opcao = opcaoPadraoUmADois();

                            if (opcao == 1) {
                                if (listaPedidosClientes.isEmpty()) {
                                    System.out.println("Nenhum pedido cadastrado.");
                                } else {
                                    System.out.println(listaPedidosClientes);
                                }
                                System.out.println("1 | VOLTAR");

                                opcao = opcaoPadraoUm();

                                if (opcao == 1) {
                                    historicoDePedido = false;
                                    menuCliente = false;
                                }
                            } else if (opcao == 2) {
                                historicoDePedido = false;
                                menuCliente = false;
                            }
                        } while (historicoDePedido);
                    } else if (opcaoMenuCliente == 3) { // 3 | EDITAR PERFIL //implementar o banco de dados
                        do {
                            System.out.println("EDITAR PERFIL:");
                            System.out.println(" ");

                            System.out.println(clientes.get(idLoginCliente - 1));

                            System.out.println("\nTem certeza que quer editar o perfil?\n");

                            System.out.println("""
                                    1 | SIM
                                    2 | NÃO
                                    3 | VOLTAR""");

                            int opcaoEditar = opcaoPadraoUmATres();

                            if (opcaoEditar == 1) {
                                Cliente updateCliente = editarCliente(clientes, idLoginCliente);
                                updateClienteDAO(updateCliente);

                                editarPerfil = false;
                            } else if (opcaoEditar == 3) {
                                editarPerfil = false;
                            }
                        } while (editarPerfil);
                        menuCliente = false;
                    }
                } while (menuCliente);
            }

            //TELA DO FUNCIONARIO --------------------------------------------------------------------------------------

            boolean menuFuncionario = true, gereciarMesas = true;
            List<Mesa> mesas = new ArrayList<>();

            while (telaFuncionario) {
                System.out.println("\nFUNCIONÁRIO");
                int opcaoMenuFuncionario = opcaoMenuFuncionario();

                if (opcaoMenuFuncionario == 4) {
                    System.out.println("Deslogado com sucesso!");
                    telaFuncionario = false;
                    telaMenu = true;
                    break;
                }

                do {
                    if (opcaoMenuFuncionario == 1) { //implementar o banco de dados
                        int opcao;
                        do {
                            System.out.println("""
                                    1 | GERENCIAR MESAS
                                    2 | VOLTAR""");

                            opcao = opcaoPadraoUmADois();

                            if (opcao == 1) {
                                if (mesas.isEmpty()) {
                                    System.out.println("Não existem mesas cadastradas.");
                                } else {
                                    System.out.println(mesaPedidosFuncionarios);
                                }

                                System.out.println("1 | VOLTAR");

                                opcao = opcaoPadraoUm();

                                if (opcao == 1) {
                                    gereciarMesas = false;
                                    menuFuncionario = false;
                                }
                            } else if (opcao == 2) {
                                gereciarMesas = false;
                                menuFuncionario = false;
                            }
                        } while (gereciarMesas);
                    } else if (opcaoMenuFuncionario == 2) {
                        do {
                            menuFuncionario = true;
                            adicionarProduto = true;

                            if (opcaoRetorno == 1) {
                                adicionarProduto = false;
                                opcaoRetorno = 0;
                            }
                            while (adicionarProduto) {
                                System.out.println("PRODUTOS: ");
                                for (Produto produto : produtos) {
                                    System.out.print(produto.id_produto); // id
                                    System.out.println(" - " + produto.nome);
                                    System.out.println("R$ " + produto.preco);
                                    System.out.println(" ");
                                }

                                System.out.println("Selecione o número do Produto que deseja adicionar no pedido: ");
                                int id = pegarProdutos(produtos);
                                System.out.println(adicionarProduto(listaDeCompras, id));

                                System.out.println("Deseja adicionar outro produto?");
                                System.out.println("""
                                        1 | SIM
                                        2 | NÃO
                                        3 | VOLTAR""");

                                int opcao = opcaoPadraoUmATres();

                                if (opcao == 2) {
                                    adicionarProduto = false;
                                } else if (opcao == 3) {
                                    adicionarProduto = false;
                                    menuFuncionario = false;
                                }
                            }
                            if (!menuFuncionario) {
                                break;
                            }

                            System.out.println(listaDeCompras);

                            System.out.println("Deseja editar o pedido?");
                            simOuNao = simOuNao();

                            if (simOuNao == 1) {
                                System.out.println("""
                                        1 | ADICIONAR PRODUTO
                                        2 | REMOVER PRODUTO""");

                                int opcao = opcaoPadraoUmADois();

                                if (opcao == 1) {
                                    menuAdicionarProduto = false;
                                } else if (opcao == 2) {
                                    System.out.println(listaDeCompras);
                                    System.out.println("Qual item do carrinho deseja remover? ");
                                    int id_Item = pegarItem(listaDeCompras);
                                    removerItem(listaDeCompras, id_Item);
                                }
                            } else {
                                total = precoTotal(listaDeCompras);
                                System.out.println(listaDeCompras);
                                System.out.println("Deseja finalizar o pedido?");
                                System.out.println("Total: R$ " + total);

                                simOuNao = simOuNao();

                                if (simOuNao == 1) {
                                    System.out.println("Pedido Realizado!.\n");
                                    mesaPedidosFuncionarios = adicionarMesaPedido(mesas, listaDeCompras, total);
                                    listaDeCompras.clear();

                                    menuAdicionarProduto = false;
                                    menuFuncionario = false;
                                } else if (simOuNao == 2) {
                                    System.out.println("Deseja apagar o pedido?");

                                    simOuNao = simOuNao();

                                    if (simOuNao == 1) {
                                        listaDeCompras.clear();
                                        menuAdicionarProduto = false;
                                        menuFuncionario = false;
                                    } else if (simOuNao == 2) {
                                        opcaoRetorno = 1;
                                        menuAdicionarProduto = false;
                                        menuFuncionario = false;
                                    }
                                }
                            }
                        } while (menuAdicionarProduto);
                    } else if (opcaoMenuFuncionario == 3) {
                        do {
                            System.out.println("EDITAR PERFIL:");
                            System.out.println(" ");

                            System.out.println(funcionarios.get(id_LoginFuncionario));

                            System.out.println("\nTem certeza que quer editar " + (id_LoginFuncionario) + "?\n");

                            System.out.println("""
                                    1 | SIM
                                    2 | NÃO
                                    3 | VOLTAR""");

                            int opcaoEditar = opcaoPadraoUmATres();

                            if (opcaoEditar == 1) {
                                editarFuncionario(funcionarios, id_LoginFuncionario);

                                editarPerfil = false;
                            } else if (opcaoEditar == 3) {
                                editarPerfil = false;
                            }
                        } while (editarPerfil);
                        menuFuncionario = false;
                    }
                } while (menuFuncionario);

            }

            //TELA ADMINISTRADOR ---------------------------------------------------------------------------------------
            int opcaoMenuAdmin, opcaoGerenciamentoFuncionarios, opcaoGerenciamentoClientes, opcaoGerenciamentoProdutos, opcaoGerenciamentoIngredientes;
            int idFuncionario = 0, idProduto, idIngrediente;
            boolean menuAdmin = true, menuAdminFuncionario = true, menuAdminCliente = true, menuAdminProdutos = true, menuAdminIngredientes = true;

            while (telaAdmin) {
                System.out.println("\nADMIN: ");
                opcaoMenuAdmin = opcaoMenuAdmin();

                if (opcaoMenuAdmin == 5) {
                    System.out.println("Deslogado com sucesso!");
                    telaAdmin = false;
                    telaMenu = true;
                }

                do {//LOOP MENU ADMINISTRADOR

                    if (opcaoMenuAdmin == 1) {//1 | GERENCIAR FUNCIONÁRIOS
                        int totalFuncionarios = tamanhoFuncionarioDAO();
                        do {
                            System.out.println("""
                                    1 | LISTA DE FUNCIONÁRIOS
                                    2 | ADICIONAR FUNCIONÁRIO
                                    3 | EDITAR FUNCIONÁRIO
                                    4 | REMOVER FUNCIONÁRIO
                                    5 | VOLTAR""");

                            opcaoGerenciamentoFuncionarios = opcaoPadraoUmACinco();

                            if (opcaoGerenciamentoFuncionarios == 1) {//1 | LISTA DE FUNCIONÁRIOS
                                System.out.println("LISTA DE FUNCIONÁRIOS: ");
                                funcionariosDAO();

                                System.out.println("1 | VOLTAR");

                                opcaoPadraoUm();

                                menuAdminFuncionario = false;//ENCERRA LOOP E VOLTA PARA O MENU INICIAL
                            } else if (opcaoGerenciamentoFuncionarios == 2) {//2 | ADICIONAR FUNCIONÁRIO
                                do {//INICIA LOOP DE CADASTRO FUNCIONÁRIO
                                    cadastroBoolean = true;
                                    System.out.println("ADICIONAR FUNCIONÁRIO: ");
                                    Funcionario novoFuncionario = cadastrarFuncionario();

                                    confirmarDadosFuncionarios(novoFuncionario);

                                    simOuNao = simOuNao();

                                    if (simOuNao == 1) {//SIM
                                        System.out.println("Funcionário(a) cadastrado(a) com sucesso!!!!");

                                        funcionarios.add(novoFuncionario);
                                        inserirFuncionarioDAO(novoFuncionario);

                                        cadastroBoolean = false;//Encerra o loop de cadastro de funcionário(a)
                                    }
                                } while (cadastroBoolean);
                            } else if (opcaoGerenciamentoFuncionarios == 3) {//3 | EDITAR FUNCIONÁRIO(A)
                                boolean editarFuncionario = true;
                                int opcao;

                                do {
                                    if (totalFuncionarios == 0) {
                                        System.out.println("Não existem funcionarios disponiveis para editar");
                                        editarFuncionario = false;
                                    } else {
                                        do {
                                            System.out.println("EDITAR FUNCIONÁRIOS:");

                                            funcionariosDAO();

                                            System.out.print("Indique o id do(a) funcionário(a) que deseja editar: ");

                                            idFuncionario = pegarFuncionarioDAO();

                                            System.out.println("\nTem certeza que quer editar o(a) funcionário(a) com id n°" + (idFuncionario) + "?\n");

                                            System.out.println("""
                                                    1 | SIM
                                                    2 | NÃO
                                                    3 | VOLTAR""");

                                            opcao = opcaoPadraoUmATres();

                                            if (opcao == 1) {
                                                editarFuncionario = false;
                                            } else if (opcao == 3) {
                                                break;
                                            }
                                        } while (editarFuncionario);
                                        if (editarFuncionario) {
                                            break;
                                        }
                                        editarFuncionario = true;

                                        Funcionario atualizarFuncionario = updateFuncionario(idFuncionario);

                                        updateFuncionarioDAO(atualizarFuncionario);

                                        System.out.println("Funcionário(a) editado(a) com sucesso!!!!");

                                        if (funcionarios.size() == 1) {
                                            editarFuncionario = false;
                                        } else {
                                            System.out.println("\nDeseja editar outro funcionario?");

                                            simOuNao = simOuNao();

                                            if (simOuNao == 2) {
                                                editarFuncionario = false;
                                            }
                                        }
                                    }
                                } while (editarFuncionario);
                            } else if (opcaoGerenciamentoFuncionarios == 4) {//4 | REMOVER FUNCIONÁRIO
                                boolean removerFuncionarios = true;
                                int opcao;

                                do {
                                    System.out.println("REMOVER FUNCIONÁRIOS: ");

                                    do {

                                        if (totalFuncionarios == 1) {
                                            System.out.println("Não existem funcionarios disponiveis para remover");
                                            removerFuncionarios = false;

                                        } else {
                                            funcionariosDAO();
                                            System.out.println("Digite o n° do id que deseja excluir: ");
                                            idFuncionario = pegarFuncionarioDAO();

                                            if(idFuncionario == 1){
                                                System.out.println("Não existem funcionarios disponiveis para remover");
                                                removerFuncionarios = false;
                                            }else{
                                                System.out.println("Deseja excluir o(a) funcionário(a) com id n°" + (idFuncionario) + "?");

                                                System.out.println("""
                                                    1 | SIM
                                                    2 | NÃO
                                                    3 | VOLTAR""");

                                                opcao = opcaoPadraoUmATres();

                                                if (opcao == 1) {
                                                    removerFuncionarios = false;
                                                } else if (opcao == 3) {
                                                    break;
                                                }
                                            }
                                        }
                                    } while (removerFuncionarios);
                                    if (removerFuncionarios) {
                                        break;
                                    }
                                    removerFuncionarios = true;


                                    removerFuncionarioDAO(idFuncionario);
                                    if (totalFuncionarios == 1) {
                                        removerFuncionarios = false;
                                    } else {
                                        System.out.println("\nDeseja remover outro funcionario?");

                                        simOuNao = simOuNao();

                                        if (simOuNao == 2) {
                                            removerFuncionarios = false;
                                        }
                                    }
                                } while (removerFuncionarios);
                            } else if (opcaoGerenciamentoFuncionarios == 5) {//5 | VOLTAR
                                menuAdminFuncionario = false;
                                menuAdmin = false;
                            }
                        } while (menuAdminFuncionario);
                    } else if (opcaoMenuAdmin == 2) {//2 | GERENCIAR CLIENTES
                        do {
                            System.out.println("""
                                    1 | LISTA DE CLIENTES
                                    2 | VOLTAR""");

                            opcaoGerenciamentoClientes = opcaoPadraoUmADois();

                            if (opcaoGerenciamentoClientes == 1) {
                                System.out.println("CLIENTES: ");
                                clientesDAO();
                                System.out.println("1 | VOLTAR");

                                opcaoPadraoUm();

                                menuAdminCliente = false;
                            } else if (opcaoGerenciamentoClientes == 2) {
                                menuAdminCliente = false;
                                menuAdmin = false;
                            }
                        } while (menuAdminCliente);
                    } else if (opcaoMenuAdmin == 3) {//3 | GERENCIAR PRODUTOS

                        do {
                            int totalProdutos = tamanhoProdutoDAO();
                            System.out.println("""
                                    1 | LISTA DE PRODUTOS
                                    2 | ADICIONAR PRODUTO
                                    3 | EDITAR PRODUTO
                                    4 | REMOVER PRODUTO
                                    5 | VOLTAR""");

                            opcaoGerenciamentoProdutos = opcaoPadraoUmACinco();

                            if (opcaoGerenciamentoProdutos == 1) {//1 | LISTA DE PRODUTOS
                                System.out.println("LISTA DE PRODUTOS: ");
                                produtosDAO();

                                System.out.println("1 | VOLTAR");

                                opcaoPadraoUm();

                                menuAdminProdutos = false;
                            } else if (opcaoGerenciamentoProdutos == 2) {//2 | ADICIONAR PRODUTO
                                do {//INICIA LOOP DE CADASTRO PRODUTO
                                    cadastroBoolean = true;
                                    System.out.println("ADICIONAR PRODUTOS");
                                    Produto novoProduto = cadastrarProduto();

                                    simOuNao = simOuNao();

                                    if (simOuNao == 1) {//SIM
                                        System.out.println("Produto cadastrado(a) com sucesso!!!!");
                                        produtos.add(novoProduto);
                                        inserirProdutoDAO(novoProduto);

                                        cadastroBoolean = false;//Encerra o loop de cadastro de funcionário(a)
                                    }
                                } while (cadastroBoolean);
                            } else if (opcaoGerenciamentoProdutos == 3) {//3 | EDITAR PRODUTO
                                boolean editarProduto = true;
                                int opcao;
                                do {
                                    if (totalProdutos == 0) {
                                        System.out.println("Não existe Produtos para editar");
                                        editarProduto = false;
                                    } else {
                                        do {
                                            System.out.println("EDITAR PRODUTOS:");
                                            produtosDAO();

                                            System.out.print("Indique o id do(a) produto que deseja editar: ");
                                            idProduto = pegarProdutoDAO();

                                            //printar produto especifico
                                            System.out.println("\nTem certeza que quer editar o produto " + (idProduto) + "?\n");

                                            System.out.println("""
                                                    1 | SIM
                                                    2 | NÃO
                                                    3 | VOLTAR""");

                                            opcao = opcaoPadraoUmATres();

                                            if (opcao == 1) {
                                                editarProduto = false;
                                            } else if (opcao == 3) {
                                                break;
                                            }
                                        } while (editarProduto);
                                        if (editarProduto) {
                                            break;
                                        }
                                        editarProduto = true;

                                        Produto atualizarProduto = editarProduto(idProduto);

                                        updateProdutosDAO(atualizarProduto);

                                        System.out.println("\nDeseja editar outro produto?");

                                        simOuNao = simOuNao();

                                        if (simOuNao == 2) {
                                            editarProduto = false;
                                        }
                                    }
                                } while (editarProduto);
                            } else if (opcaoGerenciamentoProdutos == 4) {//4 | REMOVER PRODUTO
                                boolean removerProdutos = true;
                                int id_Produto = 0, opcao;
                                do {
                                    if (totalProdutos == 0) {
                                        System.out.println("Não existem produtos a serem removidos");
                                        removerProdutos = false;
                                    } else {
                                        do {
                                            System.out.println("REMOVER PRODUTOS: ");
                                            produtosDAO();
                                            System.out.println("Digite o n° do id que deseja excluir: ");
                                            idProduto = pegarProdutoDAO();

                                            System.out.println("Deseja excluir o produto com id n°" + (idProduto) + "?");

                                            System.out.println("""
                                                    1 | SIM
                                                    2 | NÃO
                                                    3 | VOLTAR""");

                                            opcao = opcaoPadraoUmATres();

                                            if (opcao == 1) {
                                                removerProdutos = false;
                                            } else if (opcao == 3) {
                                                break;
                                            }
                                        } while (removerProdutos);
                                        if (removerProdutos) {
                                            break;
                                        }
                                        removerProdutos = true;

                                        removerProdutoDAO(idProduto);

                                        System.out.println("\nDeseja remover outro produto?");

                                        simOuNao = simOuNao();

                                        if (simOuNao == 2) {
                                            removerProdutos = false;
                                        }
                                    }
                                } while (removerProdutos);
                            } else if (opcaoGerenciamentoProdutos == 5) {//5 | VOLTAR
                                menuAdminProdutos = false;
                                menuAdmin = false;
                            }
                        } while (menuAdminProdutos);
                    } else if (opcaoMenuAdmin == 4) {//4 | GERENCIAR INGREDIENTES
                        int opcaoPadraoUm = 0;
                        do {
                            int totalIngrediente = tamanhoIngredienteDAO();
                            System.out.println("""
                                    1 | LISTA DE INGREDIENTES
                                    2 | ADICIONAR INGREDIENTE
                                    3 | EDITAR INGREDIENTES
                                    4 | REMOVER INGREDIENTES
                                    5 | VOLTAR""");

                            opcaoGerenciamentoIngredientes = opcaoPadraoUmACinco();

                            if (opcaoGerenciamentoIngredientes == 1) {//1 | LISTA DE INGREDIENTES
                                System.out.println("LISTA DE INGREDIENTES: ");
                                ingredientesDAO();

                                do {
                                    System.out.println("1 | VOLTAR");

                                    opcaoPadraoUm = opcaoPadraoUm();
                                } while (opcaoPadraoUm != 1);

                                menuAdminIngredientes = false;//ENCERRA LOOP E VOLTA PARA O MENU INICIAL
                            } else if (opcaoGerenciamentoIngredientes == 2) {//2 | ADICIONAR INGREDIENTE
                                System.out.println("ADICIONAR INGREDIENTE: ");
                                ingredientesDAO();

                                do {//INICIA LOOP DE CADASTRO PRODUTO
                                    cadastroBoolean = true;

                                    Ingrediente novoIngrediente = cadastrarIngrediente();

                                    confirmarDadosIngredientes(novoIngrediente);

                                    simOuNao = simOuNao();

                                    if (simOuNao == 1) {//SIM
                                        System.out.println("Ingrediente cadastrado(a) com sucesso!!!!");
                                        ingredientes.add(novoIngrediente);
                                        inserirIngredienteDAO(novoIngrediente);

                                        cadastroBoolean = false;//Encerra o loop de cadastro de funcionário(a)
                                    }
                                } while (cadastroBoolean);
                            } else if (opcaoGerenciamentoIngredientes == 3) {//3 | EDITAR INGREDIENTE
                                boolean editarIngrediente = true;
                                int opcao;
                                do {
                                    if (totalIngrediente == 0) {
                                        System.out.println("Não existem ingredientes a serem editados");
                                    } else {
                                        do {
                                            System.out.println("EDITAR INGREDIENTES: ");
                                            ingredientesDAO();

                                            System.out.print("Indique o id do ingrediente que deseja editar: ");
                                            idIngrediente = pegarIngredienteDAO();

                                            System.out.println("Tem certeza que quer editar o ingrediente " + (idIngrediente) + "?\n");

                                            System.out.println("""
                                                    1 | SIM
                                                    2 | NÃO
                                                    3 | VOLTAR""");

                                            opcao = opcaoPadraoUmATres();

                                            if (opcao == 1) {
                                                editarIngrediente = false;
                                            } else if (opcao == 3) {
                                                break;
                                            }
                                        } while (editarIngrediente);
                                        if (editarIngrediente) {
                                            break;
                                        }
                                        editarIngrediente = true;

                                        Ingrediente atualizarIngrediente = editarIngrediente(idIngrediente);
                                        updateIngredienteDAO(atualizarIngrediente);

                                        System.out.println("\nDeseja editar outro ingrediente?");

                                        simOuNao = simOuNao();

                                        if (simOuNao == 2) {
                                            editarIngrediente = false;
                                        }
                                    }

                                } while (editarIngrediente);
                            } else if (opcaoGerenciamentoIngredientes == 4) {//4 | REMOVER INGREDIENTE
                                boolean removerIngrediente = true;
                                int id_Ingrediente = 0, opcao;

                                do {
                                    if (ingredientes.isEmpty()) {
                                        System.out.println("Não existem ingredientes a serem removidos");
                                        removerIngrediente = false;
                                    } else {
                                        do {
                                            System.out.println("REMOVER INGREDIENTES: ");
                                            ingredientesDAO();
                                            System.out.println("Digite o n° do id que deseja excluir: ");
                                            idIngrediente = pegarIngredienteDAO();

                                            System.out.println("Deseja excluir o ingrediente com id n°" + (idIngrediente) + "?");

                                            System.out.println("""
                                                    1 | SIM
                                                    2 | NÃO
                                                    3 | VOLTAR""");

                                            opcao = opcaoPadraoUmATres();

                                            if (opcao == 1) {
                                                removerIngrediente = false;
                                            } else if (opcao == 3) {
                                                break;
                                            }
                                        } while (removerIngrediente);
                                        if (removerIngrediente) {
                                            break;
                                        }
                                        removerIngrediente = true;

                                        removerIngredienteDAO(idIngrediente);

                                        System.out.println("\nDeseja remover outro ingrediente?");

                                        simOuNao = simOuNao();

                                        if (simOuNao == 2) {
                                            removerIngrediente = false;
                                        }
                                    }
                                } while (removerIngrediente);
                            } else if (opcaoGerenciamentoIngredientes == 5) {//5 | VOLTAR
                                menuAdminIngredientes = false;
                                menuAdmin = false;
                            }
                        } while (menuAdminIngredientes);

                    } else if (opcaoMenuAdmin == 5) {//5 | OPÇÃO PARA VOLTAR
                        menuAdmin = false;//encerra o loop e volta para a menu inicial
                    }
                } while (menuAdmin);
            }
        } while (true);
    }
}