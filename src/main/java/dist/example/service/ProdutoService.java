package dist.example.service;


import dist.example.dto.ProdutoDTO;
import dist.example.model.Produto;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProdutoService {
    private static final ProdutoService instance = new ProdutoService();
    private final Map<String, Produto> produtos = new ConcurrentHashMap<>();

    private ProdutoService() {
        // Adiciona alguns produtos de exemplo para facilitar os testes
        criarProduto(new ProdutoDTO("Laptop Pro", 7500.0, 10));
        criarProduto(new ProdutoDTO("Mouse Gamer", 350.0, 50));
    }

    private Produto criarProduto(ProdutoDTO dto) {
        String id = UUID.randomUUID().toString();
        Produto novoProduto = new Produto(id, dto.getNome(), dto.getPreco(), dto.getEstoque());
        produtos.put(id, novoProduto);
        return novoProduto;
    }

    public static ProdutoService getInstance() { return instance; }

    public Collection<Produto> buscarTodos() {
        return produtos.values();
    }

    public Produto buscarPorId(String id) {
        return produtos.get(id);
    }

    public Collection<Produto> buscarPorPrecoMaximo(double precoMax) {
        return produtos.values().stream()
                .filter(p -> p.getPreco() <= precoMax)
                .collect(Collectors.toList());
    }

    public Produto criarProdutoPublic(ProdutoDTO dto) {
        return criarProduto(dto);
    }

    public Produto atualizarProduto(String id, ProdutoDTO dto) {
        Produto produtoExistente = produtos.get(id);
        if (produtoExistente != null) {
            produtoExistente.setNome(dto.getNome());
            produtoExistente.setPreco(dto.getPreco());
            produtoExistente.setEstoque(dto.getEstoque());
            return produtoExistente;
        }
        return null;
    }

    public Produto deletarProduto(String id) {
        return produtos.remove(id);
    }
}