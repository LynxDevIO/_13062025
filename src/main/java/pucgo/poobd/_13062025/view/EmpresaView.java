package pucgo.poobd._13062025.view;

import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import pucgo.poobd._13062025.controller.EmpresaDialogController;
import pucgo.poobd._13062025.model.Empresa;

public class EmpresaView {
    public static Optional<Empresa> showDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(EmpresaView.class.getResource("/pucgo/poobd/_13062025/view/empresa-dialog.fxml"));
            DialogPane pane = loader.load();
            EmpresaDialogController controller = loader.getController();
            
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Nova Empresa");
            
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                return Optional.ofNullable(controller.getEmpresa());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
} 