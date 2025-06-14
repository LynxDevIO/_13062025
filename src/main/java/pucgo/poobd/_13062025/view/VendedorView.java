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
import pucgo.poobd._13062025.dao.EmpresaDAO;
import pucgo.poobd._13062025.dao.SupervisorDAO;
import pucgo.poobd._13062025.model.Empresa;
import pucgo.poobd._13062025.model.Supervisor;
import pucgo.poobd._13062025.model.Vendedor;

public class VendedorView {
    public static Optional<Vendedor> showDialog(Connection conn) {
        return showDialog(conn, null);
    }

    public static Optional<Vendedor> showDialog(Connection conn, Vendedor vendedor) {
        Dialog<Vendedor> dialog = new Dialog<>();
        dialog.setTitle(vendedor == null ? "Novo Vendedor" : "Editar Vendedor");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        EmpresaDAO empresaDAO = new EmpresaDAO(conn);
        SupervisorDAO supervisorDAO = new SupervisorDAO(conn);

        TextField nomeField = new TextField();
        TextField cpfField = new TextField();
        TextField telefoneField = new TextField();
        ComboBox<Empresa> empresaComboBox = new ComboBox<>();
        ComboBox<Supervisor> supervisorComboBox = new ComboBox<>();

        try {
            empresaComboBox.getItems().addAll(empresaDAO.obterTodos());
            supervisorComboBox.getItems().addAll(supervisorDAO.obterTodos());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar dados");
            alert.setContentText("Ocorreu um erro ao carregar os dados: " + e.getMessage());
            alert.showAndWait();
            return Optional.empty();
        }

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeField, 1, 0);
        grid.add(new Label("CPF:"), 0, 1);
        grid.add(cpfField, 1, 1);
        grid.add(new Label("Telefone:"), 0, 2);
        grid.add(telefoneField, 1, 2);
        grid.add(new Label("Empresa:"), 0, 3);
        grid.add(empresaComboBox, 1, 3);
        grid.add(new Label("Supervisor:"), 0, 4);
        grid.add(supervisorComboBox, 1, 4);

        if (vendedor != null) {
            nomeField.setText(vendedor.getNome());
            cpfField.setText(vendedor.getCpf());
            telefoneField.setText(vendedor.getTelefone());
            empresaComboBox.setValue(vendedor.getEmpresa());
            supervisorComboBox.setValue(vendedor.getSupervisor());
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Vendedor result = vendedor != null ? vendedor : new Vendedor();
                result.setNome(nomeField.getText());
                result.setCpf(cpfField.getText());
                result.setTelefone(telefoneField.getText());
                result.setEmpresa(empresaComboBox.getValue());
                result.setSupervisor(supervisorComboBox.getValue());
                return result;
            }
            return null;
        });

        return dialog.showAndWait();
    }
} 