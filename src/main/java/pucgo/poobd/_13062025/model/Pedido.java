package pucgo.poobd._13062025.model;

import java.util.List;

public class Pedido {
    private long id;
    private long empresa;
    private long vendedor;
    private long cliente;
    private String descricao;
    private boolean aprovado;
    private List<ItemPedido> itens;
    private long formaPagamento;
    private double valorTotal;

    public Pedido() {
    }

    public Pedido(long id, long empresa, long vendedor, long cliente, String descricao, boolean aprovado, List<ItemPedido> itens, long formaPagamento, double valorTotal) {
        this.id = id;
        this.empresa = empresa;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.descricao = descricao;
        this.aprovado = aprovado;
        this.itens = itens;
        this.formaPagamento = formaPagamento;
        this.valorTotal = valorTotal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(long empresa) {
        this.empresa = empresa;
    }

    public long getVendedor() {
        return vendedor;
    }

    public void setVendedor(long vendedor) {
        this.vendedor = vendedor;
    }

    public long getCliente() {
        return cliente;
    }

    public void setCliente(long cliente) {
        this.cliente = cliente;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public long getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(long formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
