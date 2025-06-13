package pucgo.poobd._13062025.model;

public class Vendedor extends Funcionario {
    private long idVendedor;

    public Vendedor() {
    }

    public Vendedor(long id, long idVendedor, String nome, String cpf, String telefone, Endereco endereco) {
        super(id, nome, cpf, telefone, endereco);
        this.idVendedor = idVendedor;
    }

    public long getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(long idVendedor) {
        this.idVendedor = idVendedor;
    }
}
