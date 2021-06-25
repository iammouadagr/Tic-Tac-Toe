/**
 *
 *
 *  @author Anass Benzekri
 */

/**
 *
 * @class Controleur de l'interface de difficulté
 *
 *
 *
 */

package controller;

import GameAnimation.Animator;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DifficultyController {

    @FXML
    public Pane idAnchorPane;
    @FXML
    private Button easyBtn;
    @FXML
    private Button hardBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Button idBackButton;

    public static String fileModel;
    public static MultiLayerPerceptron net;
    public static String SelectedMode;

    /**
     *
     * @method initialize initialise l'interface
     *
     *
     *
     *
     *
     */
    @FXML
    public void initialize(){

        Image titleImage = new Image("file:resources/images/logolabel.jpg");

        ImageView titletView = new ImageView(titleImage);

        titletView.setFitHeight(95.0);
        titletView.setFitWidth(431.0);
        titletView.setX(173.0);
        titletView.setY(72.0);
        titletView.setPreserveRatio(true);

        Animator.animateTitle(titletView);

        idAnchorPane.getChildren().add(titletView);



        Image backImage = new Image("file:resources/images/left-arrow.png");

        ImageView backView = new ImageView(backImage);
        backView.setFitHeight(23.0);
        backView.setFitWidth(18.0);
        backView.setX(18.0);
        backView.setY(22.0);
        backView.setPreserveRatio(true);

        idBackButton.setGraphic(backView);



    }


    /**
     *
     * @method getNet retourne net
     *
     * @return MultiLayerPerceptron
     *
     */
    public MultiLayerPerceptron getNet() {
        return net;
    }



    /**
     *
     * @method setNet modifie le net
     *
     */

    public void setNet(MultiLayerPerceptron net) {
        this.net = net;
    }


    /**
     *
     * @method StartPlaying recupere le nom du fichier du model ou chargement du model si déja existant
     *
     * @param mode String
     *
     *
     */
    public void StartPlaying(String mode) throws IOException {

        FileReader fin = new FileReader("./resources/config");

        BufferedReader bin = new BufferedReader(fin);

        String facile = bin.readLine();
        String diffcile = bin.readLine();

        String [] facileSplit = facile.split(";");
        String [] difficileSplit = diffcile.split(";");

        int hf = Integer.parseInt(facileSplit[0]);
        double lrf =  new Double(facileSplit[1]);
        int lf = Integer.parseInt(facileSplit[2]);

        int hd = Integer.parseInt(difficileSplit[0]);
        double lrd = new Double(difficileSplit[1]);
        int ld =Integer.parseInt(difficileSplit[2]);

        if (mode.equals("Easy")){
            fileModel = "mlp_"+hf+"_"+lrf+"_"+lf+".srl";
        }
        else {
            fileModel = "mlp_"+hd+"_"+lrd+"_"+ld+".srl";
        }

        if(new File("./resources/models/"+fileModel).exists()){
            System.out.println("******* Model Loaded ********");
            MultiLayerPerceptron net = MultiLayerPerceptron.load("./resources/models/"+fileModel);
        }

        else{

            int[] layers = new int[lf+2];
            layers[0] = 9;
            for (int i = 0; i < lf; i++){
                layers[i+1] = hf;
            }
            layers[layers.length-1] = 9;

            setNet(new MultiLayerPerceptron(layers, lf, new SigmoidalTransferFunction()));

        }
        bin.close();


    }

    /**
     *
     * @method EasyBtnActionPerformed affectation du niveau du difficulté et lancement de l'apprentissage
     *
     * @param actionEvent ActionEvent
     *
     *
     */
    @FXML
    public void EasyBtnActionPerformed(ActionEvent actionEvent) throws IOException {
        SelectedMode = "Easy";
        launchAiTraining(SelectedMode);
    }

    /**
     *
     * @method hardBtnActionPerformed affectation du niveau du difficulté et fait appel à la methode launchAiTraining
     *
     * @param actionEvent ActionEvent
     *
     *
     */
    @FXML
    public void hardBtnActionPerformed(ActionEvent actionEvent) throws IOException {
        SelectedMode = "Hard";
        launchAiTraining(SelectedMode);
    }


    /**
     *
     * @method nextBtnActionPerformed naviguer vers l'interface de saisie des pseudos
     *
     * @param actionEvent ActionEvent
     *
     *
     */
    @FXML
    public void nextBtnActionPerformed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../application/PlayerInfo.fxml"));
        Scene gameScene = new Scene(root);
        Stage window = (Stage) nextBtn.getScene().getWindow();
        window.setScene(gameScene);
        window.show();
        window.setTitle("Tic-Tac-Toe");
    }

    /**
     *
     * @method launchAiTraining lance l'apprentissage en fonction du niveau de difficulté
     *
     * @param mode String
     *
     *
     */

    public void launchAiTraining(String mode) throws IOException {
        if(SelectedMode != null){
            StartPlaying(mode);
            nextBtn.setDisable(false);
            if(!new File("./resources/models/"+fileModel).exists()){
                Parent root = FXMLLoader.load(getClass().getResource("../application/sample.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Tic Tac Toe");
                stage.setScene(new Scene(root));
                stage.show();
            }
        }
    }

    /**
     *
     * @method launchAiTraining lance l'apprentissage en fonction du niveau de difficulté
     *
     * @param  actionEvent ActionEvent
     *
     *
     */
    public void BackButtonAction(ActionEvent actionEvent) throws IOException {
        Parent gameRoot = FXMLLoader.load(getClass().getResource("../application/gameMode.fxml"));
        Scene gameScene = new Scene(gameRoot);

        Stage window = (Stage) idBackButton.getScene().getWindow();

        window.setScene(gameScene);
        window.show();
        window.setTitle("Tic-Tac-Toe");
    }
}