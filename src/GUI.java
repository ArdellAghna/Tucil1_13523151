import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class GUI extends JFrame {
    private static final String TITLE = "IQ PUZZLER PRO SOLVER";
    private static final int MIN_WIDTH = 500;
    private static final int MIN_HEIGHT = 400;
    private static final int CELL_SIZE = 50;
    private static final int SPACING = 2;

    private JPanel boardPanel;
    private JLabel statusLabel;
    private JLabel timeLabel;
    private JLabel attemptLabel;
    private JButton[][] cells;
    private final Color[] pieceColors;
    private final int rows;
    private final int cols;
    private JButton solveButton;
    private JButton saveButton;
    private JButton saveImageButton;
    private JButton exitButton;
    private PuzzleSolver solver;
    private boolean isSolving = false;
    private Timer progressTimer;

    public GUI(int rows, int cols, PuzzleSolver solver) {
        this.rows = rows;
        this.cols = cols;
        this.solver = solver;
        this.pieceColors = initializePieceColors();
        
        setupMainFrame();
        setupComponents();
        setupKeyboardShortcuts();
        initializeProgressTimer();
    }

    private Color[] initializePieceColors() {
        Color[] colors = new Color[26];
        float hue = 0;
        float saturation = 0.8f;
        float brightness = 0.9f;
        for (int i = 0; i < 26; i++) {
            colors[i] = Color.getHSBColor(hue, saturation, brightness);
            hue += 0.618034f;
            hue %= 1.0f;
        } 
        return colors;
    }

    private void setupMainFrame() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    }

    private void setupComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createBoardPanel(), BorderLayout.CENTER);
        mainPanel.add(createControlPanel(), BorderLayout.SOUTH);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        return titlePanel;
    }

    private JPanel createBoardPanel() {
        JPanel boardContainer = new JPanel(new BorderLayout());
        boardContainer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Puzzle Board"));
        boardPanel = new JPanel(new GridLayout(rows, cols, SPACING, SPACING));
        boardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        cells = new JButton[rows][cols];
        initializeBoardCells();
        boardContainer.add(boardPanel, BorderLayout.CENTER);
        return boardContainer;
    }

    private void initializeBoardCells() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = createCell();
                boardPanel.add(cells[i][j]);
            }
        }
    }

    private JButton createCell() {
        JButton cell = new JButton();
        cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        cell.setBackground(Color.WHITE);
        cell.setOpaque(true);
        cell.setBorderPainted(true);
        cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return cell;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Menu and Information"), new EmptyBorder(5, 5, 5, 5)));
        controlPanel.add(createStatusPanel(), BorderLayout.CENTER);
        controlPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return controlPanel;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusLabel = new JLabel("Status: Siap");
        timeLabel = new JLabel("Waktu: 0 ms");
        attemptLabel = new JLabel("Percobaan: 0");
        statusPanel.add(statusLabel);
        statusPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statusPanel.add(timeLabel);
        statusPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statusPanel.add(attemptLabel); 
        return statusPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        solveButton = new JButton("Solve");
        saveButton = new JButton("Simpan Solusi");
        saveImageButton = new JButton("Simpan Gambar");
        exitButton = new JButton("Exit");
        saveButton.setEnabled(false);
        saveImageButton.setEnabled(false);
        setupButtonActions();
        buttonPanel.add(solveButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(saveImageButton);
        buttonPanel.add(exitButton); 
        return buttonPanel;
    }

    private void setupButtonActions() {
        solveButton.addActionListener(e -> startSolving());
        saveButton.addActionListener(e -> saveSolution());
        saveImageButton.addActionListener(e -> saveAsImage());
        exitButton.addActionListener(e -> exitApplication());
    }

    private void setupKeyboardShortcuts() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Exit");
        getRootPane().getActionMap().put("Exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitApplication();
            }
        });
    }

    private void initializeProgressTimer() {
        progressTimer = new Timer(100, e -> {
            if (isSolving) {
                updateTime(System.currentTimeMillis() - solver.Start());
                updateattempt(solver.attempts());
                updateBoard(solver.Board().Symbols());
            }
        });
    }
    private void startSolving() {
        if (!isSolving) {
            isSolving = true;
            solveButton.setEnabled(false);
            statusLabel.setText("Status: Mencari solusi harap menunggu...");
            progressTimer.start();
            new Thread(() -> {
                boolean solved = solver.solve();
                SwingUtilities.invokeLater(() -> {
                    progressTimer.stop();
                    isSolving = false;
                    
                    if (solved) {
                        statusLabel.setText("Status: Yeay solusi ditemukan!");
                        saveButton.setEnabled(true);
                        saveImageButton.setEnabled(true);
                    } else {
                        statusLabel.setText("Status: Yah tidak ada solusi ditemukan");
                        solveButton.setEnabled(false);
                    }
                    
                    updateBoard(solver.Board().Symbols());
                    updateTime(solver.Time());
                    updateattempt(solver.attempts());
                });
            }).start();
        }
    }

    private void saveSolution() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.toLowerCase().endsWith(".txt")) {
                filePath += ".txt";
            } try {
                java.io.PrintWriter writer = new java.io.PrintWriter(filePath);
                writer.println("IQ PUZZLER PRO");
                writer.println("--- SOLUSI ---");
                writer.println("Waktu pencarian: " + solver.Time() + " ms");
                writer.println("Jumlah percobaan: " + solver.attempts());
                writer.println("\nKonfigurasi Board:");
                writer.println(solver.Board().toString());
                writer.close();
                
                JOptionPane.showMessageDialog(this, 
                    "YEAYYYY solusi berhasil disimpan ke " + filePath,
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (java.io.FileNotFoundException e) {
                JOptionPane.showMessageDialog(this,
                    "Error tidak dapat menyimpan file",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveAsImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.toLowerCase().endsWith(".png")) {
                filePath += ".png";
            } try {
                BufferedImage image = new BufferedImage(
                    boardPanel.getWidth(),
                    boardPanel.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2d = image.createGraphics();
                boardPanel.paint(g2d);
                g2d.dispose();
                ImageIO.write(image, "png", new File(filePath));
                JOptionPane.showMessageDialog(this,
                    "YEAYYY gambar berhasil disimpan ke " + filePath,
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error menyimpan gambar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exitApplication() {
        if (isSolving) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Proses pencarian solusi sedang berjalan. Anda yakin ingin keluar?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    public void updateBoard(char[][] boardState) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    char symbol = boardState[i][j];
                    JButton cell = cells[i][j];
                    if (symbol == '.') {
                        cell.setBackground(Color.WHITE);
                        cell.setText("");
                    } else {
                        cell.setBackground(pieceColors[symbol - 'A']);
                        cell.setText(String.valueOf(symbol));
                        cell.setForeground(getContrastColor(pieceColors[symbol - 'A']));
                    }
                }
            }
        });
    }

    private Color getContrastColor(Color background) {
        double brightness = (background.getRed() * 0.299 +
                           background.getGreen() * 0.587 +
                           background.getBlue() * 0.114) / 255;
        return brightness > 0.5 ? Color.BLACK : Color.WHITE;
    }

    public void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> statusLabel.setText("Status: " + status));
    }

    public void updateTime(long time) {
        SwingUtilities.invokeLater(() -> timeLabel.setText("Waktu: " + time + " ms"));
    }

    public void updateattempt(long attempt) {
        SwingUtilities.invokeLater(() -> attemptLabel.setText("Percobaan: " + attempt));
    }

    public static void createAndShowFileChooser() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            JLabel titleLabel = new JLabel(TITLE);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(titleLabel);
            mainPanel.add(Box.createVerticalStrut(20));
            JButton browseButton = new JButton("Pilih File Input");
            browseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(browseButton);
            frame.add(mainPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            browseButton.addActionListener(e -> handleFileSelection(frame));
        });
    }
    
    private static void handleFileSelection(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
            } 
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });
        
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getPath();
                InputFile inputSystem = new InputFile(filePath);
                Board board = new Board(inputSystem.n(), inputSystem.m());
                Piece[] pieces = new Piece[inputSystem.p()];
                for (int i = 0; i < inputSystem.p(); i++) {
                    pieces[i] = new Piece(inputSystem.pieces().get(i), 
                                              inputSystem.pieceSymbols().get(i));
                }
                PuzzleSolver solver = new PuzzleSolver(board, pieces);
                GUI puzzleGUI = new GUI(board.rows(), board.cols(), solver);
                puzzleGUI.setVisible(true);
                frame.dispose();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        } createAndShowFileChooser();
    }
}
