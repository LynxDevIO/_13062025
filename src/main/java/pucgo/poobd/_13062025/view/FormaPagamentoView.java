package pucgo.poobd._13062025.view;

import java.sql.Connection;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import pucgo.poobd._13062025.model.FormaPagamento;

public class FormaPagamentoView {
    public static Optional<FormaPagamento> showDialog(Connection conn, FormaPagamento formaPagamento) {
        Dialog<FormaPagamento> dialog = new Dialog<>();
        dialog.setTitle(formaPagamento == null ? "Nova Forma de Pagamento" : "Editar Forma de Pagamento");
        dialog.setHeaderText(null);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField descricaoField = new TextField();
        grid.add(new Label("Descrição:"), 0, 0);
        grid.add(descricaoField, 1, 0);

        if (formaPagamento != null) {
            descricaoField.setText(formaPagamento.getDescricao());
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                FormaPagamento result = formaPagamento != null ? formaPagamento : new FormaPagamento();
                result.setDescricao(descricaoField.getText());
                return result;
            }
            return null;
        });

        return dialog.showAndWait();
    }
} 