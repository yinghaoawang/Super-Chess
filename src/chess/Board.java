package chess;
import chess.piece.*;
import chess.move.*;
import chess.Utilities;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.util.ArrayList;

import java.awt.event.*;

public class Board extends JPanel {
    int rows; // rows on board
    int cols; // columns on board

    double tileWidth = 50; // width of tile
    double tileHeight = 50; // height of tile
    double widthOffset = 1; // offset from left is widthOffset * tileWidth
    double heightOffset = 1; // offset from right is heightOffset * tileHeight

    Tile[][] tiles;
    Rectangle2D[][] rects;
    Tile selectedTile = null;
    Tile hoveredTile = null;
    List<Tile> moveTiles = null;

    // constructor that calls init
    Board() { this(8); }
    Board(int cols) { this(cols, cols); }
    Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        tiles = new Tile[rows][cols];
        rects = new Rectangle2D[rows][cols];
        init();
    }

    List<Tile> getMoveTiles(int row, int col) {
        List<Tile> res = new ArrayList<Tile>();
        try {
            Tile tile = tiles[row][col];
            Piece piece = tile.peek();
            for (Move move: piece.getMoves()) {
                Move transMove = move.toTransposed();
                addMoveDiagonals(res, row, col, move);
                if (move.isTransposed()) addMoveDiagonals(res, row, col, transMove);

                if (move.isUntilEnd()) {
                    int multiplier = 2;
                    while (multiplier < Math.max(rows, cols)) {
                        addMoveDiagonals(res, row, col, move, multiplier);
                        if (move.isTransposed()) addMoveDiagonals(res, row, col, transMove, multiplier);
                        ++multiplier;
                    }
                }
            }
        } catch(Exception e) { }
        return res;
    }

    void addMoveDiagonals(List<Tile> res, int row, int col, Move move) {
        addMoveDiagonals(res, row, col, move, 1);
    }
    void addMoveDiagonals(List<Tile> res, int row, int col, Move move, int multiplier) {
        // scale here
        Point[] signConversion = new Point[] {
            new Point(multiplier, multiplier), // quadrant 1 (0)
            new Point(multiplier, -1 * multiplier), // quadrant 2 (1)
            new Point(-1 * multiplier, -1 * multiplier), // quadrant 3 (2)
            new Point(-1 * multiplier, multiplier) // quadrant 4 (3)
        };
        boolean[] swapConversion = new boolean[] {
            false, // quadrant 1
            true, // quadrant 2
            false, // quadrant 3
            true // quadrant 4
        };
        boolean[] quadrants = move.getQuadrants();
        for (int i = 0; i < quadrants.length; ++i) {
            if (quadrants[i] == false) continue;
            int destRow, destCol, rowMove, colMove;
            rowMove = move.getRowMove();
            colMove = move.getColMove();

            if (swapConversion[i] == true) {
                int tmp = rowMove;
                rowMove = colMove;
                colMove = tmp;
            }
            rowMove *= signConversion[i].x; // not really x, just using point for 2 ints
            colMove *= signConversion[i].y;

            destRow = rowMove + row;
            destCol = colMove + col;

            System.out.println(destRow + ", " + destCol);
            if (!isWithinBorders(destRow, destCol)) continue;
            if (!res.contains(tiles[destRow][destCol])) res.add(tiles[destRow][destCol]);
        }
    }

    // set selected tile to designated coordinate
    void selectTile(int row, int col) {
        try {
            selectedTile = tiles[row][col];
            // test
            moveTiles = getMoveTiles(row, col);
            System.out.println(selectedTile.peek().getName());
        } catch (Exception e) {}
    }
    void hoverTile(int row, int col) {
        try {
            hoveredTile = tiles[row][col];
        } catch (Exception e) {}
    }
    void deselectTile() { selectedTile = null; }
    void unhoverTile() { hoveredTile = null; }

    // moves piece at top of src to top of dest
    void movePiece(int srcRow, int srcCol, int destRow, int destCol) {
        movePiece(0, srcRow, srcCol, destRow, destCol);
    }
    // overloaded- finds the piece at src with the given name
    void movePiece(String name, int srcRow, int srcCol, int destRow, int destCol) {
        try {
            int index = tiles[srcRow][srcCol].indexOfName(name);
            if (index < 0)
                throw new Exception("Piece with name \"" + name + "\" not on tile");
            movePiece(index, srcRow, srcCol, destRow, destCol);
        } catch (Exception e) {
            Utilities.printException(e);
        }
    }
    // overloaded- finds the piece at src with the index and moves that one
    void movePiece(int index, int srcRow, int srcCol, int destRow, int destCol) {
        try {
            Tile tile = tiles[srcRow][srcCol];
            Tile destTile = tiles[destRow][destCol];
            Piece piece = tile.remove(index);
            destTile.push(piece);
        } catch (Exception e) {
            Utilities.printException(e);
        }
    }

    // puts a piece to the top of the tile
    void addPiece(Piece piece, int row, int col) {
        tiles[row][col].push(piece);
    }

    // removes and returns the piece at the top of the tile
    Piece removePiece(int row, int col) throws Exception {
        return tiles[row][col].pop();
    }

    // initiates board by calling other inits
    void init() {
        initTiles();
        initPieces();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                for (int i = 0; i < rows; ++i)
                    for (int j = 0; j < cols; ++j) {
                        Rectangle2D rect = rects[i][j];
                        if (rect.contains(me.getPoint())) {
                            unhoverTile();
                            selectTile(i, j);
                            repaint();
                        }
                    }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                super.mouseMoved(me);
                for (int i = 0; i < rows; ++i)
                    for (int j = 0; j < cols; ++j)
                        if (rects[i][j].contains(me.getPoint())) {
                            if (selectedTile != tiles[i][j])
                                hoverTile(i, j);
                            else
                                unhoverTile();
                            repaint();
                        }
            }
        });
    }

    // places pieces on the chess board as they should be
    void initPieces() {
        addPiece(new Rook(Piece.Color.BLACK), 0, 0);
        addPiece(new Knight(Piece.Color.BLACK), 0, 1);
        addPiece(new Bishop(Piece.Color.BLACK), 0, 2);
        addPiece(new Queen(Piece.Color.BLACK), 0, 3);
        addPiece(new King(Piece.Color.BLACK), 0, 4);
        addPiece(new Bishop(Piece.Color.BLACK), 0, 5);
        addPiece(new Knight(Piece.Color.BLACK), 0, 6);
        addPiece(new Rook(Piece.Color.BLACK), 0, 7);
        for (int i = 0; i < 8; ++i) {
            addPiece(new Pawn(Piece.Color.BLACK), 1, i);
        }

        addPiece(new Rook(Piece.Color.WHITE), 7, 0);
        addPiece(new Knight(Piece.Color.WHITE), 7, 1);
        addPiece(new Bishop(Piece.Color.WHITE), 7, 2);
        addPiece(new Queen(Piece.Color.WHITE), 7, 3);
        addPiece(new King(Piece.Color.WHITE), 7, 4);
        addPiece(new Bishop(Piece.Color.WHITE), 7, 5);
        addPiece(new Knight(Piece.Color.WHITE), 7, 6);
        addPiece(new Rook(Piece.Color.WHITE), 7, 7);
        for (int i = 0; i < 8; ++i) {
            addPiece(new Pawn(Piece.Color.WHITE), 6, i);
        }
    }

    // inits and colors the tiles as a chessboard design
    void initTiles() {
        // begin first tile as black
        boolean blackRow = false;
        boolean blackCol = false;

        // needed for rects
        Dimension size = getSize();
        double width = size.getWidth();
        double height = size.getHeight();

        // go through the board
        for (int i = 0; i < rows; ++i) {
            if (blackRow == true) {
                blackCol = true;
            } else {
                blackCol = false;
            }

            for (int j = 0; j < cols ; ++j) {
                // create the rect
                double xPos = tileWidth * (j + widthOffset);
                double yPos = tileHeight * (i + heightOffset);
                rects[i][j] = new Rectangle2D.Double(xPos, yPos, tileWidth, tileHeight);

                if (blackCol) {
                    tiles[i][j] = new Tile(Tile.Color.BLACK);
                } else {
                    tiles[i][j] = new Tile(Tile.Color.WHITE);
                }
                blackCol = !blackCol;
            }

            blackRow = !blackRow;
        }
    }


    // add necessary things to draw
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTiles(g);
    }

    // draws the tiles and pieces on the tiles
    private void drawTiles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // rh makes drawing smooth
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // size values
        Dimension size = getSize();
        double width = size.getWidth();
        double height = size.getHeight();

        Color black = new Color(51, 25, 0); // dark brown for black
        Color white = new Color(204, 102, 0); // light brown for white
        Color pieceBlack = new Color(115, 115, 115); // gray for black pieces
        Color pieceWhite = new Color(255, 255, 255); // white for white pieces

        // draw the mxn board
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols ; ++j) {
                // set positions with scaling
                double xPos = tileWidth * (j + widthOffset);
                double yPos = tileHeight * (i + heightOffset);
                g2d.setStroke(new BasicStroke(1));

                // set tile color
                Tile tile = tiles[i][j];
                if (tile.isBlack()) {
                    g2d.setColor(black);
                } else {
                    g2d.setColor(white);
                }

                // if selected or hovered, change to respective colors
                if (tile == selectedTile) g2d.setColor(Color.red);
                else if (isAMoveTile(tile)) g2d.setColor(Color.blue);
                else if (tile == hoveredTile) g2d.setColor(Color.gray);

                // color in the tile
                //g2d.draw(rect);
                g2d.fill(rects[i][j]);

                // draw the piece
                Piece piece = tile.peek();
                if (!tile.isEmpty()) {
                    if (piece.isBlack()) g2d.setColor(pieceBlack);
                    else g2d.setColor(pieceWhite);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, (int)(tileHeight/2)));
                    g2d.drawString(piece.getEncoding() + "", (int)xPos, (int)(yPos + tileHeight));
                }
            }
        }
    }
    boolean isWithinBorders(int row, int col) {
        if (row >= rows || col >= cols || row < 0 || col < 0) return false;
        return true;
    }
    boolean isAMoveTile(Tile tile) {
        for (Tile t: moveTiles) {
            if (t == tile) return true;
        }
        return false;
    }
}
