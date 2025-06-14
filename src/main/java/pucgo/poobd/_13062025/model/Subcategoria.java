package pucgo.poobd._13062025.model;

public class Subcategoria {
    private long id;
    private String descricao;
    private Categoria categoria;

    public Subcategoria() {
    }

    public Subcategoria(long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Subcategoria(long id, String descricao, Categoria categoria) {
        this.id = id;
        this.descricao = descricao;
        this.categoria = categoria;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
