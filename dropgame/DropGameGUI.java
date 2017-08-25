package dropgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DropGameGUI extends Application {
  private GraphicsContext gc;
  private int player = 1;
  private DropGameHelper game;
  private boolean started = false;
  private boolean gameOver = false;
  private boolean highlighted = false;
  private boolean connect4 = true;
  private Location[][] locs;

  @Override
  public void start(Stage stage) {
    Group root = new Group();
    Scene scene = new Scene(root);

    Canvas canvas = new Canvas(900, 800);
    gc = canvas.getGraphicsContext2D();

    game = new DropGameHelper(6, 7);
    locs = new Location[6][7];
    for ( int r=0; r<locs.length; r++ )
      for ( int c=0; c<locs[0].length; c++ )
        locs[r][c] = new Location(r,c);
    gc.setStroke(Color.BLACK);

    canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
        new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        if ( gameOver ) {
          started = false;
          gameOver = false;
          game = new DropGameHelper(6, 7);
        }
        else if ( ! started ) {
          if ( t.getX()>=120 && t.getX()<=370 && t.getY()>=305 && t.getY()>=405 ) {
            connect4 = true;
            started = true;
          }
          if ( t.getX()>=500 && t.getX()<=750 && t.getY()>=305 && t.getY()>=405 ) {
            connect4 = false;
            started = true;
          }
        }
        else {
          int xOffset = 100, colWidth = 100;
          int col = ((int)t.getX() - xOffset) / colWidth;
          for ( int r=0; r<game.numRows(); r++ )
            for ( int c=0; c<game.numCols(); c++ )
              if ( game.playerAt(locs[r][c]).equals("3") )
                game.removeHighlight(c);
          if ( game.isValid(col) && ! game.isFull(col) ) {
            for ( int i = 0; i < game.findLowestEmptyRow(col); i++ ) {
              game.putPiece(""+player, i, col);
              //game.removePiece(i, col);
            }
            game.playMove(""+player, col);
            player = (player%2) + 1;
          }
          //game.playMove(""+3, col);
          highlighted = false;
        }
      }
    });

    canvas.addEventHandler(MouseEvent.MOUSE_MOVED,
        new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        int col = ((int)t.getX() - 100) / 100;
        //game.removeHighlight(col);
        for ( int r=0; r<game.numRows(); r++ ) {
          for ( int c=0; c<game.numCols(); c++ ) {
            if ( game.playerAt(locs[r][c]).equals("3") ) {
              if ( c != col ) {
                game.removeHighlight(c);
                highlighted = false;
              }
            }
          }
        }
        if ( game.isValid(col) && ! game.isFull(col) && ! highlighted ) {
          game.playMove(""+3, col);
          highlighted = true;
        }
      }
    });

    root.getChildren().add(canvas);

    stage.setTitle("Drop Game");
    stage.setScene(scene);
    stage.show();

    new AnimationTimer() {
      @Override
      public void handle(long now) {
        drawEverything();
      }
    }.start();
  }

  public void drawEverything() {
    gc.setFill(Color.WHITE);
    gc.fillRect(0,0,900,800);

    if ( ! started )
    {
      gc.setFill(Color.RED);
      gc.fillRect(120,405,250,100);
      gc.setFill(Color.YELLOW);
      gc.fillRect(500,405,250,100);
      gc.setFill(Color.BLACK);
      gc.strokeRect(120,405,250,100);
      gc.strokeRect(500,405,250,100);
      gc.setFont(Font.font("Arial", FontWeight.BOLD, 72) );
      gc.fillText( "DROP GAMES", 200, 150 );
      gc.setFont(Font.font("Arial", FontWeight.BOLD, 36) );
      gc.fillText( "Connect 4", 150, 470 );
      gc.fillText( "Clump 4", 550, 470 );
    }
    else
    {
      drawBoard();
      if ( connect4 ) {
        gameOver = game.isWinner4("1") || game.isWinner4("2") || game.isFull();
        if ( gameOver ) {
          gc.setFont(Font.font("Arial", FontWeight.BOLD, 120));
          gc.setFill(Color.BLACK);
          if ( game.isWinner4("1") )
            gc.fillText("Player 1 wins!", 50, 400);
          if ( game.isWinner4("2") )
            gc.fillText("Player 2 wins!", 50, 400);
          if ( game.isFull() )
            gc.fillText("TIE GAME", 150, 400);
        }
        else {
          gc.setFont(Font.font("Arial", FontWeight.BOLD, 72));
          gc.setFill(Color.BLACK);
          gc.fillText("Player " + player + ", go.", 30, 750);
        }
      } else {
        gameOver = game.isWinner("1") || game.isWinner("2") || game.isFull();
        if ( gameOver ) {
          gc.setFont(Font.font("Arial", FontWeight.BOLD, 120));
          gc.setFill(Color.BLACK);
          if ( game.isWinner("1") )
            gc.fillText("Player 1 wins!", 50, 400);
          if ( game.isWinner("2") )
            gc.fillText("Player 2 wins!", 50, 400);
          if ( game.isFull() )
            gc.fillText("TIE GAME", 150, 400);
        }
        else {
          gc.setFont(Font.font("Arial", FontWeight.BOLD, 72));
          gc.setFill(Color.BLACK);
          gc.fillText("Player " + player + ", go.", 30, 750);
        }
      }
    }
  }

  public void drawBoard() {
    double xOffset = 100;
    double yOffset = 50;
    double colWidth = 100;
    double rowWidth = 100;
    for ( int r=0; r<game.numRows(); r++ ) {
      for ( int c=0; c<game.numCols(); c++ ) {
        double x = xOffset + c*colWidth;
        double y = yOffset + r*rowWidth;
        if ( game.playerAt(locs[r][c]).equals(" ") )
          gc.strokeOval(x, y, 80, 80);
        else {
          if ( game.playerAt(locs[r][c]).equals("1") )
            gc.setFill(Color.RED);
          else if ( game.playerAt(locs[r][c]).equals("2") )
            gc.setFill(Color.YELLOW);
          else if ( game.playerAt(locs[r][c]).equals("3") )
            gc.setFill(Color.GRAY);
          gc.fillOval(x, y, 80, 80);
        }
      }
    }
  }

  public static void main( String[] args ) { launch(args); }
}
