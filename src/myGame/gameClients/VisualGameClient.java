package myGame.gameClients;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by liamkreiss on 6/15/18.
 */
public class VisualGameClient extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage theStage) {
        theStage.setTitle("Plus Four");

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene( theScene );

        Canvas canvas = new Canvas( 512, 512 );
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();




        //makes the board (with black tiles)
        int curX = 150, curY = 150, size = 50;
        gc.setFill(Color.BLACK);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gc.fillRect(curX, curY, size - 2, size - 2);
                curX += size;
            }
            curX -= 4*size;
            curY += size;
        }

        //Place red piece
        gc.setFill(Color.RED);
        int centerXRed = 175 + (size/2), centerYRed = 100, radiusRed = (size-4)/2;
        gc.fillOval(centerXRed - radiusRed, centerYRed - radiusRed, radiusRed * 2, radiusRed * 2);
        Circle redPiece = new Circle(centerXRed, centerYRed, radiusRed);

        //Place black piece
        gc.setFill(Color.BLUE);
        int centerXBlue = 275 + (size/2), centerYBlue = 100, radiusBlue = (size-4)/2;
        gc.fillOval(centerXBlue - radiusBlue, centerYBlue - radiusBlue, radiusBlue * 2, radiusBlue * 2);
        Circle bluePiece = new Circle(centerXBlue, centerYBlue, radiusBlue);

        theScene.setOnMouseClicked(
                new EventHandler<MouseEvent>()
                {
                    public void handle(MouseEvent e)
                    {
                        if ( redPiece.contains( e.getX(), e.getY() ) )
                        {
                            System.out.println("clicked the red piece");
                        }
                    }
                });

        theScene.setOnMouseClicked(
                new EventHandler<MouseEvent>()
                {
                    public void handle(MouseEvent e)
                    {
                        if ( bluePiece.contains( e.getX(), e.getY() ) )
                        {
                            System.out.println("clicked the blue piece");
                        } else if ( redPiece.contains( e.getX(), e.getY() ) )
                        {
                            System.out.println("clicked the red piece");
                        }
                    }
                });


//        ArrayList<String> input = new ArrayList<String>();
//
//        theScene.setOnKeyPressed(
//                e -> {
//                    String code = e.getCode().toString();
//
//                    // only add once... prevent duplicates
//                    if ( !input.contains(code) )
//                        input.add( code );
//                });
//
//        theScene.setOnKeyReleased(
//                e -> {
//                    String code = e.getCode().toString();
//                    input.remove( code );
//                });
//
//
//        Image left = new Image( "left.png" );
//        Image leftG = new Image( "leftG.png" );
//
//        Image right = new Image( "right.png" );
//        Image rightG = new Image( "rightG.png" );
//
//        new AnimationTimer()
//        {
//            public void handle(long currentNanoTime)
//            {
//                // Clear the canvas
//                gc.clearRect(0, 0, 512,512);
//
//                if (input.contains("LEFT"))
//                    gc.drawImage( leftG, 64, 64 );
//                else
//                    gc.drawImage( left, 64, 64 );
//
//                if (input.contains("RIGHT"))
//                    gc.drawImage( rightG, 256, 64 );
//                else
//                    gc.drawImage( right, 256, 64 );
//            }
//        }.start();


        theStage.show();
    }


}
