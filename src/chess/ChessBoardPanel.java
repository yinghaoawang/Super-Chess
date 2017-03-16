package chess;
import chess.piece.Piece;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class ChessBoardPanel extends JPanel {
    ChessGame game = null;
    Board board = null;
    Tile[][] tiles = null;
    Tile hoveredTile = null;
    List<BoardMove> boardMoves = null;
    Rectangle2D[][] rects = null;

    int rows, cols;

    // TODO tempo?
    double tileWidth = 50; // width of tile
    double tileHeight = 50; // height of tile
    double widthOffset = .75; // offset from left is widthOffset * tileWidth
    double heightOffset = .75; // offset from right is heightOffset * tileHeight

    Color pieceBlackColor = new Color(0, 0, 0);
    Color pieceWhiteColor = new Color(215, 215, 215);

    public ChessBoardPanel(ChessGame game) {
        this.game = game;
        board = game.board;
        tiles = board.tiles;
        rows = board.rows;
        cols = board.cols;
        boardMoves = game.boardMoves;
        init();
    }

    void init() {
        initRects();
        initMouseEvents();
    }
    void initRects() {
        rects = new Rectangle2D[rows][cols];

        // needed for rects
        Dimension size = getSize();
        double width = size.getWidth();
        double height = size.getHeight();

        // create the rect
        for (int i = 0; i < rows; ++i) {
            double yPos = tileHeight * (i + heightOffset);
            for (int j = 0; j < cols; ++j) {
                double xPos = tileWidth * (j + widthOffset);
                rects[i][j] = new Rectangle2D.Double(xPos, yPos, tileWidth, tileHeight);
            }
        }
    }

    // TODO refactor
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

        // colors for board
        Color black = new Color(121, 95, 70);
        Color white = new Color(205, 122, 20);

        g2d.setStroke(new BasicStroke(1));
        g.setFont(new Font("Arial", Font.PLAIN, (int)(tileHeight/2.5)));
        for (int i = 0; i < rows; ++i) {
            double xPos = tileWidth - 30;
            double yPos = tileHeight * (i + heightOffset) - 15;
            g2d.drawString(Character.toString(Utilities.rowToChar(i)), (int)(xPos), (int)(yPos + (tileHeight)));
        }
        for (int j = 0; j < cols; ++j) {
            double xPos = tileWidth * (j + widthOffset) + 20;
            double yPos = tileHeight - 20;
            g2d.drawString(Character.toString(Utilities.colToChar(j)), (int)(xPos + 0), (int)(yPos));
        }
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

                Color hoverColor = new Color(50, 50, 50, (int)(0.45 * 255));
                Color selectedColor = new Color(100, 0, 0, (int)(0.75 * 255));
                Color moveTileColor = new Color(0, 0, 255, (int)(0.45 * 255));

                // if selected or hovered, change to respective colors
                if (tile == game.selectedTile) {
                    g2d.setColor(selectedColor);
                }
                else if (game.isAMoveTile(tile)) g2d.setColor(moveTileColor);
                else if (tile == hoveredTile) g2d.setColor(hoverColor);

                // color in the tile
                g2d.fill(rects[i][j]);

                // draw the piece
                Piece piece = tile.peek();
                if (!tile.isEmpty()) {
                    if (piece.isBlack()) g2d.setColor(pieceBlackColor);
                    else g2d.setColor(pieceWhiteColor);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, (int)(tileHeight/1.3)));
                    g2d.drawString(Character.toString(piece.getEncoding()), (int)(xPos + tileWidth/8.5), (int)(yPos + (tileHeight* 4/5)));
                }
            }
        }
    }

    // add mouse listeners and events
    void initMouseEvents() {
        addMouseListener(new MouseAdapter() {
            // TODO refactor this somehow?
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
                //updateBoardMovesTextArea();
                //updateGravesTextAreas();
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

    // action for when mouse hovers over a tile
    void tileHovered(int row, int col) {
        if (game.selectedTile != tiles[row][col])
            hoverTile(row, col);
        else
            unhoverTile();
    }

    // action for when mouse hovers over tile
    void hoverTile(int row, int col) {
        try {
            hoveredTile = tiles[row][col];
        } catch (Exception e) {}
    }
    void unhoverTile() { hoveredTile = null; }

    // add necessary things to draw
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTiles(g);
    }
}
