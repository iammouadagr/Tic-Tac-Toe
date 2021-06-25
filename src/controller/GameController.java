/**
 *
 * @author Mouad Aguirar
 *  @author Anass Benzekri
 */

/**
 *
 * @class Controleur de l'interface du jeu
 *
 *
 *
 */

package controller;

import GameAnimation.Animator;
import ai.ClassAI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import utilitaire.DataManager;
import utilitaire.GameBoard;
import utilitaire.Player;
import utilitaire.Tile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameController {


    @FXML
    private Label playerOneName;
    @FXML
    private Label playerOneScore;

    @FXML
    private Label playerTwoName;
    @FXML
    private Label playerTwoScore;

    @FXML
    private Pane gridToppingPane;

    @FXML
    private GridPane gridGame;

    @FXML
    private GridPane firstPlayerGridPane;

    @FXML
    private GridPane secondPlayerGridPane;

    @FXML
    private Label currentPlayerTurn;

    private boolean play;

    @FXML
    private Button newGameLauncher;

    @FXML
    private Button backToMenu;

    @FXML private ImageView playerOneImage;
    @FXML private ImageView playerTwoImage;
    @FXML private Pane playerOneImagePane;
    @FXML private Pane playerTwoImagePane;

    @FXML private Label saveLabel;



    /**
     *
     * @method backToMenuActionPerformed naviguer à l'interface de'accueil
     *
     *
     * @param actionEvent Action event
     *
     *
     */
    @FXML
    public void backToMenuActionPerformed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../application/mainWindow.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) backToMenu.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @method launchNewGame initialise la grille et lance la methode initialize de l'interface
     *
     *
     *
     *
     *
     */

    @FXML
    private void launchNewGame() {
        System.out.println("##############################");
        System.out.println(DataManager.rows + " " + DataManager.columns + " " + DataManager.winningCombo);
        gridToppingPane.getChildren().clear();
        gridToppingPane.setDisable(true);
        DataManager.gameBoard = null;
        initialize();
    }

    /**
     *
     * @method doPlayerTurn effetcue le mouvement du joueur et appelle la méthode checkForWiningPatter et change le tour
     *
     *
     * @param tile Tile
     * @param gameBoard GameBoard
     *
     *
     */

    private void doPlayerTurn(Tile tile, GameBoard gameBoard) {
        tile.setPlayer(gameBoard.getCurrentPlayer());
        System.out.println("- " + tile.getPlayer().getName() + " chose the tile: (" + tile.getX() + ", " + tile.getY() + ")");

        // Animation de la form du joueur
        Animator.animateClickedTile(tile);

        // Verifier un vainqueur
        checkForWinningPattern(tile);

        // verification si la grille est plein
        if (gameBoard.isFull() && !gameBoard.getHasWinner()) {
            System.out.println("No player won this time!");
            play = false;
        }
        else {
            gameBoard.setTurn(gameBoard.getTurn()+1);
        }


        if (play) {
            gameBoard.switchPlayerTurn();

            // mise a jour de la zone de texte
            Animator.changeLabel(currentPlayerTurn, gameBoard.getCurrentPlayer().getName()+"'s Turn", gameBoard.getCurrentPlayer().getColor());
        }
    }

    /**
     *
     * @method doAITurn effectue le mouvement de l'IA
     *
     *
     * @param sleepingTimeMillis int
     * @param gameBoard GameBoard
     *
     *
     */

    private void doAITurn(GameBoard gameBoard, int sleepingTimeMillis) {
        // Pour s'assurer que le joueur ne peut pas jouer pendant le tour de l'IA
        gridToppingPane.setDisable(false);

        // Créez une tâche que le thread AI utilisera
        Runnable AITask = () -> {
            double startTime = System.nanoTime();

            int chosenTile = ClassAI.play(gameBoard.getAvailableTiles());
            for (List<Tile> row : gameBoard.getTiles()) {
                for (Tile tile : row) {
                    if (tile.getPlayer() == null) {
                        if (chosenTile == 0) {
                            // Rend le thread en veille si nécessaire pour que l'IA ne joue pas "instantanément"
                            double endTime = System.nanoTime();
                            int remainingTime = sleepingTimeMillis - (int)(endTime - startTime)/1_000_000;
                            if (remainingTime > 0) {
                                try {
                                    Thread.sleep(remainingTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                                // Faire jouer l'IA (Platform.runLater () pour éviter "IllegalStateException: Not on FX application thread" de GameAnimator
                            Platform.runLater(() -> doPlayerTurn(tile, gameBoard));
                            return;
                        }
                        else {
                            chosenTile--;
                        }
                    }
                }
            }
        };

        // Créer un fil pour l'IA, en utilisant la tâche AI. Le thread est essentiellement là pour appeler "Thread.sleep ()" sans geler l'interface utilisateur
        Thread AIThread = new Thread(AITask);
        AIThread.start();

        // Réactiver le lecteur pour jouer
        gridToppingPane.setDisable(true);
    }

    /**
     *
     * @method checkForWinningPattern verifie la ligne qui permet de gagner
     *
     *
     * @param tile Tile
     *
     *
     */

    private void checkForWinningPattern(Tile tile) {
        //  sune ligne horizontale
        checkInLine(tile, 1, 0);

        //diagonale supérieure gauche à inférieure droite
        checkInLine(tile, 1, 1);

        // ligne verticale
        checkInLine(tile, 0, 1);

        // diagonale supérieure droite à inférieure gauche
        checkInLine(tile, -1, 1);
    }

    /**
     *
     * @method checkInLine verifie la ligne
     *
     *
     * @param
     *
     *
     */

    private void checkInLine(Tile currentTile, int vectorX, int vectorY) {
        int combo = 1;

        //  Sera utilisé si le combo est atteint pour dessiner une ligne dessus
        DataManager.gameBoard.setWinningTiles( new ArrayList<>());
        DataManager.gameBoard.getWinningTiles().add(currentTile);

        //  essayer direction
        combo = checkInDirection(currentTile, combo, vectorX, vectorY);

        // essayer autre direction
        checkInDirection(currentTile, combo, -vectorX, -vectorY);
    }
    /**
     *
     * @method checkInDirection verifie la direction
     *
     *
     * @param
     *
     *
     */
    private int checkInDirection(Tile currentTile, int currentCombo, int vectorX, int vectorY) {
        GameBoard gameBoard = DataManager.gameBoard;
        Player player = currentTile.getPlayer();
        int vectorMultiplier = 1;

        while (true) {
            try {
                Tile tile = gameBoard.getTileAt(currentTile.getX() + (vectorX * vectorMultiplier), currentTile.getY() + (vectorY * vectorMultiplier));
                if (tile.getPlayer() == player) {
                    currentCombo++;
                    gameBoard.getWinningTiles().add(tile);
                    if (currentCombo == gameBoard.getWinningCombo()) {
                        if (!gameBoard.getHasWinner()) {
                            System.out.println(tile.getPlayer().getName() + " won!");
                            currentPlayerTurn.setText(tile.getPlayer().getName() + " won!");
                            // mise a jours du score du vainqueur
                            tile.getPlayer().setScore(tile.getPlayer().getScore()+1);;
                            if (tile.getPlayer() == DataManager.playerOne) {
                                Animator.animateScore(playerOneScore, DataManager.playerOne);
                            }
                            else {
                                Animator.animateScore(playerTwoScore, DataManager.playerTwo);
                            }

                            gameBoard.setHasWinner(true);
                        }

                        // Récupère les coordonnées du motif gagnant
                        Pair<Pair<Double, Double>, Pair<Double, Double>> winningLineCoordinates = gameBoard.getWinningLineXYCoordinates();
                        double x1 = winningLineCoordinates.getKey().getKey();
                        double y1 = winningLineCoordinates.getKey().getValue();
                        double x2 = winningLineCoordinates.getValue().getKey();
                        double y2 = winningLineCoordinates.getValue().getValue();

                        // Trace une ligne pour indiquer le motif gagnant
                        Animator.animateWinningLine(gridToppingPane, x1, y1, x2, y2);

                        play = false;
                        return currentCombo;
                    }
                    vectorMultiplier++;
                }
                else {
                    return currentCombo;
                }
            } catch (IndexOutOfBoundsException e) {
                return currentCombo;
            }
        }
    }

    /**
     *
     * @method initializeGameBoard intialise la grille
     *
     *
     * @param
     *
     *
     */

    private void initializeGameBoard() {
        GameBoard gameBoard = DataManager.gameBoard;

        for (int y = 0; y < gameBoard.getTiles().size(); y++) {

            List<Tile> row = gameBoard.getTiles().get(y);
            for (int x = 0; x < row.size(); x++) {

                Tile tile = row.get(x);
                tile.getPane().setOnMouseClicked(e -> {

                    if ( play && tile.getPlayer() == null) {
                        //Laisser le joueur jouer et mettre à jour l'interface / le plateau de jeu
                        doPlayerTurn(tile, gameBoard);

                        // Si personne n'a encore gagné et qu'il reste des cases disponibles
                        if (play) {
                            //Vérifiez si l'autre joueur est une IA, dans ce cas, laissez l'IA jouer
                            if (!DataManager.gameMode.equals("MultiPlayer")) {
                                doAITurn(gameBoard, 500);

                            }
                        }
                    }
                });

                // Lors du chargement d'une sauvegarde, le plateau de jeu a peut-être déjà été utilisé
                if (tile.getPlayer() != null) {
                    // Animer la forme du joueur sur la case
                    Animator.animateClickedTile(tile);

                    // Vérifiez si le joueur actuel a gagné
                    if (play && gameBoard.getHasWinner()) {
                        checkForWinningPattern(tile);
                    }

                    // Vérifie si la grille de jeu est plein et nécessite une réinitialisation
                    if (play && gameBoard.isFull() && !gameBoard.getHasWinner()) {
                        System.out.println("No player won this time!");
                        play = false;
                    }
                }
                System.out.println();
                gridGame.add(tile.getPane(), x, y);
            }
        }
    }

    /**
     *
     * @method initializeGame initialise la grille et la zone du texte
     *
     *
     * @param gameBoard GameBoard
     *
     *
     */
    private void initializeGame(GameBoard gameBoard) {
        gridGame.getChildren().clear();

        play = true;
        int rows = Settings.numRow;
        int columns = Settings.numColumn;
        int winningCombo = DataManager.winningCombo;

        for (int i = 0; i < rows; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.SOMETIMES);
            gridGame.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < columns; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            gridGame.getRowConstraints().add(rowConstraints);
        }

        if (gameBoard == null) {
            DataManager.gameBoard = new GameBoard(rows, columns, winningCombo, gridGame.getPrefWidth(), gridGame.getPrefHeight());
        }

        initializeGameBoard();
        System.out.println(DataManager.gameBoard.getCurrentPlayer().getName());
        // Initialiser la zone du texte du tour
        Animator.changeLabel(currentPlayerTurn, DataManager.gameBoard.getCurrentPlayer().getName()+"'s Turn", DataManager.gameBoard.getCurrentPlayer().getColor());
    }

    /**
     *
     * @method saveGame calls DataManager's save method and display saveLabel
     *
     *
     * @param
     *
     *
     */
    @FXML
    private void saveGame() throws IOException {
        DataManager.save();
        saveLabel.setText("Game has been saved.");
    }





    /**
     *
     * @method initialize initilise l'interface
     *
     *
     * @param
     *
     *
     */
    @FXML
    public  void initialize(){

        // initialiser le premier joueur
        playerOneName.setText(DataManager.playerOne.getName());
        playerOneScore.setText(String.valueOf(DataManager.playerOne.getScore()));

        // initialiser l'icone du premier joueur
        Image img1 = new Image("file:resources/images/cross.png");
        playerOneImage  = new ImageView(img1);
        playerOneImage.setFitHeight(60.0);
        playerOneImage.setFitWidth(60.0);
        playerOneImage.setX(38.0);
        playerOneImage.setY(1.0);
        playerOneImage.setPreserveRatio(true);
        playerOneImagePane.getChildren().add(playerOneImage);



        // intialiser le deuxieme joueur
        playerTwoName.setText(DataManager.playerTwo.getName());
        playerTwoScore.setText(String.valueOf(DataManager.playerTwo.getScore()));

        // initialiser l'icone du deuxieme joueur
        Image img2 = new Image("file:resources/images/circle.png");
        playerTwoImage  = new ImageView(img2);
        playerTwoImage.setFitHeight(59.0);
        playerTwoImage.setFitWidth(150.0);
        playerTwoImage.setX(14.0);
        playerTwoImage.setY(0.0);
        playerTwoImage.setPreserveRatio(true);
        playerTwoImagePane.getChildren().add(playerTwoImage);


        // initialiser le pane

        initializeGame(DataManager.gameBoard);

    }

    
}
