/**
 * @author Anass Benzekri
 *
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utilitaire.DataManager;

import java.io.IOException;

public class GameModeController {

    @FXML
    public Button idSingleplayer;
    @FXML
    public Button idMultiplayer;
    @FXML
    public ImageView user;
    @FXML
    public ImageView users;
    @FXML
    public Pane idTitlePane;
    @FXML
    public ImageView Title;
    @FXML
    public Button idBackButton;
    @FXML
    public ImageView backIcon;

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




        Image backImage = new Image("file:resources/images/left-arrow.png");

        ImageView backView = new ImageView(backImage);

        backView.setFitHeight(23.0);
        backView.setFitWidth(18.0);
        backView.setX(18.0);
        backView.setY(22.0);
        backView.setPreserveRatio(true);

        idBackButton.setGraphic(backView);



        Image titleImage = new Image("file:resources/images/logolabel.jpg");

        ImageView titletView = new ImageView(titleImage);

        titletView.setFitHeight(97.0);
        titletView.setFitWidth(431.0);
        titletView.setX(94.0);
        titletView.setY(51.0);
        titletView.setPreserveRatio(true);

        idTitlePane.getChildren().add(titletView);



        Image userImage = new Image("file:resources/images/user(1).png");

        ImageView userView = new ImageView(userImage);

        userView.setFitHeight(23.0);
        userView.setFitWidth(35.0);
        userView.setX(231.0);
        userView.setY(228.0);
        userView.setPreserveRatio(true);

        idSingleplayer.setGraphic(userView);


        Image usersImage = new Image("file:resources/images/users.png");

        ImageView usersView = new ImageView(usersImage);

        usersView.setFitHeight(23.0);
        usersView.setFitWidth(35.0);
        usersView.setX(231.0);
        usersView.setY(366.0);
        usersView.setPreserveRatio(true);

        idMultiplayer.setGraphic(usersView);

        // Animation du logo
        Animator.animateTitle(titletView);

        //Animation des icones des boutons
        Animator.animateFadingNode(userView, 1, 0.1,2000, Timeline.INDEFINITE);
        Animator.animateFadingNode(usersView, 1, 0.1,2000, Timeline.INDEFINITE);

    }
    /**
     *
     * @method SinglePlayerAction modifier la valeur du gameMode du DataManger et naviguer vers l'interface de la difficulté
     *
     *
     * @param actionEvent ActionEvent
     *
     *
     */
    public void SinglePlayerAction(ActionEvent actionEvent) throws IOException {

        DataManager.gameMode = "SinglePlayer";

        Parent root = FXMLLoader.load(getClass().getResource("../application/difficulty.fxml"));
        Scene gameScene = new Scene(root);


        Stage window = (Stage) idBackButton.getScene().getWindow();

        window.setScene(gameScene);
        window.show();
        window.setTitle("Tic-Tac-Toe");
    }
    /**
     *
     * @method MultiplayerAction modifier la valeur du gameMode du DataManger et naviguer vers l'interface de saisie des pseudos
     *
     *
     * @param actionEvent ActionEvent
     *
     *
     */
    public void MultiplayerAction(ActionEvent actionEvent) throws IOException {

        DataManager.gameMode = "MultiPlayer";

        Parent gameRoot = FXMLLoader.load(getClass().getResource("../application/playerInfo.fxml"));
        Scene gameScene = new Scene(gameRoot);

        Stage window = (Stage) idBackButton.getScene().getWindow();

        window.setScene(gameScene);
        window.show();
        window.setTitle("Tic-Tac-Toe");
    }
    /**
     *
     * @method BackButtonAction naviguer vers l'interface precédente
     *
     *
     * @param actionEvent ActionEvent
     *
     *
     */
    public void BackButtonAction(ActionEvent actionEvent) throws IOException {

        Parent gameRoot = FXMLLoader.load(getClass().getResource("../application/mainWindow.fxml"));
        Scene gameScene = new Scene(gameRoot);

        Stage window = (Stage) idBackButton.getScene().getWindow();

        window.setScene(gameScene);
        window.show();
        window.setTitle("Tic-Tac-Toe");
    }
}
