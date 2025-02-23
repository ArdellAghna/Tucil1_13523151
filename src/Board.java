public class Board {
    private final int rows;
    private final int cols;
    private final boolean[][] occupied;
    private final char[][] symbols;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.occupied = new boolean[rows][cols];
        this.symbols = new char[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                symbols[i][j] = '.';
            }
        }
    }

    public void display() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(symbols[i][j] + " ");
            } System.out.println();
        } System.out.println();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(symbols[i][j]);
            } sb.append('\n');
        } 
        return sb.toString();
    } public boolean[][] Occupied() {
        return occupied;
    } public char[][] Symbols() {
        return symbols;
    } public int rows() {
        return rows;
    } public int cols() {
        return cols;
    }
}
