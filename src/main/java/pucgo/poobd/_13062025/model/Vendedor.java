package pucgo.poobd._13062025.model;

public class Vendedor extends Funcionario {
    private long idVendedor;
    private Supervisor supervisor;

    public Vendedor() {
    }

    public Vendedor(long id, String nome, String cpf, String telefone, Endereco endereco, Empresa empresa, long idVendedor, Supervisor supervisor) {
        super(id, nome, cpf, telefone, endereco, empresa);
        this.idVendedor = idVendedor;
        this.supervisor = supervisor;
    }

    public long getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(long idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }
}
