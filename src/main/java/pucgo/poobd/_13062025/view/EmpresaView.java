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
import pucgo.poobd._13062025.model.Empresa;

public class EmpresaView {
    public static Optional<Empresa> showDialog(Connection conn) {
        return showDialog(conn, null);
    }

    public static Optional<Empresa> showDialog(Connection conn, Empresa empresa) {
        Dialog<Empresa> dialog = new Dialog<>();
        dialog.setTitle(empresa == null ? "Nova Empresa" : "Editar Empresa");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomeField = new TextField();
        TextField cnpjField = new TextField();
        TextField nomeFantasiaField = new TextField();
        TextField razaoSocialField = new TextField();

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeField, 1, 0);
        grid.add(new Label("CNPJ:"), 0, 1);
        grid.add(cnpjField, 1, 1);
        grid.add(new Label("Nome Fantasia:"), 0, 2);
        grid.add(nomeFantasiaField, 1, 2);
        grid.add(new Label("Razão Social:"), 0, 3);
        grid.add(razaoSocialField, 1, 3);

        if (empresa != null) {
            nomeField.setText(empresa.getNome());
            cnpjField.setText(empresa.getCnpj());
            nomeFantasiaField.setText(empresa.getNomeFantasia());
            razaoSocialField.setText(empresa.getRazaoSocial());
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Empresa result = empresa != null ? empresa : new Empresa();
                result.setNome(nomeField.getText());
                result.setCnpj(cnpjField.getText());
                result.setNomeFantasia(nomeFantasiaField.getText());
                result.setRazaoSocial(razaoSocialField.getText());
                return result;
            }
            return null;
        });

        return dialog.showAndWait();
    }
} 