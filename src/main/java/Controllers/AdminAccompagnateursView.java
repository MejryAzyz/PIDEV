package Controllers;

import DAO.AccompagnateurDAO;
import Modeles.Accompagnateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminAccompagnateursView {
    @FXML
    private TableView<Accompagnateur> tableAccompagnateurs;
    @FXML
    private TableColumn<Accompagnateur, String> colUsername;
    @FXML
    private TableColumn<Accompagnateur, String> colEmail;
    @FXML
    private TableColumn<Accompagnateur, String> colStatut;
    @FXML
    private TableColumn<Accompagnateur, Void> colAction; // Colonne pour les boutons

    private final AccompagnateurDAO accompagnateurDAO = new AccompagnateurDAO();
    private final ObservableList<Accompagnateur> accompagnateursList = FXCollections.observableArrayList();
    private static final Logger LOGGER = Logger.getLogger(AdminAccompagnateursView.class.getName());

    @FXML
    public void initialize() {
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        chargerAccompagnateurs();
        ajouterBoutonsAction();
    }

    private void chargerAccompagnateurs() {
        try {
            List<Accompagnateur> accompagnateurs = accompagnateurDAO.recupererParStatut("En attente");
            accompagnateursList.clear();
            accompagnateursList.addAll(accompagnateurs);
            tableAccompagnateurs.setItems(accompagnateursList);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Erreur SQL lors du chargement des accompagnateurs", e);
        }
    }

    private void ajouterBoutonsAction() {
        colAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Accompagnateur, Void> call(final TableColumn<Accompagnateur, Void> param) {
                return new TableCell<>() {
                    private final Button btnAccepter = new Button("✅ Accepter");
                    private final Button btnRefuser = new Button("❌ Refuser");
                    private final HBox buttonsBox = new HBox(5, btnAccepter, btnRefuser);

                    {
                        btnAccepter.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                        btnRefuser.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                        btnAccepter.setOnAction(event -> {
                            Accompagnateur ac = getTableView().getItems().get(getIndex());
                            changerStatut(ac);
                        });

                        btnRefuser.setOnAction(event -> {
                            Accompagnateur ac = getTableView().getItems().get(getIndex());
                            supprimerAccompagnateur(ac);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonsBox);
                        }
                    }
                };
            }
        });
    }

    private void changerStatut(Accompagnateur ac) {
        try {
            ac.setStatut("recruté");
            accompagnateurDAO.mettreAJourStatut(ac);
            chargerAccompagnateurs(); // Rafraîchir la table après mise à jour
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Erreur lors de la mise à jour du statut", e);
        }
    }

    private void supprimerAccompagnateur(Accompagnateur ac) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer cet accompagnateur ?");
        alert.setContentText("Cette action est irréversible. Voulez-vous continuer ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    accompagnateurDAO.supprimer(ac.getIdAccompagnateur());
                    chargerAccompagnateurs(); // Rafraîchir après suppression
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "❌ Erreur lors de la suppression", e);
                }
            }
        });
    }
}
