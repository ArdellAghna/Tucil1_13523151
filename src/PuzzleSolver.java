public class PuzzleSolver {
    private final Board board;
    private final Piece[] pieces;
    private final boolean[] used;
    private long attempt;
    private long startTime;
    private long endTime;
    private boolean solved;

    public PuzzleSolver(Board board, Piece[] pieces) {
        this.board = board;
        this.pieces = pieces;
        this.used = new boolean[pieces.length];
        this.attempt = 0;
        this.startTime = 0;
        this.endTime = 0;
        this.solved = false;
    }

    public boolean solve() {
        startTime = System.currentTimeMillis();
        solved = solver();
        endTime = System.currentTimeMillis();
        return solved;
    }

    private boolean solver() {
        attempt++;
        boolean allUsed = true;
        for (boolean u : used) {
            if (!u) {
                allUsed = false;
                break;
            }
        } if (allUsed) {
            for (int i = 0; i < board.rows(); i++) {
                for (int j = 0; j < board.cols(); j++) {
                    if (!board.Occupied()[i][j]) {
                        return false;
                    }
                }
            } 
            return true;
        } for (int p = 0; p < pieces.length; p++) {
            if (!used[p]) {
                for (int i = 0; i < board.rows(); i++) {
                    for (int j = 0; j < board.cols(); j++) {
                        Piece piece = pieces[p].clone();
                        for (int rot = 0; rot < 4; rot++) {
                            if (piece.canPlace(board.Occupied(), i, j)) {
                                piece.place(board.Occupied(), board.Symbols(), i, j);
                                used[p] = true;
                                if (solver()) {
                                    return true;
                                } 
                                used[p] = false;
                                piece.remove(board.Occupied(), board.Symbols(), i, j);
                            } piece.rotate();
                        } 
                        piece = pieces[p].clone();
                        piece.mirror();
                        for (int rot = 0; rot < 4; rot++) {
                            if (piece.canPlace(board.Occupied(), i, j)) {
                                piece.place(board.Occupied(), board.Symbols(), i, j);
                                used[p] = true;
                                if (solver()) {
                                    return true;
                                } 
                                used[p] = false;
                                piece.remove(board.Occupied(), board.Symbols(), i, j);
                            } piece.rotate();
                        }
                    }
                }
            }
        } 
        return false;
    } public boolean isSolved() {
        return solved;
    } public long Start() {
        return startTime;
    } public long Time() {
        return endTime - startTime;
    } public long attempts() {
        return attempt;
    } public Board Board() {
        return board;
    }
}
