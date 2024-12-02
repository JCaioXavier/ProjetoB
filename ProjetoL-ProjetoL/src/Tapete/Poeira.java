package Tapete;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import BancoDeDados.*;
import Entidades.*;

import static BancoDeDados.CarrinhoIngredienteDAO.*;
import static BancoDeDados.CarrinhoDAO.*;
import static BancoDeDados.MesaDAO.mesaDAO;
import static BancoDeDados.PedidoDAO.*;
import static BancoDeDados.IngredienteDAO.*;
import static BancoDeDados.ProdutoDAO.*;
import static BancoDeDados.ClienteDAO.*;
import static BancoDeDados.FuncionarioDAO.*;
import static BancoDeDados.ProdutoIngredienteDAO.*;
import static Util.DadosEstaticos.*;
import static Util.GerenciadorDeClientes.*;
import static Util.GerenciadorDeFuncionarios.*;
import static Util.GerenciadorDeIngredientes.*;
import static Util.GerenciadorDeProdutos.*;
import static Util.RevisarOpcao.*;

public class Poeira {
    public static void Poeira() {
        Scanner scanner = new Scanner(System.in);
        List<Funcionario> funcionarios = gerarFuncionarios();
        List<Cliente> clientes = gerarClientes();
        List<Produto> produtos = gerarProduto();
        List<Ingrediente> ingredientes = gerarIngrediente();
        List<Carrinho> listaDeCompras = gerarCarrinho();
        List<HistoricoPedido> listaPedidosClientes = new ArrayList<>();
        List<Mesa> mesaPedidosFuncionarios = new ArrayList<>();
        FuncionarioDAO funcionarioDAO;

        int opcaoLogin, opcaoCadastro, opcaoMenu, simOuNao;
        int idLoginCliente = 0, idLoginFuncionario = 0;

        boolean menuBoolean = true, cadastroBoolean;

        boolean telaMenu = true, telaCliente = false, telaFuncionario = false, telaAdmin = false;

        do {
            do {
                opcaoMenu = opcaoMenu();

                do {
                    if (opcaoMenu == 1) {
                        System.out.println("LOGAR COMO:");
                        opcaoLogin = opcaoLogin();

                        if (opcaoLogin == 1) {
                            Cliente novocliente = new Cliente();

                            idLoginCliente = loginClienteDAO(novocliente);

                            menuBoolean = false;
                            telaMenu = false;
                            telaCliente = true;

                        } else if (opcaoLogin == 2) {
                            Funcionario novoFuncionario = new Funcionario();

                            idLoginFuncionario = loginFuncionarioDAO(novoFuncionario);

                            menuBoolean = false;
                            telaMenu = false;

                            if (idLoginFuncionario == 1) {
                                telaAdmin = true;
                            } else {
                                telaFuncionario = true;
                            }

                        } else if (opcaoLogin == 3) {
                            menuBoolean = false;
                        }
                    } else if (opcaoMenu == 2) {
                        System.out.println("CADASTRAR COMO:");
                        opcaoCadastro = opcaoCadastro();

                        if (opcaoCadastro == 1) {
                            do {
                                cadastroBoolean = true;

                                Cliente novoCliente = cadastrarClienteDAO();

                                simOuNao = simOuNao();

                                if (simOuNao == 1) {
                                    clientes.add(novoCliente);
                                    inserirClienteDAO(novoCliente);

                                    cadastroBoolean = false;
                                }
                            } while (cadastroBoolean);
                            menuBoolean = false;
                        } else if (opcaoCadastro == 2) {
                            menuBoolean = false;
                        }
                    } else if (opcaoMenu == 3) {
                        System.out.println("Encerrando o sistema.");
                        return;
                    }
                } while (menuBoolean);
            } while (telaMenu);

            //TELA DO CLIENTE ------------------------------------------------------------------------------------------
            double total = 0, subtrairTotal = 0;
            int opcaoMenuCliente, opcaoRetorno = 0, idPedido = 0, idPedido1 = 0, a = 0, original, opcaoVoltarBoolean, idCarrinho = 0;
            boolean menuCliente = true, menuAdicionarProduto = true, editarPerfil = true, historicoDePedido = true, adicionarProduto = true,
                    opcaoVoltar = false, opcaoRetornoEditar = false;
            List<HistoricoPedido> historicoPedidos = new ArrayList<>();
            DetalhePedido novoDetalhePedido = new DetalhePedido();
            Pedido novoPedido = new Pedido();
            Carrinho novoCarrinho = new Carrinho();
            List<Carrinho> listaCarrinho = gerarCarrinho();

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
                    if (opcaoMenuCliente == 1) {
                        if(opcaoVoltar){
                            System.out.println("Deseja retornar o pedido que você não finalizou?");

                            opcaoVoltarBoolean = simOuNao();

                            if(opcaoVoltarBoolean == 2){
                                apagarCarrinhoClienteDAO(idLoginCliente);
                            }
                        }

                        do {
                            menuCliente = true;
                            adicionarProduto = true;

                            if(opcaoVoltar){
                                adicionarProduto = false;
                                opcaoVoltar = false;
                            }

                            while (adicionarProduto) {

                                adicionarCarrinhoClienteDAO(idLoginCliente);

                                carrinhoClienteDAO(idLoginCliente);

                                System.out.println("Deseja adicionar outro item?");
                                System.out.println("""
                                        1 | SIM
                                        2 | NÃO
                                        3 | VOLTAR""");

                                int opcao = opcaoPadraoUmATres();

                                if (opcao == 2) {
                                    adicionarProduto = false;
                                } else if (opcao == 3) {
                                    opcaoVoltar = true;
                                    adicionarProduto = false;
                                    menuCliente = false;
                                }
                            }

                            if (!menuCliente) {
                                break;
                            }

                            int retorno = carrinhoClienteDAO(idLoginCliente);

                            if(retorno == 1){
                                break;
                            }

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
                                    carrinhoClienteDAO(idLoginCliente);
                                    subtrairTotal = removerItemCarrinhoClienteDAO(idLoginCliente);//
                                    totalCarrinhoSubtrairClienteDAO(idLoginCliente, subtrairTotal);
                                    opcaoVoltar = true;
                                }
                            } else {
                                total = totalCarrinhoClienteDAO(idLoginCliente);
                                carrinhoClienteDAO(idLoginCliente);
                                System.out.println("Deseja finalizar o pedido?");
                                System.out.println("Total: R$ " + total);

                                simOuNao = simOuNao();

                                if (simOuNao == 1) {
                                    System.out.println("Pedido Realizado!");

                                    finalizarCarrinhoClienteDAO(idLoginCliente, total);
                                    finalizarCarrinhoIngrediente();

                                    menuAdicionarProduto = false;
                                    menuCliente = false;
                                } else if (simOuNao == 2) {
                                    System.out.println("Deseja apagar o pedido?");

                                    simOuNao = simOuNao();

                                    if (simOuNao == 1) {
                                        apagarCarrinhoClienteDAO(idLoginCliente);

                                        menuAdicionarProduto = false;
                                        menuCliente = false;
                                    } else if (simOuNao == 2) {
                                        opcaoVoltar = true;
                                        menuAdicionarProduto = false;
                                        menuCliente = false;
                                    }
                                }
                            }
                        } while (menuAdicionarProduto);
                    } else if (opcaoMenuCliente == 2) {
                        int opcao;
                        int tamanhoPedido = tamanhoPedidoDAO(idLoginCliente);
                        do {
                            System.out.println("""
                                    1 | HISTÓRICO DE PEDIDOS
                                    2 | VOLTAR""");

                            opcao = opcaoPadraoUmADois();

                            if (opcao == 1) {
                                if (tamanhoPedido == 0) {
                                    System.out.println("Nenhum pedido cadastrado.");
                                } else {
                                    pedidosDAO(idLoginCliente);
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
                    } else if (opcaoMenuCliente == 3) {
                        do {
                            System.out.println("EDITAR PERFIL:");

                            perfilCliente(idLoginCliente);

                            System.out.println("\nTem certeza que quer editar o perfil?\n");

                            System.out.println("""
                                    1 | SIM
                                    2 | NÃO
                                    3 | VOLTAR""");

                            int opcaoEditar = opcaoPadraoUmATres();

                            if (opcaoEditar == 1) {
                                Cliente updateCliente = editarCliente(idLoginCliente);
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
                    if (opcaoMenuFuncionario == 1) {
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
                                    mesaDAO();
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
                            if(opcaoVoltar){
                                System.out.println("Deseja retornar o pedido que você não finalizou?");

                                opcaoVoltarBoolean = simOuNao();

                                if(opcaoVoltarBoolean == 2){
                                    apagarCarrinhoFuncionarioDAO(idLoginFuncionario);
                                }
                            }

                            menuFuncionario = true;
                            adicionarProduto = true;

                            if(opcaoVoltar){
                                adicionarProduto = false;
                                opcaoVoltar = false;
                            }

                            while (adicionarProduto) {

                                adicionarCarrinhoFuncionarioDAO(idLoginFuncionario);

                                carrinhoFuncionarioDAO(idLoginFuncionario);

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

                            int retorno = carrinhoFuncionarioDAO(idLoginFuncionario);

                            if(retorno == 1){
                                break;
                            }

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
                                    carrinhoFuncionarioDAO(idLoginFuncionario);
                                    subtrairTotal = removerItemCarrinhoFuncionarioDAO(idLoginFuncionario);
                                    totalCarrinhoSubtrairFuncionarioDAO(idLoginFuncionario, subtrairTotal);
                                    opcaoVoltar = true;
                                }
                            } else {
                                total = totalCarrinhoFuncionarioDAO(idLoginFuncionario);
                                carrinhoFuncionarioDAO(idLoginFuncionario);
                                System.out.println("Deseja finalizar o pedido?");
                                System.out.println("Total: R$ " + total);

                                simOuNao = simOuNao();

                                if (simOuNao == 1) {
                                    System.out.println("Pedido Realizado!");
                                    finalizarCarrinhoFuncionarioDAO(idLoginFuncionario, total);
                                    menuAdicionarProduto = false;
                                    menuFuncionario = false;
                                } else if (simOuNao == 2) {
                                    System.out.println("Deseja apagar o pedido?");

                                    simOuNao = simOuNao();

                                    if (simOuNao == 1) {
                                        apagarCarrinhoFuncionarioDAO(idLoginFuncionario);

                                        menuAdicionarProduto = false;
                                        menuFuncionario = false;
                                    } else if (simOuNao == 2) {
                                        opcaoVoltar = true;
                                        menuAdicionarProduto = false;
                                        menuFuncionario = false;
                                    }
                                }
                            }
                        } while (menuAdicionarProduto);
                    } else if (opcaoMenuFuncionario == 3) {
                        do {
                            System.out.println("EDITAR PERFIL:");

                            perfilFuncionario(idLoginFuncionario);

                            System.out.println("\nTem certeza que quer editar " + (idLoginFuncionario) + "?\n");

                            System.out.println("""
                                    1 | SIM
                                    2 | NÃO
                                    3 | VOLTAR""");

                            int opcaoEditar = opcaoPadraoUmATres();

                            if (opcaoEditar == 1) {
                                Funcionario updateFuncionario = editarFuncionario(idLoginFuncionario);
                                updateFuncionarioDAO(updateFuncionario);

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

                do {

                    if (opcaoMenuAdmin == 1) {
                        int totalFuncionarios = tamanhoFuncionarioDAO();
                        do {
                            System.out.println("""
                                    1 | LISTA DE FUNCIONÁRIOS
                                    2 | ADICIONAR FUNCIONÁRIO
                                    3 | EDITAR FUNCIONÁRIO
                                    4 | REMOVER FUNCIONÁRIO
                                    5 | VOLTAR""");

                            opcaoGerenciamentoFuncionarios = opcaoPadraoUmACinco();

                            if (opcaoGerenciamentoFuncionarios == 1) {
                                System.out.println("LISTA DE FUNCIONÁRIOS: ");
                                funcionariosDAO();

                                System.out.println("1 | VOLTAR");

                                opcaoPadraoUm();

                                menuAdminFuncionario = false;
                            } else if (opcaoGerenciamentoFuncionarios == 2) {
                                do {
                                    cadastroBoolean = true;
                                    System.out.println("ADICIONAR FUNCIONÁRIO: ");
                                    Funcionario novoFuncionario = cadastrarFuncionarioDAO();

                                    simOuNao = simOuNao();

                                    if (simOuNao == 1) {//SIM
                                        System.out.println("Funcionário(a) cadastrado(a) com sucesso!!!!");

                                        funcionarios.add(novoFuncionario);
                                        inserirFuncionarioDAO(novoFuncionario);

                                        cadastroBoolean = false;
                                    }
                                } while (cadastroBoolean);
                            } else if (opcaoGerenciamentoFuncionarios == 3) {
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
                            } else if (opcaoGerenciamentoFuncionarios == 5) {
                                menuAdminFuncionario = false;
                                menuAdmin = false;
                            }
                        } while (menuAdminFuncionario);
                    } else if (opcaoMenuAdmin == 2) {
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
                    } else if (opcaoMenuAdmin == 3) {

                        do {
                            int totalProdutos = tamanhoProdutoDAO();
                            System.out.println("""
                                    1 | LISTA DE PRODUTOS
                                    2 | ADICIONAR PRODUTO
                                    3 | EDITAR PRODUTO
                                    4 | REMOVER PRODUTO
                                    5 | VOLTAR""");

                            opcaoGerenciamentoProdutos = opcaoPadraoUmACinco();

                            if (opcaoGerenciamentoProdutos == 1) {
                                System.out.println("LISTA DE PRODUTOS: ");
                                produtosDAO();

                                System.out.println("1 | VOLTAR");

                                opcaoPadraoUm();

                                menuAdminProdutos = false;
                            } else if (opcaoGerenciamentoProdutos == 2) {
                                do {
                                    cadastroBoolean = true;
                                    System.out.println("\nADICIONAR PRODUTOS:");
                                    Produto novoProduto = cadastrarProduto();

                                    simOuNao = simOuNao();

                                    if (simOuNao == 1) {//SIM
                                        produtos.add(novoProduto);
                                        inserirProdutoDAO(novoProduto);

                                        System.out.println("\nEsse produto tem algum ingrediente?");
                                        simOuNao = simOuNao();
                                        if (simOuNao == 1){
                                            selecionarIngrediente(novoProduto.nome);
                                        }else {
                                            idProduto = pegarIdProduto(novoProduto.nome);
                                            estoque(novoProduto,idProduto);
                                        }

                                        cadastroBoolean = false;
                                    }
                                } while (cadastroBoolean);
                            } else if (opcaoGerenciamentoProdutos == 3) {
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
                            } else if (opcaoGerenciamentoProdutos == 4) {
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
                            } else if (opcaoGerenciamentoProdutos == 5) {
                                menuAdminProdutos = false;
                                menuAdmin = false;
                            }
                        } while (menuAdminProdutos);
                    } else if (opcaoMenuAdmin == 4) {
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

                            if (opcaoGerenciamentoIngredientes == 1) {
                                System.out.println("LISTA DE INGREDIENTES: ");
                                ingredientesDAO();

                                do {
                                    System.out.println("1 | VOLTAR");

                                    opcaoPadraoUm = opcaoPadraoUm();
                                } while (opcaoPadraoUm != 1);

                                menuAdminIngredientes = false;
                            } else if (opcaoGerenciamentoIngredientes == 2) {
                                System.out.println("ADICIONAR INGREDIENTE: ");
                                ingredientesDAO();

                                do {
                                    cadastroBoolean = true;

                                    Ingrediente novoIngrediente = cadastrarIngrediente();

                                    confirmarDadosIngredientes(novoIngrediente);

                                    simOuNao = simOuNao();

                                    if (simOuNao == 1) {
                                        System.out.println("Ingrediente cadastrado(a) com sucesso!!!!");
                                        ingredientes.add(novoIngrediente);
                                        inserirIngredienteDAO(novoIngrediente);

                                        cadastroBoolean = false;
                                    }
                                } while (cadastroBoolean);
                            } else if (opcaoGerenciamentoIngredientes == 3) {
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
                            } else if (opcaoGerenciamentoIngredientes == 4) {
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
                            } else if (opcaoGerenciamentoIngredientes == 5) {
                                menuAdminIngredientes = false;
                                menuAdmin = false;
                            }
                        } while (menuAdminIngredientes);

                    } else if (opcaoMenuAdmin == 5) {
                        menuAdmin = false;
                    }
                } while (menuAdmin);
            }
        } while (true);
    }
}