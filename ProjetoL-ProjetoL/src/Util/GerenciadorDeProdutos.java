package Util;

import Entidades.Produto;

import static BancoDeDados.ProdutoDAO.*;
import static BancoDeDados.ProdutoIngredienteDAO.selecionarIngrediente;
import static Util.RevisarOpcao.simOuNao;

import java.util.List;
import java.util.Scanner;

public class GerenciadorDeProdutos {

    public static void confirmarDadosProdutos(Produto produto) {
        System.out.println("Os dados do produto estão corretos?"
                + "\nId_produto: " + produto.id_produto + "\n"
                + "Nome: " + produto.nome + "\n"
                + "Estoque: " + produto.estoque + "\n"
                + "Preço: " + produto.preco + "\n");
    }

    public static Produto cadastrarProduto() {
        Produto novoProduto = new Produto();
        Scanner scanner = new Scanner(System.in);

        String nome = checkadordeProdutosDAO(novoProduto);
        novoProduto.estoque = 0;

        do {
            try {
                System.out.print("Digite o preço do produto: ");
                String novoPrecoString = scanner.nextLine();

                Double.parseDouble(novoPrecoString);

                if (!novoPrecoString.isEmpty()) {
                    novoProduto.preco = Double.parseDouble(novoPrecoString);
                    break;
                } else if (novoProduto.preco < 0) {
                    System.out.println("Preço não pode ser menor que 0! Digite novamente.");
                }
            } catch (NumberFormatException erro) {
                System.out.println("[ERROR]");
            }
        } while (true);

        System.out.println("\nDeseja adicionar o produto?");

        return novoProduto;
    }



    private static void checkadordeProdutos(Produto novoProduto, List<Produto> ProdutosExistentes) {
        Scanner scanner = new Scanner(System.in);
        boolean produtoExistente;

        do {
            produtoExistente = false;

            System.out.print("Digite o nome do produto: ");
            novoProduto.nome = scanner.nextLine().trim();

            if (novoProduto.nome.isEmpty()) {
                System.out.println("Produto não pode ser vazio ou apenas espaços! Digite novamente.");
                continue;
            }

            for (Produto produto : ProdutosExistentes) {
                if (produto.nome.equalsIgnoreCase(novoProduto.nome)) {
                    produtoExistente = true;
                    System.out.println("Nome já cadastrado. Tente novamente.");
                    break;
                } else {
                    break;
                }
            }

        } while (produtoExistente);
    }

    public static int pegarProdutos(List<Produto> produtos) {
        Scanner scanner = new Scanner(System.in);
        int indexProduto = 0;
        do {
            indexProduto = scanner.nextInt();
            if (indexProduto == 1) {
                indexProduto--;
                break;
            }

            indexProduto--;
            if ((produtos.size() < indexProduto) || (0 >= indexProduto) || indexProduto >= produtos.size()) {
                System.out.println("Não existe Produto com esse id! Digite novamente.");
            }
        } while ((produtos.size() < indexProduto) || (0 >= indexProduto) || indexProduto >= produtos.size());
        return indexProduto;
    }

    public static void removerProdutos(List<Produto> produtos, int indexProduto) {
        if (indexProduto >= 0 && indexProduto < produtos.size()) {
            produtos.remove(indexProduto);
            for (int i = 0; i < produtos.size(); i++) {
                produtos.get(i).id_produto = i + 1;
            }
            System.out.println("Produto removido com sucesso!");
        } else {
            System.out.println("Não existe produto com esse ID!");
        }
    }


    public static Produto editarProduto(int id) {
        Scanner scanner = new Scanner(System.in);
        Produto produtoAtual = new Produto();
        int simOuNao;

        produtoAtual.id_produto = id;
        checkadordeProdutosDAO(produtoAtual);

        do {
            try {
                System.out.print("Digite o novo estoque do produto: ");
                String estque = scanner.nextLine();

                Integer.parseInt(estque);

                if (!estque.isEmpty()) {
                    System.out.println(estque);
                    System.out.println("\nConfirmar novo estoque?");
                    simOuNao = simOuNao();
                    if (simOuNao == 1) {
                        produtoAtual.estoque = Integer.parseInt(estque);
                        break;
                    }
                }
            } catch (NumberFormatException erro) {
                System.out.println("[ERROR]");
            }
        } while(true);

        System.out.println("\nProduto atual: " + produtoAtual);

        do {

            try {
                System.out.print("Digite o novo preço do produto: ");
                String preco = scanner.nextLine();

                Double.parseDouble(preco);

                if (!preco.isEmpty()) {
                    System.out.println(preco);
                    System.out.println("\nConfirmar novo preço?");
                    simOuNao = simOuNao();
                    if (simOuNao == 1) {
                        produtoAtual.preco = Double.parseDouble(preco);
                        break;
                    }
                }
            } catch (NumberFormatException erro) {
                System.out.println("[ERROR]");
            }
        } while (true);
        System.out.println("Produto editado com sucesso!");
        return produtoAtual;

    }

}
