package com.chess.graphics;
import com.chess.board.Board;
import com.chess.ChessGame;
import com.chess.board.Tile;
import com.chess.board.TileCollection;
import com.chess.util.Utilities;
import com.chess.piece.Piece;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

/* This panel is for displaying the Chess' Board */
public class ChessBoardPanel extends JPanel {
    // sizes for the coordinates (a, b, ..., 1, 2, ...)
    private int pieceFontSize = 39;
    private int coordFontSize = 20;
    private double rowCoordWidthOffset = 0;
    private double rowCoordHeightOffset = 6;
    private double colCoordWidthOffset = 38;
    private double colCoordHeightOffset = 18;

    // sizes for the tiles on the board
    private double tileWidth = 50; // width of tile
    private double tileHeight = 50; // height of tile
    private double tileWidthOffset = 18; // offset from left
    private double tileHeightOffset = 25; // offset from top

    // colors for board
    private Color black = new Color(121, 95, 70);
    private Color white = new Color(205, 122, 20);
    private Color tileLineColor = new Color(55, 55, 55);

    // colors for pieces
    Color pieceBlackColor = new Color(0, 0, 0);
    Color pieceWhiteColor = new Color(225, 225, 225);

    // colors for tiles
    Color hoverColor = new Color(50, 50, 50, (int)(0.45 * 255));
    Color selectedColor = new Color(100, 0, 0, (int)(0.75 * 255));
    Color moveTileColor = new Color(0, 0, 255, (int)(0.45 * 255));
    Color checkColor = new Color(175, 0, 0, (int)(.85 * 255));

    // non hardcoded fields
    private ChessGame game = null;
    private Board board = null;
    private Tile hoveredTile = null;
    private Rectangle2D[][] rects = null;

    private int rows, cols;

    public ChessBoardPanel(ChessGame game) {
        this.game = game;
        board = game.getBoard();
        rows = game.getRows();
        cols = game.getCols();
        init();
    }

    // initialization
    void init() {
        initRects();
        initEventListeners();
    }
    void initRects() {
        rects = new Rectangle2D[rows][cols];

        // needed for rects
        Dimension size = getSize();
        double width = size.getWidth();
        double height = size.getHeight();

        // create the rect
        for (int i = 0; i < rows; ++i) {
            double yPos = tileHeight * i + tileHeightOffset;
            for (int j = 0; j < cols; ++j) {
                double xPos = tileWidth * j + tileWidthOffset;
                rects[i][j] = new Rectangle2D.Double(xPos, yPos, tileWidth, tileHeight);
            }
        }
    }

    // draw the coordinates on side of chess board a, b, c... 1, 2, 3...
    private void drawCoords(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // draw coordinates a1, etc.
        g2d.setStroke(new BasicStroke());
        g.setFont(new Font("Arial", Font.PLAIN, (int)(coordFontSize)));
        for (int i = 0; i < rows; ++i) {
            double xPos = rowCoordWidthOffset;
            double yPos = rowCoordHeightOffset + tileHeight * i;
            g2d.drawString(Character.toString(Utilities.rowToChar(i)), (int)(xPos), (int)(yPos + (tileHeight)));
        }
        for (int j = 0; j < cols; ++j) {
            double xPos = colCoordWidthOffset + tileWidth * j;
            double yPos = colCoordHeightOffset;
            g2d.drawString(Character.toString(Utilities.colToChar(j)), (int)(xPos), (int)(yPos));
        }
    }

    // draws the pieces character encodings which appear like real chess pieces
    private void drawPieces(Graphics g) {
        TileCollection tiles = board.getTiles();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke());
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols ; ++j) {
                double xPos = tileWidth * j + tileWidthOffset;
                double yPos = tileHeight * i + tileHeightOffset;

                // draw the piece
                Tile tile = tiles.get(i, j);
                Piece piece = tile.peek();
                if (!tile.isEmpty()) {
                    if (piece.isBlack()) g2d.setColor(pieceBlackColor);
                    else g2d.setColor(pieceWhiteColor);
                    g2d.setFont(new Font("TimesRoman", Font.PLAIN, pieceFontSize));
                    g2d.drawString(Character.toString(piece.getEncoding()), (int)(xPos + tileWidth/8.5), (int)(yPos + (tileHeight* 4/5)));
                }
            }
        }
    }

    // draws the tiles and pieces on the tiles
    private void drawTiles(Graphics g) {
        TileCollection tiles = board.getTiles();
        Graphics2D g2d = (Graphics2D) g;

        // rh makes drawing smooth
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // draw the mxn board
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols ; ++j) {
                // set positions with scaling
                g2d.setStroke(new BasicStroke(1));

                // set tile color
                Tile tile = tiles.get(i, j);
                if (tile.isBlack()) {
                    g2d.setColor(black);
                } else {
                    g2d.setColor(white);
                }

                Piece p = tile.peek();
                boolean isCheck = game.isAnyCheck();

                // if selected or hovered, change to respective colors
                if (tile == game.getSelectedTile()) {
                    g2d.setColor(selectedColor);
                }
                else if (game.isInMoveTiles(tile)) g2d.setColor(moveTileColor);
                else if (isCheck && p != null && p.getName() == "King" && game.isInDanger(p)) g2d.setColor(checkColor);
                else if (tile == hoveredTile) g2d.setColor(hoverColor);

                // color in the tile
                g2d.fill(rects[i][j]);

                g2d.setColor(tileLineColor);
                g2d.draw(rects[i][j]);

            }
        }
    }

    // add mouse listeners and events
    void initEventListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                super.mousePressed(me);
                // check if mouse clicked on a tile (rect representing the tile)
                for (int i = 0; i < rows; ++i) {
                    for (int j = 0; j < cols; ++j) {
                        Rectangle2D rect = rects[i][j];
                        if (rect.contains(me.getPoint())) {
                            unhoverTile();
                            game.tilePressed(i, j);
                        }
                    }
                }
            }
            // text updates and repainting moved to mouse release due to bugginess if in pressed
            @Override
            public void mouseReleased(MouseEvent me) {
                super.mouseReleased(me);
                repaint();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                super.mouseMoved(me);
                // check if mouse is hovering over a tile (rect representing the tile, since rect is a component)
                for (int i = 0; i < rows; ++i) {
                    for (int j = 0; j < cols; ++j) {
                        if (rects[i][j].contains(me.getPoint())) {
                            tileHovered(i, j);
                            repaint();
                        }
                    }
                }
            }
        });
    }

    // action for mouseevent
    private void tileHovered(int row, int col) {
        TileCollection tiles = board.getTiles();
        if (game.getSelectedTile() != tiles.get(row, col))
            hoverTile(row, col);
        else
            unhoverTile();
    }

    // action for when mouse hovers over tile
    private void hoverTile(int row, int col) {
        TileCollection tiles = board.getTiles();
        try {
            hoveredTile = tiles.get(row, col);
        } catch (Exception e) {}
    }
    //  action for when mouse no longer hovers over tile
    private void unhoverTile() { hoveredTile = null; }

    // add necessary things to draw
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCoords(g);
        drawTiles(g);
        drawPieces(g);
    }

    @Override
    public Dimension getPreferredSize() {
        int width = (int)(tileWidth * cols + tileWidthOffset);
        int height = (int)(tileHeight * rows + tileHeightOffset);
        return new Dimension(width, height);
    }
}
