package services;

import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class fileChooser {

    public static <T> void  chooseSaveLocationAndExport(TableView<T> tableView) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            PDFexport_API.exportToPDF(tableView, file.getAbsolutePath());
        }
    }
}
