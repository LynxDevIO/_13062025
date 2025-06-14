package pucgo.poobd._13062025.model;

import java.util.List;

public class Supervisor extends Funcionario {
    private long idSupervisor;
    private List<Vendedor> vendedoresSupervisionados;

    public Supervisor() {}

    public Supervisor(long id, String nome, String cpf, String telefone, Endereco endereco, Empresa empresa, long idSupervisor) {
        super(id, nome, cpf, telefone, endereco, empresa);
        this.idSupervisor = idSupervisor;
    }

    public long getIdSupervisor() {
        return idSupervisor;
    }

    public void setIdSupervisor(long idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    public List<Pedido> pedidosAcimaValorLimite(double valorLimite) {
        return List.of(); // TODO
    }
}
