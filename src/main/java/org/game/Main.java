package org.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.game.state.GameOverState;
import org.game.state.GameState;
import org.game.state.PlayingState;
import org.game.state.Score;

public class Main extends Application {

    private static final double WIDTH = 800, HEIGHT = 600;
    private double playerSpeed = 5;
    private boolean isJumping = false;
    private double jumpHeight = 0;
    private double jumpMaxHeight = 100;
    private double jumpSpeed = 2;
    private boolean gameOver = false;

    private Circle player;
    private Rectangle ground;
    private Timeline gameLoop;
    private Text scoreText;
    private Text gameOverText;
    private Rectangle gameOverBackground;  // Rectangle pour le fond "Game Over"
    private Score score;

    private GameState currentState;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Ajouter l'arrière-plan
        Image backgroundImage = new Image(getClass().getResource("/images/BACK.jpg").toExternalForm());
        ImageView background = new ImageView(backgroundImage);
        background.setFitWidth(WIDTH);
        background.setFitHeight(HEIGHT);
        root.getChildren().add(background);

        // Initialisation de l'état du jeu
        currentState = new PlayingState();

        score = new Score();
        score.addObserver((o, arg) -> scoreText.setText("Score: " + score.getScore()));

        // Créer le joueur (balle) - Positionnée vers le bas
        player = new Circle(25, Color.BLUE);
        player.setCenterX(WIDTH / 2);
        player.setCenterY(HEIGHT - 50);

        // Créer le sol
        ground = new Rectangle(0, HEIGHT - 10, WIDTH, 10);
        ground.setFill(Color.BROWN);

        // Créer le texte pour le score
        scoreText = new Text(20, 30, "Score: 0");

        // Créer le texte "Game Over"
        gameOverText = new Text(WIDTH / 2 - 50, HEIGHT / 2 + 10, "Game Over!");
        gameOverText.setFill(Color.RED);

        // Créer un rectangle gris pour le fond "Game Over"
        gameOverBackground = new Rectangle(WIDTH / 2 - 60, HEIGHT / 2 - 20, 120, 40);
        gameOverBackground.setFill(Color.GRAY);
        gameOverBackground.setOpacity(0.7); // Rendre le fond semi-transparent pour mieux voir le texte

        gameOverText.setVisible(false); // Masqué au début
        gameOverBackground.setVisible(false); // Masqué au début

        root.getChildren().addAll(player, ground, scoreText, gameOverBackground, gameOverText);

        // Détection des touches
        scene.setOnKeyPressed(event -> {
            if (gameOver) return;  // Si le jeu est terminé, on ignore les mouvements

            if (event.getCode() == KeyCode.RIGHT) {
                player.setCenterX(player.getCenterX() + playerSpeed);
            }
            if (event.getCode() == KeyCode.LEFT) {
                player.setCenterX(player.getCenterX() - playerSpeed);
            }
            if (event.getCode() == KeyCode.UP && !isJumping) {
                isJumping = true;
                jumpHeight = 0;
            }
        });

        // Ajouter un obstacle : un cactus
        Rectangle cactus = new Rectangle(40, 200, Color.GREEN);
        cactus.setX(WIDTH - 200);
        cactus.setY(HEIGHT - 50);
        root.getChildren().add(cactus);

        // Boucle de jeu
        gameLoop = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            if (gameOver) return;  // Si le jeu est terminé, on ne fait rien

            currentState.handleState(this);  // Gérer l'état du jeu

            // Déplacement du cactus (obstacle)
            cactus.setX(cactus.getX() - 2);
            if (cactus.getX() < 0) {
                cactus.setX(WIDTH - 50);  // Réinitialiser la position du cactus
                score.incrementScore();  // Incrémenter le score
            }

            // Vérification de la collision entre le joueur et le cactus
            if (player.getBoundsInParent().intersects(cactus.getBoundsInParent())) {
                gameOver = true;  // Fin du jeu
                currentState = new GameOverState();  // Passer à l'état "Game Over"
                showGameOver();  // Afficher l'écran "Game Over"
            }

            updateGame();  // Met à jour le mouvement de la balle et le saut
        }));

        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        primaryStage.setTitle("Chevalier contre les monstres");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthodes de gestion du jeu
    public void updateGame() {
        if (isJumping) {
            jumpHeight += jumpSpeed;
            player.setCenterY(player.getCenterY() - jumpSpeed);  // Monter
            if (jumpHeight >= jumpMaxHeight) {
                isJumping = false;
            }
        } else {
            if (player.getCenterY() < HEIGHT - 50) {
                player.setCenterY(player.getCenterY() + jumpSpeed);  // Descendre
            } else {
                player.setCenterY(HEIGHT - 50);  // Revenir au sol
            }
        }
    }

    // Méthode pour afficher "Game Over" avec le rectangle
    public void showGameOver() {
        gameOverBackground.setVisible(true);  // Afficher le rectangle gris
        gameOverText.setVisible(true);  // Afficher "Game Over"
        gameLoop.stop();  // Arrêter la boucle du jeu
    }

    public static void main(String[] args) {
        launch(args);
    }
}
