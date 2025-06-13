package pucgo.poobd._13062025.model;

import java.util.List;

public class Supervisor extends Funcionario {
    private long idSupervisor;
    private List<Vendedor> vendedoresSupervisionados;

    public List<Pedido> pedidosAcimaValorLimite(double valorLimite) {
        return List.of(); // TODO
    }
}
