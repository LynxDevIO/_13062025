package pucgo.poobd._13062025.model;

import java.util.List;

public class Pedido {
    private long id;
    private Empresa empresa;
    private Vendedor vendedor;
    private Cliente cliente;
    private String descricao;
    private boolean aprovado;
    private List<ItemPedido> itens;
    private FormaPagamento formaPagamento;
    private double valorTotal;

    public Pedido() {
    }

    public Pedido(long id, Empresa empresa, Vendedor vendedor, Cliente cliente, String descricao, boolean aprovado, List<ItemPedido> itens, FormaPagamento formaPagamento, double valorTotal) {
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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
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

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
