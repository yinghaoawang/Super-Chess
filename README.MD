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
        - [x] Check
        - [ ] Checkmate
        - [x] Blocking
        - [ ] En Passant
        - [ ] Castling
        - [ ] Game states
            - [ ] Win/Loss
            - [ ] Draw
            - [ ] Restart
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
            - [x] Chess grave panel
            - [ ] Player panel

<!---
- [ ] Chess Engine Extras
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
-->

### Description:
Super Chess is a chess engine that is able to create pieces with dynamic moves easily.

### How to run
- Download/clone the program

#### On command line:
- Enter the directory containing the Makefile
- Run the command "Make"
- Run the command "./run"

#### On Intellij IDEA:
- Open the project folder
- Compile and run ChessGameRunner

### Sample Modifications
```java
// Sample code goes here
```

### Things learned:
- Due to packaging, I now learn of the importance of class access. Especially package-private.
- Using a different Makefile given a package with subdirectories (i think that's what you call them, i.e chess/piece/\* etc)
- Further solidified the concept of mapping x to columns, and y to rows
- Slightly better understanding of the placement of try catch blocks and throwing error exceptions
- Basics of character encodings in java
- Exercised the practice of writing readable code (i.e the board move determination from 4-6 nested for loops to 2)
- The active practice of "high cohesion, loose coupling" (primarily when I split up the board and created logic and display modules)

## Progress Visuals
### 1/5/18
![1/5](https://i.imgur.com/UJik6kw.png)
*Update: Check added and working. Works for moving king, blocking king, and eating threat*

### 1/2/18
![1/2](https://i.imgur.com/37ZuYo2.png)
*Update: Back to working on this project. Layout made modular and smooth, twitchy graveyard fix*

### 3/16/17 modularity [blueprint](https://raw.githubusercontent.com/yinghaoawang/Super-Chess/master/images/blueprint_3-16-17.png)

### 3/16/17
![3/16](images/gifs/super-chess_3-16-17.gif)

*Update: Graveyard and display added*

### 3/15/17
![3/15](images/gifs/super-chess_3-15-17.gif)

*Update: Moveable pieces and most piece move logic (blocking, extending of moves, etc.)  
List of board moves displayed in text area*

### 3/14/17
![3/14](images/gifs/super-chess_3-14-17.gif)

*Update: Visual GUI with mouse events, board movement determination, piece encoding display*

### 3/09/17
![3/09](images/gifs/super-chess_3-09-17.gif)

*Initial: Board, tile, selected tile, move, and piece base logic  
Graphics for chessboard tiles, and drawn symbols for pieces*
