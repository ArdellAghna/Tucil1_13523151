import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class InputFile {
    private final int N;
    private final int M;
    private final int P;
    private final String puzzleType;
    private final List<List<String>> puzzlePieces;
    private final List<Character> pieceSymbols;

    public InputFile(String fileName) throws Exception {
        puzzlePieces = new ArrayList<>();
        pieceSymbols = new ArrayList<>();
        Map<Character, List<String>> pieceMap = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            String[] dimensions = scanner.nextLine().trim().split("\\s+");
            if (dimensions.length != 3) {
                throw new Exception("Input tidak valid, format seharusnya N M P");
            } try {
                this.N = Integer.parseInt(dimensions[0]);
                this.M = Integer.parseInt(dimensions[1]);
                this.P = Integer.parseInt(dimensions[2]);
                if (this.P > 26) {
                    throw new Exception("P harus lebih kecil dari 27 ya karena hanya bisa huruf (A-Z)");
                }
            } catch (NumberFormatException e) {
                throw new Exception("Angka tidak valid");
            } 
            this.puzzleType = scanner.nextLine().trim();
            if (!this.puzzleType.equals("DEFAULT")) {
                throw new Exception("Maaf untuk saat ini tipe puzzle hanya bisa DEFAULT");
            }
            List<String> currentPiece = new ArrayList<>();
            char currentChar = ' ';
            Set<Character> usedSymbols = new HashSet<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    if (!currentPiece.isEmpty()) {
                        validateAndAddPiece(currentPiece, currentChar, usedSymbols, pieceMap);
                        pieceSymbols.add(currentChar);
                        currentPiece = new ArrayList<>();
                        currentChar = ' ';
                    } continue;
                } char lineChar = ' ';
                for (char c : line.toCharArray()) {
                    if (Character.isUpperCase(c)) {
                        lineChar = c;
                        break;
                    }
                } if (lineChar == ' ') continue;
                if (currentChar != ' ' && lineChar != currentChar) {
                    if (!currentPiece.isEmpty()) {
                        validateAndAddPiece(currentPiece, currentChar, usedSymbols, pieceMap);
                        pieceSymbols.add(currentChar);
                        currentPiece = new ArrayList<>();
                    }
                } 
                currentChar = lineChar;
                currentPiece.add(line);
            } if (!currentPiece.isEmpty()) {
                validateAndAddPiece(currentPiece, currentChar, usedSymbols, pieceMap);
                pieceSymbols.add(currentChar);
            } if (pieceMap.size() != this.P) {
                throw new Exception("Ketemu " + pieceMap.size() + " pieces nih, Seharusnya " + this.P + " pieces");
            } for (char symbol : pieceSymbols) {
                puzzlePieces.add(pieceMap.get(symbol));
            }

        } catch (FileNotFoundException e) {
            throw new Exception("File tidak ditemukan huhu: " + fileName);
        }
    }

    private void validateAndAddPiece(List<String> piece, char symbol, Set<Character> usedSymbols, 
                                   Map<Character, List<String>> pieceMap) throws Exception {
        if (usedSymbols.contains(symbol)) {
            throw new Exception("OMG Duplicate symbol found!! : " + symbol);
        } for (String line : piece) {
            for (char c : line.toCharArray()) {
                if (c != ' ' && c != symbol) {
                    throw new Exception("Yah invalid karena ada " + c + " di piece: '" + symbol + "'");
                }
            }
        } 
        usedSymbols.add(symbol);
        pieceMap.put(symbol, new ArrayList<>(piece));
    } public int n() { 
        return N; 
    } public int m() { 
        return M; 
    } public int p() { 
        return P; 
    } public String puzzleType() { 
        return puzzleType; 
    } public List<List<String>> pieces() { 
        return puzzlePieces; 
    } public List<Character> pieceSymbols() { 
        return pieceSymbols; 
    }
}
