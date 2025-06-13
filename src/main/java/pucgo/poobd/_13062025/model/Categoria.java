package pucgo.poobd._13062025.model;

import java.util.List;

public class Categoria {
    private long id;
    private String descricao;
    private List<Subcategoria> subcategorias;

    public Categoria() {
    }

    public Categoria(long id, String descricao, List<Subcategoria> subcategorias) {
        this.id = id;
        this.descricao = descricao;
        this.subcategorias = subcategorias;
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

    public List<Subcategoria> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<Subcategoria> subcategorias) {
        this.subcategorias = subcategorias;
    }
}
