package pucgo.poobd._13062025.model;

public abstract class Funcionario {
    private long id;
    private String nome;
    private String cpf;
    private String telefone;
    private Endereco endereco;
    private Empresa empresa;

    public Funcionario() {
    }

    public Funcionario(long id, String nome, String cpf, String telefone, Endereco endereco, Empresa empresa) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
        this.empresa = empresa;
    }

    public void aprovarPedido(Pedido pedido) {
        if (pedido != null) {
            if (pedido.isAprovado()) {
                pedido.setAprovado(true);
            } else {
                System.out.println("Tentou-se aprovar um pedido já aprovado. Número do pedido: " + pedido.getId());
            }
        } else {
            System.out.println("Erro ao processar pedido. Objeto PedidoDAO está nulo.");
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
