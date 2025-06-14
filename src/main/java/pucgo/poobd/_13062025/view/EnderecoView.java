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
import pucgo.poobd._13062025.model.Endereco;

public class EnderecoView {
    public static Optional<Endereco> showDialog(Connection conn) {
        return showDialog(conn, null);
    }

    public static Optional<Endereco> showDialog(Connection conn, Endereco endereco) {
        Dialog<Endereco> dialog = new Dialog<>();
        dialog.setTitle(endereco == null ? "Novo Endereço" : "Editar Endereço");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField ruaField = new TextField();
        TextField complementoField = new TextField();
        TextField bairroField = new TextField();
        TextField cidadeField = new TextField();
        TextField estadoField = new TextField();
        TextField cepField = new TextField();

        grid.add(new Label("Rua:"), 0, 0);
        grid.add(ruaField, 1, 0);
        grid.add(new Label("Complemento:"), 0, 1);
        grid.add(complementoField, 1, 1);
        grid.add(new Label("Bairro:"), 0, 2);
        grid.add(bairroField, 1, 2);
        grid.add(new Label("Cidade:"), 0, 3);
        grid.add(cidadeField, 1, 3);
        grid.add(new Label("Estado:"), 0, 4);
        grid.add(estadoField, 1, 4);
        grid.add(new Label("CEP:"), 0, 5);
        grid.add(cepField, 1, 5);

        if (endereco != null) {
            ruaField.setText(endereco.getRua());
            complementoField.setText(endereco.getComplemento());
            bairroField.setText(endereco.getBairro());
            cidadeField.setText(endereco.getCidade());
            estadoField.setText(endereco.getEstado());
            cepField.setText(endereco.getCep());
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Endereco result = endereco != null ? endereco : new Endereco();
                result.setRua(ruaField.getText());
                result.setComplemento(complementoField.getText());
                result.setBairro(bairroField.getText());
                result.setCidade(cidadeField.getText());
                result.setEstado(estadoField.getText());
                result.setCep(cepField.getText());
                return result;
            }
            return null;
        });

        return dialog.showAndWait();
    }
} 