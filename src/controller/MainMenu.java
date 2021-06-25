/**
 * @author Anass Benzekri
 * @author Mouad Aguirar
 *
 */

/**
 *
 * @class Controleur de l'interface d'accueil
 *
 *
 *
 */
package controller;


import GameAnimation.Animator;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utilitaire.Data;
import utilitaire.DataManager;

import java.io.*;

public class MainMenu {

    @FXML
    public Button idStart;
    @FXML
    public Button loadButton;
    @FXML
    public Button idSettings;
    @FXML
    public ImageView Title;
    @FXML
    public ImageView startIcon;
    @FXML
    public ImageView reloadIcon;
    @FXML
    public ImageView settingsIcon;

    @FXML
    public AnchorPane idAnchorPane;

    @FXML
    public Pane idTitlePane;

    /**
     *
     * @method initialize l'interface
     *
     *
     *
     *
     *
     */

    @FXML
   private void initialize(){


        Image titleImage = new Image("file:resources/images/logolabel.jpg");

        ImageView titletView = new ImageView(titleImage);

        titletView.setFitHeight(97.0);
        titletView.setFitWidth(431.0);
        titletView.setX(7.0);
        titletView.setY(34.0);
        titletView.setPreserveRatio(true);

        idTitlePane.getChildren().add(titletView);

        Image startImage = new Image("file:resources/images/play-button-arrowhead(1).png");

        ImageView startView = new ImageView(startImage);

        startView.setFitHeight(23.0);
        startView.setFitWidth(35.0);
        startView.setX(231.0);
        startView.setY(228.0);
        startView.setPreserveRatio(true);

        idStart.setGraphic(startView);

        Image reloadImage = new Image("file:resources/images/reload.png");

        ImageView reloadView = new ImageView(reloadImage);

        reloadView.setFitHeight(23.0);
        reloadView.setFitWidth(35.0);
        reloadView.setX(231.0);
        reloadView.setY(298.0);
        reloadView.setPreserveRatio(true);

        loadButton.setGraphic(reloadView);

        Image settingsImage = new Image("file:resources/images/settings.png");

        ImageView settingsView = new ImageView(settingsImage);

        settingsView.setFitHeight(23.0);
        settingsView.setFitWidth(35.0);
        settingsView.setX(231.0);
        settingsView.setY(266.0);
        settingsView.setPreserveRatio(true);

        idSettings.setGraphic(settingsView);

       Animator.animateTitle(titletView);

       Animator.animateFadingNode(startView, 1, 0.1,2000, Timeline.INDEFINITE);
       Animator.animateFadingNode(reloadView, 1, 0.1,2000, Timeline.INDEFINITE);
       Animator.animateFadingNode(settingsView, 1, 0.1,2000, Timeline.INDEFINITE);
    }

    /**
     *
     * @method StartGameActionPerformed naviguer vers l'interface de la sélection du mode
     *
     *
     * @param actionEvent Action event
     *
     *
     */

    public void StartGameActionPerformed(ActionEvent actionEvent) throws IOException {
        //Initialisation de la grille
        DataManager.gameBoard = null;

        //Initialisation des dimensions
        DataManager.rows = Settings.numRow;
        DataManager.columns = Settings.numColumn;

        // Récuperation de la scene
        Parent gameRoot = FXMLLoader.load(getClass().getResource("../application/gameMode.fxml"));
        Scene gameScene = new Scene(gameRoot);

        // Récuperation du stage
        Stage window = (Stage) idStart.getScene().getWindow();

       // Affectaion du scene au stage
        window.setScene(gameScene);
        window.show();
        window.setTitle("Tic-Tac-Toe");
    }

    /**
     *
     * @method loadGame charger la partie sélectionné
     *
     *
     * @param actionEvent Action event
     *
     *
     */
    public void loadGame(ActionEvent actionEvent) throws IOException, ClassNotFoundException {

        // Récuperation du Stage
        Stage window = (Stage) idTitlePane.getScene().getWindow();

        // Selectionneur du fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a save");
        fileChooser.setInitialDirectory(new File("./resources/dataset/saves/"));


        File saveFile = fileChooser.showOpenDialog(window);
        if (saveFile == null) {
            return;
        }



        // Chargement des données
        DataManager.load(saveFile);


        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent p = fxmlLoader.load(getClass().getResource("../application/game.fxml"));
        Scene gameScene = new Scene(p);
        // Affectaion du scene au stage
        window.setScene(gameScene);
        GameController game = fxmlLoader.getController();
        game.initialize();
        window.show();

        window.setTitle("Tic-Tac-Toe");
        window.setTitle("Yet Another Tic-Tac-Toe Game (" + DataManager.gameMode + ")");

    }

    /**
     *
     * @method SettingsActionPerformed naviguer vers l'interface des parametres
     *
     *
     * @param actionEvent Action event
     *
     *
     */


    public void SettingsActionPerformed(ActionEvent actionEvent) throws IOException {
        // Récuperation de la scene
        Parent gameRoot = FXMLLoader.load(getClass().getResource("../application/settings.fxml"));
        Scene gameScene = new Scene(gameRoot);

        // Récuperation du Stage
        Stage window = (Stage) idStart.getScene().getWindow();

        // Affectaion du scene au stage
        window.setScene(gameScene);
        window.show();
        window.setTitle("Tic-Tac-Toe");
    }
}
