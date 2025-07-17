package dist.example.dto;

public class ProdutoDTO {
    private String nome;
    private double preco;
    private int estoque;

    public ProdutoDTO(String mouseGamer, double v, int i) {
    }

    // Getters
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getEstoque() { return estoque; }
}