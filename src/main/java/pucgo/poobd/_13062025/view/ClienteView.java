package pucgo.poobd._13062025.view;

import java.sql.Connection;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import pucgo.poobd._13062025.model.Cliente;

public class ClienteView {
    public static Optional<Cliente> showDialog(Connection conn) {
        return showDialog(conn, null);
    }

    public static Optional<Cliente> showDialog(Connection conn, Cliente cliente) {
        Dialog<Cliente> dialog = new Dialog<>();
        dialog.setTitle(cliente == null ? "Novo Cliente" : "Editar Cliente");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomeField = new TextField();
        TextField cpfField = new TextField();
        TextField telefoneField = new TextField();

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeField, 1, 0);
        grid.add(new Label("CPF:"), 0, 1);
        grid.add(cpfField, 1, 1);
        grid.add(new Label("Telefone:"), 0, 2);
        grid.add(telefoneField, 1, 2);

        if (cliente != null) {
            nomeField.setText(cliente.getNome());
            cpfField.setText(cliente.getCpf());
            telefoneField.setText(cliente.getTelefone());
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Cliente result = cliente != null ? cliente : new Cliente();
                result.setNome(nomeField.getText());
                result.setCpf(cpfField.getText());
                result.setTelefone(telefoneField.getText());
                return result;
            }
            return null;
        });

        return dialog.showAndWait();
    }
} 