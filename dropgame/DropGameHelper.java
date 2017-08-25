package dropgame;

public class DropGameHelper
{
  private DropGamePiece[][] board;
  private int numRows, numCols;

  public DropGameHelper( int numRows, int numCols ) {
    board = new DropGamePiece[numRows][numCols];
    this.numRows = numRows;
    this.numCols = numCols;

    for ( int r = 0; r < numRows; r++ )
      for ( int c = 0; c < numCols; c++ )
        board[r][c] = null;
  }

  public int numRows() { return numRows; }
  public int numCols() { return numCols; }

  public boolean isWinner4( String p ) {
    for ( int r = 0; r < numRows; r++ )
      for ( int c = 0; c < numCols; c++ )
        if ( winCheck4(p, board[r][c]) )
          return true;
    return false;
  }

  public boolean isWinner( String p ) {
    for ( int r = 0; r < numRows; r++ )
      for ( int c = 0; c < numCols; c++ )
        if ( winCheck(p, board[r][c]) )
          return true;
    return false;
  }

  private boolean winCheck4( String p, DropGamePiece piece ) {
    if ( piece == null )
      return false;
    if ( ! p.equals(piece.getSymbol()) )
      return false;
    Location loc = piece.getLocation();
    if ( ! isValid(loc) )
      return false;

    Location left1, left2, left3, right1, right2, right3;
    Location below1, below2, below3, above1, above2, above3;
    Location aldiag1, aldiag2, aldiag3, ardiag1, ardiag2, ardiag3;
    Location bldiag1, bldiag2, bldiag3, brdiag1, brdiag2, brdiag3;
    int hCount = 0, vCount = 0, dlCount = 0, drCount = 0;
    left1 = loc.left();
    left2 = left1.left();
    left3 = left2.left();
    right1 = loc.right();
    right2 = right1.right();
    right3 = right2.right();
    below1 = loc.below();
    below2 = below1.below();
    below3 = below2.below();
    above1 = loc.above();
    above2 = above1.above();
    above3 = above2.above();
    aldiag1 = loc.custom(-1,-1);
    aldiag2 = loc.custom(-2,-2);
    aldiag3 = loc.custom(-3,-3);
    ardiag1 = loc.custom(1,-1);
    ardiag2 = loc.custom(2,-2);
    ardiag3 = loc.custom(3,-3);
    bldiag1 = loc.custom(-1,1);
    bldiag2 = loc.custom(-2,2);
    bldiag3 = loc.custom(-3,3);
    brdiag1 = loc.custom(1,1);
    brdiag2 = loc.custom(2,2);
    brdiag3 = loc.custom(3,3);
    if ( isValid(left1) && p.equals(this.playerAt(left1)) ) {
      hCount++;
      if ( isValid(left2) && p.equals(this.playerAt(left2)) ) {
        hCount++;
        if ( isValid(left3) && p.equals(this.playerAt(left3)) )
          hCount++;
      }
    }
    if ( isValid(right1) && p.equals(this.playerAt(right1)) ) {
      hCount++;
      if ( isValid(right2) && p.equals(this.playerAt(right2)) ) {
        hCount++;
        if ( isValid(right3) && p.equals(this.playerAt(right3)) )
          hCount++;
      }
    }
    if ( isValid(below1) && p.equals(this.playerAt(below1)) ) {
      vCount++;
      if ( isValid(below2) && p.equals(this.playerAt(below2)) ) {
        vCount++;
        if ( isValid(below3) && p.equals(this.playerAt(below3)) )
          vCount++;
      }
    }
    if ( isValid(above1) && p.equals(this.playerAt(above1)) ) {
      vCount++;
      if ( isValid(above2) && p.equals(this.playerAt(above2)) ) {
        vCount++;
        if ( isValid(above3) && p.equals(this.playerAt(above3)) )
          vCount++;
      }
    }
    if ( isValid(aldiag1) && p.equals(this.playerAt(aldiag1)) ) {
      dlCount++;
      if ( isValid(aldiag2) && p.equals(this.playerAt(aldiag2)) ) {
        dlCount++;
        if ( isValid(aldiag3) && p.equals(this.playerAt(aldiag3)) )
          dlCount++;
      }
    }
    if ( isValid(brdiag1) && p.equals(this.playerAt(brdiag1)) ) {
      dlCount++;
      if ( isValid(brdiag2) && p.equals(this.playerAt(brdiag2)) ) {
        dlCount++;
        if ( isValid(brdiag3) && p.equals(this.playerAt(brdiag3)) )
          dlCount++;
      }
    }
    if ( isValid(ardiag1) && p.equals(this.playerAt(ardiag1)) ) {
      drCount++;
      if ( isValid(ardiag2) && p.equals(this.playerAt(ardiag2)) ) {
        drCount++;
        if ( isValid(ardiag3) && p.equals(this.playerAt(ardiag3)) )
          drCount++;
      }
    }
    if ( isValid(bldiag1) && p.equals(this.playerAt(bldiag1)) ) {
      drCount++;
      if ( isValid(bldiag2) && p.equals(this.playerAt(bldiag2)) ) {
        drCount++;
        if ( isValid(bldiag3) && p.equals(this.playerAt(bldiag3)) )
          drCount++;
      }
    }
    return (hCount>=3 || vCount>=3 || dlCount>=3 || drCount>=3);
  }

  private boolean winCheck( String p, DropGamePiece piece ) {
    if ( piece == null )
      return false;
    if ( ! p.equals(piece.getSymbol()) )
      return false;
    Location loc = piece.getLocation();
    if ( ! isValid(loc) )
      return false;

    Location left, right, below, above;
    int adjacentCount = 0;
    left = loc.left();
    right = loc.right();
    below = loc.below();
    above = loc.above();
    if ( isValid(left) && p.equals(this.playerAt(left)) )
      adjacentCount++;
    if ( isValid(right) && p.equals(this.playerAt(right)) )
      adjacentCount++;
    if ( isValid(below) && p.equals(this.playerAt(below)) )
      adjacentCount++;
    if ( isValid(above) && p.equals(this.playerAt(above)) )
      adjacentCount++;

    return adjacentCount >= 3;
  }

  public boolean isFull() {
    int usedCount = 0;
    for ( int r = 0; r < numRows; r++ )
      for ( int c = 0; c < numCols; c++ )
        if ( board[r][c] != null )
          usedCount++;

    return usedCount == numRows*numCols;
  }

  public boolean isFull(int col) {
    assert isValid(col);
    int nullCount = 0;
    for ( int r = 0; r < numRows; r++ )
      if ( board[r][col] == null )
        nullCount++;

    return nullCount == 0;
  }

  public boolean isValid( Location loc ) {
    return ( 0 <= loc.row && loc.row < numRows
        && 0 <= loc.col && loc.col < numCols
    );
  }

  public boolean isValid( int col ) {
    return ( 0 <= col && col < this.numCols );
  }

  public String playerAt( Location loc ) {
    if ( isValid(loc) && board[loc.row][loc.col] != null )
      return board[loc.row][loc.col].getSymbol();
    else if ( isValid(loc) && board[loc.row][loc.col] == null )
      return " ";
    else
      return "ERROR: " + loc;
  }

  public String toString() {
    String out = "", line;
    for ( int r = 0; r < numRows; r++ ) {
      line = "|";
      for ( int c = 0; c < numCols; c++ ) {
        DropGamePiece piece = board[r][c];
        if ( piece == null )
          line += "_|";
        else
          line += piece.getSymbol() + "|";
      }
      out += line + "\n";
    }
    // draw numbers under each column
    line = "|";
    for ( int c = 0; c < numCols; c++ )
      line += c + "|";
      out += line + "\n";
      return out;
  }

  public int findLowestEmptyRow( int col ) {
    assert isValid(col);
    assert ! isFull(col);
    int row = numRows-1;
    while ( board[row][col] != null )
      row--;
    return row;
  }

  public void playMove( String p, int col ) {
    assert isValid(col);
    assert ! isFull(col);
    int row = findLowestEmptyRow(col);
    board[row][col] = new DropGamePiece(row, col, p);
  }

  public void putPiece( String p, int row, int col ) {
    assert isValid(col);
    assert ! isFull(col);
    board[row][col] = new DropGamePiece(row, col, p);
  }

  public void removePiece( int row, int col ) {
    board[row][col] = null;
  }

  public void removeHighlight( int col ) {
    int row;
    if ( isFull(col) )
      row = 0;
    else
      row = findLowestEmptyRow(col) + 1;
    board[row][col] = null;
  }
}
