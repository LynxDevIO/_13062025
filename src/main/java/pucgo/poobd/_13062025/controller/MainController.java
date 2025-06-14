package pucgo.poobd._13062025.controller;

import java.util.List;
import java.util.Optional;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import pucgo.poobd._13062025.dao.ClienteDAO;
import pucgo.poobd._13062025.dao.EmpresaDAO;
import pucgo.poobd._13062025.dao.FormaPagamentoDAO;
import pucgo.poobd._13062025.dao.PedidoDAO;
import pucgo.poobd._13062025.dao.ProdutoDAO;
import pucgo.poobd._13062025.model.Cliente;
import pucgo.poobd._13062025.model.Empresa;
import pucgo.poobd._13062025.model.Endereco;
import pucgo.poobd._13062025.model.FormaPagamento;
import pucgo.poobd._13062025.model.ItemPedido;
import pucgo.poobd._13062025.model.Pedido;
import pucgo.poobd._13062025.model.Produto;
import pucgo.poobd._13062025.model.Vendedor;
import pucgo.poobd._13062025.view.ClienteView;
import pucgo.poobd._13062025.view.EmpresaView;
import pucgo.poobd._13062025.view.FormaPagamentoView;

public class MainController {
    @FXML private ComboBox<Empresa> empresaComboBox;
    @FXML private VBox mainContent;
    @FXML private VBox supervisorContent;
    @FXML private ComboBox<Cliente> clienteComboBox;
    @FXML private ComboBox<Produto> produtoComboBox;
    @FXML private TextField quantidadeField;
    @FXML private TableView<ItemPedido> produtosTable;
    @FXML private TableColumn<ItemPedido, String> produtoColumn;
    @FXML private TableColumn<ItemPedido, Long> quantidadeColumn;
    @FXML private TableColumn<ItemPedido, Double> valorUnitarioColumn;
    @FXML private TableColumn<ItemPedido, Double> valorTotalColumn;
    @FXML private ComboBox<FormaPagamento> formaPagamentoComboBox;
    @FXML private Label totalLabel;
    @FXML private ComboBox<String> filtroComboBox;
    @FXML private TableView<Pedido> pedidosTable;
    @FXML private TableColumn<Pedido, Long> idColumn;
    @FXML private TableColumn<Pedido, String> clienteColumn;
    @FXML private TableColumn<Pedido, Double> valorTotalColumnPedido;
    @FXML private TableColumn<Pedido, String> statusColumn;
    @FXML private TableColumn<Pedido, Void> acoesColumn;
    @FXML private TextField limiteAprovacaoField;

    private final ObservableList<ItemPedido> itensPedido = FXCollections.observableArrayList();
    private Empresa empresaSelecionada;
    private Vendedor vendedorAtual;
    private double limiteAprovacao = 1000.0;

    @FXML
    public void initialize() {
        try {
            EmpresaDAO empresaDAO = new EmpresaDAO();
            ClienteDAO clienteDAO = new ClienteDAO();
            ProdutoDAO produtoDAO = new ProdutoDAO();
            FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
            PedidoDAO pedidoDAO = new PedidoDAO();

            // INÍCIO -- código gerado com IA
            empresaComboBox.setItems(FXCollections.observableArrayList(empresaDAO.obterTodos()));
            clienteComboBox.setItems(FXCollections.observableArrayList(clienteDAO.obterTodos()));
            produtoComboBox.setItems(FXCollections.observableArrayList(produtoDAO.obterTodos()));
            formaPagamentoComboBox.setItems(FXCollections.observableArrayList(formaPagamentoDAO.obterTodos()));

            empresaComboBox.setOnAction(e -> handleEmpresaSelecionada());
            
            produtosTable.setItems(itensPedido);
            produtoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduto().getNome()));
            quantidadeColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getQuantidade()).asObject());
            valorUnitarioColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getProduto().getValorUnitario()).asObject());
            valorTotalColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValorTotal()).asObject());

            filtroComboBox.setItems(FXCollections.observableArrayList("Todos", "Aguardando Aprovação"));
            filtroComboBox.getSelectionModel().selectFirst();

            idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
            clienteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getNome()));
            valorTotalColumnPedido.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValorTotal()).asObject());
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isAprovado() ? "Aprovado" : "Pendente"));
            // FIM -- código gerado com IA
            
            acoesColumn.setCellFactory(col -> new TableCell<>() {
                private final Button aprovarButton = new Button("Aprovar");
                {
                    aprovarButton.setOnAction(e -> {
                        Pedido pedido = getTableView().getItems().get(getIndex());
                        handleAprovarPedido(pedido);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : aprovarButton);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erro ao inicializar", e.getMessage());
        }
    }

    @FXML
    private void handleEmpresaSelecionada() {
        empresaSelecionada = empresaComboBox.getValue();
        if (empresaSelecionada != null) {
            mainContent.setVisible(true);
            vendedorAtual = new Vendedor(
                1L,
                "Vendedor Teste",
                "123.456.789-00",
                "123456789",
                new Endereco(),
                empresaSelecionada,
                5L,
                null
            );
            if (vendedorAtual.getSupervisor() != null) {
                supervisorContent.setVisible(true);
                atualizarTabelaPedidos();
            }
        }
    }

    @FXML
    private void handleNovaEmpresa() {
        try {
            Optional<Empresa> novaEmpresa = EmpresaView.showDialog();
            if (novaEmpresa.isPresent()) {
                EmpresaDAO empresaDAO = new EmpresaDAO();
                empresaDAO.criar(novaEmpresa.get());
                empresaComboBox.getItems().add(novaEmpresa.get());
                empresaComboBox.setValue(novaEmpresa.get());
            }
        } catch (Exception e) {
            showError("Erro", "Erro ao criar empresa");
        }
    }

    @FXML
    private void handleNovoCliente() {
        try {
            Optional<Cliente> novoCliente = ClienteView.showDialog();
            if (novoCliente.isPresent()) {
                ClienteDAO clienteDAO = new ClienteDAO();
                clienteDAO.criar(novoCliente.get());
                clienteComboBox.getItems().add(novoCliente.get());
                clienteComboBox.setValue(novoCliente.get());
            }
        } catch (Exception e) {
            showError("Erro", "Erro ao criar cliente");
        }
    }

    @FXML
    private void handleNovaFormaPagamento() {
        try {
            Optional<FormaPagamento> novaFormaPagamento = FormaPagamentoView.showDialog();
            if (novaFormaPagamento.isPresent()) {
                FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
                formaPagamentoDAO.criar(novaFormaPagamento.get());
                formaPagamentoComboBox.getItems().add(novaFormaPagamento.get());
                formaPagamentoComboBox.setValue(novaFormaPagamento.get());
            }
        } catch (Exception e) {
            showError("Erro", "Erro ao criar forma de pagamento");
        }
    }

    @FXML
    private void handleAdicionarProduto() {
        try {
            Produto produto = produtoComboBox.getValue();
            long quantidade = Long.parseLong(quantidadeField.getText());
            
            if (produto != null && quantidade > 0) {
                ItemPedido item = new ItemPedido(0, produto, null, quantidade, produto.getValorUnitario() * quantidade);
                itensPedido.add(item);
                atualizarTotal();
                quantidadeField.clear();
            }
        } catch (NumberFormatException e) {
            showError("Erro", "Quantidade inválida");
        }
    }

    @FXML
    private void handleRegistrarPedido() {
        try {
            Cliente cliente = clienteComboBox.getValue();
            FormaPagamento formaPagamento = formaPagamentoComboBox.getValue();
            
            if (cliente == null || formaPagamento == null || itensPedido.isEmpty()) {
                showError("Erro", "Preencha todos os campos");
                return;
            }

            Pedido pedido = new Pedido();
            pedido.setEmpresa(empresaSelecionada);
            pedido.setVendedor(vendedorAtual);
            pedido.setCliente(cliente);
            pedido.setFormaPagamento(formaPagamento);
            pedido.setItens(itensPedido);
            pedido.setValorTotal(calcularTotal());
            pedido.setAprovado(calcularTotal() <= limiteAprovacao);

            PedidoDAO pedidoDAO = new PedidoDAO();
            pedidoDAO.criar(pedido);

            limparFormulario();
            showInfo("Sucesso", "Pedido registrado com sucesso");
            
            if (vendedorAtual.getSupervisor() != null) {
                atualizarTabelaPedidos();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erro", "Erro ao registrar pedido");
        }
    }

    @FXML
    private void handleAtualizarPedidos() {
        atualizarTabelaPedidos();
    }

    @FXML
    private void handleDefinirLimite() {
        try {
            double novoLimite = Double.parseDouble(limiteAprovacaoField.getText());
            if (novoLimite > 0) {
                limiteAprovacao = novoLimite;
                showInfo("Sucesso", "Limite atualizado com sucesso");
            } else {
                showError("Erro", "Limite deve ser maior que zero");
            }
        } catch (NumberFormatException e) {
            showError("Erro", "Valor inválido");
        }
    }

    private void handleAprovarPedido(Pedido pedido) {
        try {
            pedido.setAprovado(true);
            PedidoDAO pedidoDAO = new PedidoDAO();
            pedidoDAO.atualizarPorId(pedido.getId(), pedido);
            atualizarTabelaPedidos();
            showInfo("Sucesso", "Pedido aprovado com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erro", "Erro ao aprovar pedido");
        }
    }

    private void atualizarTabelaPedidos() {
        try {
            PedidoDAO pedidoDAO = new PedidoDAO();
            List<Pedido> pedidos = pedidoDAO.obterTodos();
            
            if (filtroComboBox.getValue().equals("Aguardando Aprovação")) {
                pedidos = pedidos.stream()
                    .filter(p -> !p.isAprovado())
                    .toList();
            }
            
            pedidosTable.setItems(FXCollections.observableArrayList(pedidos));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erro", "Erro ao atualizar tabela de pedidos");
        }
    }

    private void atualizarTotal() {
        totalLabel.setText(String.format("Total: R$ %.2f", calcularTotal()));
    }

    private double calcularTotal() {
        return itensPedido.stream()
            .mapToDouble(ItemPedido::getValorTotal)
            .sum();
    }

    private void limparFormulario() {
        clienteComboBox.setValue(null);
        formaPagamentoComboBox.setValue(null);
        itensPedido.clear();
        atualizarTotal();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}