package pucgo.poobd._13062025.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pucgo.poobd._13062025.dao.ClienteDAO;
import pucgo.poobd._13062025.dao.EmpresaDAO;
import pucgo.poobd._13062025.dao.FormaPagamentoDAO;
import pucgo.poobd._13062025.dao.PedidoDAO;
import pucgo.poobd._13062025.dao.VendedorDAO;
import pucgo.poobd._13062025.model.Cliente;
import pucgo.poobd._13062025.model.Empresa;
import pucgo.poobd._13062025.model.FormaPagamento;
import pucgo.poobd._13062025.model.Pedido;
import pucgo.poobd._13062025.model.Vendedor;
import pucgo.poobd._13062025.view.ClienteView;
import pucgo.poobd._13062025.view.EmpresaView;
import pucgo.poobd._13062025.view.FormaPagamentoView;
import pucgo.poobd._13062025.view.PedidoView;
import pucgo.poobd._13062025.view.VendedorView;
import javafx.beans.property.SimpleStringProperty;

public class MainController {
    @FXML private TableView<Pedido> pedidoTable;
    @FXML private TableColumn<Pedido, Long> idColumnPedido;
    @FXML private TableColumn<Pedido, String> empresaColumnPedido;
    @FXML private TableColumn<Pedido, String> clienteColumnPedido;
    @FXML private TableColumn<Pedido, String> vendedorColumnPedido;
    @FXML private TableColumn<Pedido, String> descricaoColumnPedido;
    @FXML private TableColumn<Pedido, Boolean> aprovadoColumnPedido;
    @FXML private TableColumn<Pedido, Double> valorTotalColumnPedido;

    @FXML private TableView<Empresa> empresaTable;
    @FXML private TableColumn<Empresa, Long> idColumnEmpresa;
    @FXML private TableColumn<Empresa, String> nomeColumnEmpresa;
    @FXML private TableColumn<Empresa, String> cnpjColumnEmpresa;
    @FXML private TableColumn<Empresa, String> nomeFantasiaColumnEmpresa;
    @FXML private TableColumn<Empresa, String> razaoSocialColumnEmpresa;

    @FXML private TableView<Cliente> clienteTable;
    @FXML private TableColumn<Cliente, Long> idColumnCliente;
    @FXML private TableColumn<Cliente, String> nomeColumnCliente;
    @FXML private TableColumn<Cliente, String> cpfColumnCliente;
    @FXML private TableColumn<Cliente, String> telefoneColumnCliente;

    @FXML private TableView<Vendedor> vendedorTable;
    @FXML private TableColumn<Vendedor, Long> idColumnVendedor;
    @FXML private TableColumn<Vendedor, String> nomeColumnVendedor;
    @FXML private TableColumn<Vendedor, String> cpfColumnVendedor;
    @FXML private TableColumn<Vendedor, String> telefoneColumnVendedor;
    @FXML private TableColumn<Vendedor, String> empresaColumnVendedor;
    @FXML private TableColumn<Vendedor, String> supervisorColumnVendedor;

    @FXML private TableView<FormaPagamento> formaPagamentoTable;
    @FXML private TableColumn<FormaPagamento, Long> idColumnFormaPagamento;
    @FXML private TableColumn<FormaPagamento, String> descricaoColumnFormaPagamento;

    private Connection conn;
    private PedidoDAO pedidoDAO;
    private EmpresaDAO empresaDAO;
    private ClienteDAO clienteDAO;
    private VendedorDAO vendedorDAO;
    private FormaPagamentoDAO formaPagamentoDAO;

    public void setConnection(Connection conn) {
        this.conn = conn;
        this.pedidoDAO = new PedidoDAO(conn);
        this.empresaDAO = new EmpresaDAO(conn);
        this.clienteDAO = new ClienteDAO(conn);
        this.vendedorDAO = new VendedorDAO(conn);
        this.formaPagamentoDAO = new FormaPagamentoDAO(conn);
        
        // Initialize tables after DAOs are set up
        atualizarTabelaPedido();
        atualizarTabelaEmpresa();
        atualizarTabelaCliente();
        atualizarTabelaVendedor();
        atualizarTabelaFormaPagamento();
    }

    @FXML
    public void initialize() {
        // Configurar colunas do Pedido
        idColumnPedido.setCellValueFactory(new PropertyValueFactory<>("id"));
        empresaColumnPedido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmpresa().getNome()));
        clienteColumnPedido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getNome()));
        vendedorColumnPedido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVendedor().getNome()));
        descricaoColumnPedido.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        aprovadoColumnPedido.setCellValueFactory(new PropertyValueFactory<>("aprovado"));
        valorTotalColumnPedido.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        // Configurar colunas da Empresa
        idColumnEmpresa.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumnEmpresa.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cnpjColumnEmpresa.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        nomeFantasiaColumnEmpresa.setCellValueFactory(new PropertyValueFactory<>("nomeFantasia"));
        razaoSocialColumnEmpresa.setCellValueFactory(new PropertyValueFactory<>("razaoSocial"));

        // Configurar colunas do Cliente
        idColumnCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumnCliente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cpfColumnCliente.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        telefoneColumnCliente.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        // Configurar colunas do Vendedor
        idColumnVendedor.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumnVendedor.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cpfColumnVendedor.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        telefoneColumnVendedor.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        empresaColumnVendedor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmpresa().getNome()));
        supervisorColumnVendedor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupervisor().getNome()));

        // Configurar colunas da Forma de Pagamento
        idColumnFormaPagamento.setCellValueFactory(new PropertyValueFactory<>("id"));
        descricaoColumnFormaPagamento.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    }

    @FXML
    private void handleNovoPedido() {
        Optional<Pedido> novoPedido = PedidoView.showDialog(conn);
        novoPedido.ifPresent(pedido -> {
            try {
                pedidoDAO.criar(pedido);
                atualizarTabelaPedido();
            } catch (SQLException e) {
                showError("Erro ao criar pedido", e.getMessage());
            }
        });
    }

    @FXML
    private void handleEditarPedido() {
        Pedido pedidoSelecionado = pedidoTable.getSelectionModel().getSelectedItem();
        if (pedidoSelecionado != null) {
            Optional<Pedido> pedidoEditado = PedidoView.showDialog(conn, pedidoSelecionado);
            pedidoEditado.ifPresent(pedido -> {
                try {
                    pedidoDAO.atualizarPorId(pedidoSelecionado.getId(), pedido);
                    atualizarTabelaPedido();
                } catch (SQLException e) {
                    showError("Erro ao editar pedido", e.getMessage());
                }
            });
        }
    }

    @FXML
    private void handleExcluirPedido() {
        Pedido pedidoSelecionado = pedidoTable.getSelectionModel().getSelectedItem();
        if (pedidoSelecionado != null) {
            try {
                pedidoDAO.deletarPorId(pedidoSelecionado.getId());
                atualizarTabelaPedido();
            } catch (SQLException e) {
                showError("Erro ao excluir pedido", e.getMessage());
            }
        }
    }

    @FXML
    private void handleNovaEmpresa() {
        Optional<Empresa> novaEmpresa = EmpresaView.showDialog(conn, null);
        novaEmpresa.ifPresent(empresa -> {
            try {
                empresaDAO.criar(empresa);
                atualizarTabelaEmpresa();
            } catch (SQLException e) {
                showError("Erro ao criar empresa", e.getMessage());
            }
        });
    }

    @FXML
    private void handleEditarEmpresa() {
        Empresa empresaSelecionada = empresaTable.getSelectionModel().getSelectedItem();
        if (empresaSelecionada != null) {
            Optional<Empresa> empresaEditada = EmpresaView.showDialog(conn, empresaSelecionada);
            empresaEditada.ifPresent(empresa -> {
                try {
                    empresaDAO.atualizarPorId(empresaSelecionada.getId(), empresa);
                    atualizarTabelaEmpresa();
                } catch (SQLException e) {
                    showError("Erro ao editar empresa", e.getMessage());
                }
            });
        }
    }

    @FXML
    private void handleExcluirEmpresa() {
        Empresa empresaSelecionada = empresaTable.getSelectionModel().getSelectedItem();
        if (empresaSelecionada != null) {
            try {
                empresaDAO.deletarPorId(empresaSelecionada.getId());
                atualizarTabelaEmpresa();
            } catch (SQLException e) {
                showError("Erro ao excluir empresa", e.getMessage());
            }
        }
    }

    @FXML
    private void handleNovoCliente() {
        Optional<Cliente> novoCliente = ClienteView.showDialog(conn, null);
        novoCliente.ifPresent(cliente -> {
            clienteDAO.criar(cliente);
            atualizarTabelaCliente();
        });
    }

    @FXML
    private void handleEditarCliente() {
        Cliente clienteSelecionado = clienteTable.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            Optional<Cliente> clienteEditado = ClienteView.showDialog(conn, clienteSelecionado);
            clienteEditado.ifPresent(cliente -> {
                clienteDAO.atualizarPorId(clienteSelecionado.getId(), cliente);
                atualizarTabelaCliente();
            });
        }
    }

    @FXML
    private void handleExcluirCliente() {
        Cliente clienteSelecionado = clienteTable.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            clienteDAO.deletarPorId(clienteSelecionado.getId());
            atualizarTabelaCliente();
        }
    }

    @FXML
    private void handleNovoVendedor() {
        Optional<Vendedor> novoVendedor = VendedorView.showDialog(conn, null);
        novoVendedor.ifPresent(vendedor -> {
            try {
                vendedorDAO.criar(vendedor);
                atualizarTabelaVendedor();
            } catch (SQLException e) {
                showError("Erro ao criar vendedor", e.getMessage());
            }
        });
    }

    @FXML
    private void handleEditarVendedor() {
        Vendedor vendedorSelecionado = vendedorTable.getSelectionModel().getSelectedItem();
        if (vendedorSelecionado != null) {
            Optional<Vendedor> vendedorEditado = VendedorView.showDialog(conn, vendedorSelecionado);
            vendedorEditado.ifPresent(vendedor -> {
                try {
                    vendedorDAO.atualizarPorId(vendedorSelecionado.getId(), vendedor);
                    atualizarTabelaVendedor();
                } catch (SQLException e) {
                    showError("Erro ao editar vendedor", e.getMessage());
                }
            });
        }
    }

    @FXML
    private void handleExcluirVendedor() {
        Vendedor vendedorSelecionado = vendedorTable.getSelectionModel().getSelectedItem();
        if (vendedorSelecionado != null) {
            try {
                vendedorDAO.deletarPorId(vendedorSelecionado.getId());
                atualizarTabelaVendedor();
            } catch (SQLException e) {
                showError("Erro ao excluir vendedor", e.getMessage());
            }
        }
    }

    @FXML
    private void handleNovaFormaPagamento() {
        Optional<FormaPagamento> novaFormaPagamento = FormaPagamentoView.showDialog(conn, null);
        novaFormaPagamento.ifPresent(formaPagamento -> {
            try {
                formaPagamentoDAO.criar(formaPagamento);
                atualizarTabelaFormaPagamento();
            } catch (SQLException e) {
                showError("Erro ao criar forma de pagamento", e.getMessage());
            }
        });
    }

    @FXML
    private void handleEditarFormaPagamento() {
        FormaPagamento formaPagamentoSelecionada = formaPagamentoTable.getSelectionModel().getSelectedItem();
        if (formaPagamentoSelecionada != null) {
            Optional<FormaPagamento> formaPagamentoEditada = FormaPagamentoView.showDialog(conn, formaPagamentoSelecionada);
            formaPagamentoEditada.ifPresent(formaPagamento -> {
                try {
                    formaPagamentoDAO.atualizarPorId(formaPagamentoSelecionada.getId(), formaPagamento);
                    atualizarTabelaFormaPagamento();
                } catch (SQLException e) {
                    showError("Erro ao editar forma de pagamento", e.getMessage());
                }
            });
        }
    }

    @FXML
    private void handleExcluirFormaPagamento() {
        FormaPagamento formaPagamentoSelecionada = formaPagamentoTable.getSelectionModel().getSelectedItem();
        if (formaPagamentoSelecionada != null) {
            try {
                formaPagamentoDAO.deletarPorId(formaPagamentoSelecionada.getId());
                atualizarTabelaFormaPagamento();
            } catch (SQLException e) {
                showError("Erro ao excluir forma de pagamento", e.getMessage());
            }
        }
    }

    private void atualizarTabelaPedido() {
        try {
            pedidoTable.getItems().setAll(pedidoDAO.obterTodos());
        } catch (SQLException e) {
            showError("Erro ao atualizar tabela de pedidos", e.getMessage());
        }
    }

    private void atualizarTabelaEmpresa() {
        try {
            empresaTable.getItems().setAll(empresaDAO.obterTodos());
        } catch (SQLException e) {
            showError("Erro ao atualizar tabela de empresas", e.getMessage());
        }
    }

    private void atualizarTabelaCliente() {
        clienteTable.getItems().setAll(clienteDAO.obterTodos());
    }

    private void atualizarTabelaVendedor() {
        try {
            vendedorTable.getItems().setAll(vendedorDAO.obterTodos());
        } catch (SQLException e) {
            showError("Erro ao atualizar tabela de vendedores", e.getMessage());
        }
    }

    private void atualizarTabelaFormaPagamento() {
        try {
            formaPagamentoTable.getItems().setAll(formaPagamentoDAO.obterTodos());
        } catch (SQLException e) {
            showError("Erro ao atualizar tabela de formas de pagamento", e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}