package pucgo.poobd._13062025.view;

import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import pucgo.poobd._13062025.controller.ClienteDialogController;
import pucgo.poobd._13062025.model.Cliente;

public class ClienteView {
    public static Optional<Cliente> showDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(ClienteView.class.getResource("/pucgo/poobd/_13062025/view/cliente-dialog.fxml"));
            DialogPane pane = loader.load();
            ClienteDialogController controller = loader.getController();
            
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Novo Cliente");
            
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                return Optional.ofNullable(controller.getCliente());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
} 