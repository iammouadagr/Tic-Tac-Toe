/**
 * @author Anass Benzekri
 *
 *
 */

/**
 *
 * @class classe qui permet de faire jouer l'IA
 *
 *
 *
 */
package ai;


import controller.DifficultyController;
import utilitaire.DataManager;
import utilitaire.Player;


import java.util.Arrays;

public class ClassAI {


    /**
     *
     * @method play récuperation du model et choix de cases
     *
     * @param  availableTiles int
     *
     *
     */
    public static int play(int availableTiles) {

        MultiLayerPerceptron net = MultiLayerPerceptron.load("./resources/models/"+DifficultyController.fileModel);



        // entrées en fonction de l'état actuel de la de jeu
        double[] inputs = new double[9];
        Arrays.fill(inputs, 0);
        for (int i = 0; i < DataManager.gameBoard.getTiles().size(); i++) {
            for (int j = 0; j < DataManager.gameBoard.getTiles().get(0).size(); j++) {
                // Si la case appartient au joueur
                Player tileOwner = DataManager.gameBoard.getTiles().get(i).get(j).getPlayer();
                if (tileOwner == DataManager.playerOne) {
                    inputs[i*3 + j] = -1;
                }
                // Sinon s'il appartient à l'IA
                else if (tileOwner == DataManager.playerTwo) {
                    inputs[i*3 + j] = 1;
                }
            }
        }

//
        assert net != null;
        double[] outputs = net.forwardPropagation(inputs);


        // Choisir la case qui a le plus de poids
        int chosenTileIndex;
        int availableTileIndex = -1;
        do {
            chosenTileIndex = 0;
            double chosenTileWeight = outputs[0];
            for (int i = 1; i < outputs.length; i++) {
                if (outputs[i] > chosenTileWeight) {
                    chosenTileIndex = i;
                    chosenTileWeight = outputs[i];
                }
            }


            if (inputs[chosenTileIndex] != 0) {
                outputs[chosenTileIndex] = 0;
            }
            else {
                // Récupère l'index de la case disponible au lieu de l'index de la case déja choisi
                availableTileIndex = chosenTileIndex;
                for (int i = 0; i < chosenTileIndex; i++) {
                    if (inputs[i] != 0) {
                        availableTileIndex--;
                    }
                }

            }
        } while (availableTileIndex == -1);

        return availableTileIndex;
    }

}
