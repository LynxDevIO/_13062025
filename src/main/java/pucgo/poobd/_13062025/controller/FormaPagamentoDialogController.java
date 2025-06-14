package pucgo.poobd._13062025.controller;

import java.sql.Connection;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pucgo.poobd._13062025.dao.FormaPagamentoDAO;
import pucgo.poobd._13062025.model.FormaPagamento;

public class FormaPagamentoDialogController {
    @FXML private DialogPane dialogPane;
    @FXML private TextField descricaoField;
    
    private Stage dialogStage;
    private FormaPagamento formaPagamento;
    private boolean salvarClicked = false;
    private Connection conn;
    private FormaPagamentoDAO formaPagamentoDAO;

    public void setConnection(Connection conn) {
        this.conn = conn;
        this.formaPagamentoDAO = new FormaPagamentoDAO(conn);
    }

    @FXML
    public void initialize() {
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);
        
        descricaoField.textProperty().addListener((obs, oldVal, newVal) -> {
            dialogPane.lookupButton(ButtonType.OK).setDisable(newVal.trim().isEmpty());
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
        
        if (formaPagamento != null) {
            descricaoField.setText(formaPagamento.getDescricao());
        }
    }

    public boolean isSalvarClicked() {
        return salvarClicked;
    }

    @FXML
    private void handleSalvar() {
        if (isInputValid()) {
            formaPagamento = new FormaPagamento();
            formaPagamento.setDescricao(descricaoField.getText());

            try {
                formaPagamentoDAO.criar(formaPagamento);
                salvarClicked = true;
                dialogStage.close();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Mostrar erro
            }
        }
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (descricaoField.getText() == null || descricaoField.getText().length() == 0) {
            errorMessage += "Descrição inválida!\n";
        }
        return errorMessage.length() == 0;
    }

    public FormaPagamento getFormaPagamento() {
        if (formaPagamento == null) {
            formaPagamento = new FormaPagamento();
        }
        formaPagamento.setDescricao(descricaoField.getText());
        return formaPagamento;
    }
} 