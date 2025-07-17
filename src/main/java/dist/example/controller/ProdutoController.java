package dist.example.controller;


import dist.example.dto.ProdutoDTO;
import dist.example.model.Produto;
import dist.example.service.ProdutoService;
import dist.middleware.annotations.*;

import java.util.Collection;

@RemoteObject("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService = ProdutoService.getInstance();

    @MethodMapping(path = "", method = HttpMethod.GET)
    public Collection<Produto> buscarTodos() {
        return produtoService.buscarTodos();
    }

    @MethodMapping(path = "/{id}", method = HttpMethod.GET)
    public Produto buscarPorId(@Path(name = "id") String id) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto com ID " + id + " nao encontrado.");
        }
        return produto;
    }

    @MethodMapping(path = "/filtrar", method = HttpMethod.GET)
    public Collection<Produto> buscarPorPreco(@Param(name = "precoMax") double precoMax) {
        return produtoService.buscarPorPrecoMaximo(precoMax);
    }

    @MethodMapping(path = "", method = HttpMethod.POST)
    public Produto criarProduto(@RequestBody ProdutoDTO dto) {
        return produtoService.criarProdutoPublic(dto);
    }

    @MethodMapping(path = "/{id}", method = HttpMethod.PUT)
    public Produto atualizarProduto(@Path(name = "id") String id, @RequestBody ProdutoDTO dto) {
        Produto produto = produtoService.atualizarProduto(id, dto);
        if (produto == null) {
            throw new IllegalArgumentException("Produto com ID " + id + " nao encontrado para atualizar.");
        }
        return produto;
    }

    @MethodMapping(path = "/{id}", method = HttpMethod.DELETE)
    public Produto deletarProduto(@Path(name = "id") String id) {
        Produto produto = produtoService.deletarProduto(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto com ID " + id + " nao encontrado para deletar.");
        }
        return produto;
    }
}