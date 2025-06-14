package pucgo.poobd._13062025.view;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import pucgo.poobd._13062025.dao.ClienteDAO;
import pucgo.poobd._13062025.dao.EmpresaDAO;
import pucgo.poobd._13062025.dao.FormaPagamentoDAO;
import pucgo.poobd._13062025.dao.VendedorDAO;
import pucgo.poobd._13062025.model.Cliente;
import pucgo.poobd._13062025.model.Empresa;
import pucgo.poobd._13062025.model.FormaPagamento;
import pucgo.poobd._13062025.model.Pedido;
import pucgo.poobd._13062025.model.Vendedor;

public class PedidoView {
    public static Optional<Pedido> showDialog(Connection conn) {
        return showDialog(conn, null);
    }

    public static Optional<Pedido> showDialog(Connection conn, Pedido pedido) {
        Dialog<Pedido> dialog = new Dialog<>();
        dialog.setTitle(pedido == null ? "Novo Pedido" : "Editar Pedido");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        EmpresaDAO empresaDAO = new EmpresaDAO(conn);
        ClienteDAO clienteDAO = new ClienteDAO(conn);
        VendedorDAO vendedorDAO = new VendedorDAO(conn);
        FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO(conn);

        ComboBox<Empresa> empresaComboBox = new ComboBox<>();
        ComboBox<Cliente> clienteComboBox = new ComboBox<>();
        ComboBox<Vendedor> vendedorComboBox = new ComboBox<>();
        ComboBox<FormaPagamento> formaPagamentoComboBox = new ComboBox<>();
        TextField descricaoField = new TextField();

        try {
            empresaComboBox.getItems().addAll(empresaDAO.obterTodos());
            clienteComboBox.getItems().addAll(clienteDAO.obterTodos());
            vendedorComboBox.getItems().addAll(vendedorDAO.obterTodos());
            formaPagamentoComboBox.getItems().addAll(formaPagamentoDAO.obterTodos());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar dados");
            alert.setContentText("Ocorreu um erro ao carregar os dados: " + e.getMessage());
            alert.showAndWait();
            return Optional.empty();
        }

        grid.add(new Label("Empresa:"), 0, 0);
        grid.add(empresaComboBox, 1, 0);
        grid.add(new Label("Cliente:"), 0, 1);
        grid.add(clienteComboBox, 1, 1);
        grid.add(new Label("Vendedor:"), 0, 2);
        grid.add(vendedorComboBox, 1, 2);
        grid.add(new Label("Forma de Pagamento:"), 0, 3);
        grid.add(formaPagamentoComboBox, 1, 3);
        grid.add(new Label("Descrição:"), 0, 4);
        grid.add(descricaoField, 1, 4);

        if (pedido != null) {
            empresaComboBox.setValue(pedido.getEmpresa());
            clienteComboBox.setValue(pedido.getCliente());
            vendedorComboBox.setValue(pedido.getVendedor());
            formaPagamentoComboBox.setValue(pedido.getFormaPagamento());
            descricaoField.setText(pedido.getDescricao());
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Pedido result = pedido != null ? pedido : new Pedido();
                result.setEmpresa(empresaComboBox.getValue());
                result.setCliente(clienteComboBox.getValue());
                result.setVendedor(vendedorComboBox.getValue());
                result.setFormaPagamento(formaPagamentoComboBox.getValue());
                result.setDescricao(descricaoField.getText());
                return result;
            }
            return null;
        });

        return dialog.showAndWait();
    }
} 