import java.util.List;

public class Piece {
    private boolean[][] shape;
    private final char symbol;
    private int height;
    private int width;

    public Piece(List<String> pieceStrings, char symbol) {
        this.symbol = symbol;
        this.height = pieceStrings.size();
        this.width = pieceStrings.stream().mapToInt(String::length).max().orElse(0);
        this.shape = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            String line = pieceStrings.get(i);
            for (int j = 0; j < line.length(); j++) {
                shape[i][j] = (line.charAt(j) == symbol);
            }
        } if (!isValidPiece()) {
            throw new IllegalStateException("Konfigurasi piece tidak valid, mohon cek kembali");
        }
    }

    public Piece rotate() {
        boolean[][] newShape = new boolean[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newShape[j][height - 1 - i] = shape[i][j];
            }
        }
        int temp = width;
        width = height;
        height = temp;
        shape = newShape; 
        return this;
    }

    public Piece mirror() {
        boolean[][] newShape = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newShape[i][width - 1 - j] = shape[i][j];
            }
        } 
        shape = newShape;
        return this;
    }

    private boolean isValidPiece() {
        boolean[][] visited = new boolean[height][width];
        boolean found = false;
        int startI = 0, startJ = 0;
        for (int i = 0; i < height && !found; i++) {
            for (int j = 0; j < width && !found; j++) {
                if (shape[i][j]) {
                    startI = i;
                    startJ = j;
                    found = true;
                }
            }
        } 
        return found && (dfs(startI, startJ, visited) == countCells());
    }

    private int dfs(int i, int j, boolean[][] visited) {
        if (i < 0 || i >= height || j < 0 || j >= width || 
            visited[i][j] || !shape[i][j]) {
            return 0;
        } 
        visited[i][j] = true;
        int count = 1;
        count += dfs(i+1, j, visited);
        count += dfs(i-1, j, visited);
        count += dfs(i, j+1, visited);
        count += dfs(i, j-1, visited);
        return count;
    }

    private int countCells() {
        int count = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (shape[i][j]) count++;
            }
        } 
        return count;
    }

    public boolean canPlace(boolean[][] board, int row, int col) {
        if (row + height > board.length || col + width > board[0].length) {
            return false;
        } for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (shape[i][j] && board[row + i][col + j]) {
                    return false;
                }
            }
        } 
        return true;
    }

    public void place(boolean[][] board, char[][] boardSymbols, int row, int col) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (shape[i][j]) {
                    board[row + i][col + j] = true;
                    boardSymbols[row + i][col + j] = symbol;
                }
            }
        }
    }

    public void remove(boolean[][] board, char[][] boardSymbols, int row, int col) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (shape[i][j]) {
                    board[row + i][col + j] = false;
                    boardSymbols[row + i][col + j] = '.';
                }
            }
        }
    }

    public Piece clone() {
        Piece clone = new Piece(symbol);
        clone.height = this.height;
        clone.width = this.width;
        clone.shape = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            System.arraycopy(this.shape[i], 0, clone.shape[i], 0, width);
        } 
        return clone;
    }

    private Piece(char symbol) {
        this.symbol = symbol;
    }
}
