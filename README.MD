## Chess Checklist
- [ ] Base Chess Engine
    - [x] Create pieces with respective moves
        - [x] Pieces
            - [x] Colors
            - [x] Moves
                - [x] Scaleable moves (i.e A knight that can move in constant Ls and can be blocked)
                - [x] Implementation of moves given characteristics
            - [x] Pawn Moves
        - [x] Base chess Pieces (Knight, Rook, etc.)
    - [x] GUI
        - [x] Chessboard tiles
        - [x] Chesspiece drawing (Just settled on drawing encodings)
            - [x] White
            - [x] Black
        - [x] Piece moving (Click piece and click target tile)
            - [x] Initial piece selection
            - [x] Possible moves
        - [x] Text
            - [x] Tile names (A1, C5, etc.)
            - [x] Previous moves
        - [x] Graves (eaten pieces)
    - [ ] Rules
        - [ ] Check
        - [ ] Checkmate
        - [x] Blocking
    - [x] 2 players
        - [x] Turns
        - [x] Graves
    - [ ] Modularize
        - Chess game
            - [ ] Chess game
        - Displays
            - [x] Chess game frame
            - [x] Chess board panel
            - [x] Chess board move panel
            - [ ] Chess grave panel
            - [ ] Player panel

- [ ] Chess Engine Extras
    - [ ] Rules
        - [ ] Draw
        - [ ] En passant
        - [ ] Castling
    - [ ] State saving
        - [ ] Loadable chess board from text file
        - [ ] Save/load user state
    - [ ] Artificial Intelligence (AI)
        - [ ] Open source
            - [ ] One difficulty
            - [ ] Multiple difficulties
        - [ ] Custom script
    - [ ] Network
        - [ ] Play over local network
        - [ ] Play over TCP

### Description:
I make a scaleable chess engine such that it may be able to create awesome pieces with insane moves. First time making an actual graphical project from scratch. It is very satisfying thus far.  
The moves that the pieces aren't hardcoded! A pawn can be set to behave like a Queen (or any other piece) in no more than 5 lines of code!  
This project first started as an attempt of a custom scaleable impementation for Stanford's CS106A.  
To see the others click [here](https://github.com/yinghaoawang/DIY-CS106A.git)

### Things learned:
- Due to packaging, I now learn of the importance of class access. Especially package-private.
- Using a different Makefile given a package with subdirectories (i think that's what you call them, i.e chess/piece/\* etc)
- Further solidified the concept of mapping x to columns, and y to rows
- Slightly better understanding of the placement of try catch blocks and throwing error exceptions
- Basics of character encodings in java
- Exercised the practice of writing readable code (i.e the board move determination from 4-6 nested for loops to 2)

## Progress GIFs
### 3/16 modularity [blueprint](https://raw.githubusercontent.com/yinghaoawang/Super-Chess/master/images/blueprint_3-16-17.png)

### 3/16
![3/16](images/gifs/super-chess_3-16-17.gif)

*Update: Graveyard and display added*

### 3/15
![3/15](images/gifs/super-chess_3-15-17.gif)

*Update: Moveable pieces and most piece move logic (blocking, extending of moves, etc.)  
List of board moves displayed in text area*

### 3/14
![3/14](images/gifs/super-chess_3-14-17.gif)

*Update: Visual GUI with mouse events, board movement determination, piece encoding display*

### 3/09
![3/09](images/gifs/super-chess_3-09-17.gif)

*Initial: Board, tile, selected tile, move, and piece base logic  
Graphics for chessboard tiles, and drawn symbols for pieces*
