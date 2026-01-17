package principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class PathfindingApp {

    static CardLayout cardLayout = new CardLayout();
    static JPanel mainPanel = new JPanel(cardLayout);

    public static void main(String[] args) {
        JFrame frame = new JFrame("Visualizador de Algoritmos - Java Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 850);
        frame.setLocationRelativeTo(null);

        MenuPanel menu = new MenuPanel();
        InstructionsPanel instructions = new InstructionsPanel();
        GamePanel game = new GamePanel();

        mainPanel.add(menu, "MENU");
        mainPanel.add(instructions, "INSTRUCOES");
        mainPanel.add(game, "JOGO");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    static class MenuPanel extends JPanel {
        public MenuPanel() {
            setLayout(new GridBagLayout());
            setBackground(new Color(40, 44, 52));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 0, 10, 0);
            gbc.gridx = 0;
            gbc.gridy = 0;

            JLabel title = new JLabel("PATHFINDING VISUALIZER");
            title.setFont(new Font("Arial", Font.BOLD, 30));
            title.setForeground(Color.WHITE);
            add(title, gbc);

            gbc.gridy++;
            JButton playBtn = createButton("JOGAR");
            playBtn.addActionListener(e -> cardLayout.show(mainPanel, "JOGO"));
            add(playBtn, gbc);

            gbc.gridy++;
            JButton helpBtn = createButton("COMO JOGAR");
            helpBtn.addActionListener(e -> cardLayout.show(mainPanel, "INSTRUCOES"));
            add(helpBtn, gbc);
        }

        private JButton createButton(String text) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.setPreferredSize(new Dimension(200, 50));
            btn.setFocusPainted(false);
            return btn;
        }
    }

    static class InstructionsPanel extends JPanel {
        public InstructionsPanel() {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);

            JLabel title = new JLabel("COMO JOGAR", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 30));
            title.setBorder(BorderFactory.createEmptyBorder(60, 0, 20, 0));
            add(title, BorderLayout.NORTH);

            String htmlText = "<html><body style='width: 500px; padding: 10px; font-size: 16px; font-family: Arial;'>"
                    + "<h2>🎯 Seu Objetivo (O Arquiteto)</h2>"
                    + "<p>Você deve montar um cenário desafiador para o computador resolver.</p>"
                    + "<br>"
                    + "<h2>🎨 Legenda das Cores</h2>"
                    + "<ul>"
                    + "<li><font color='green'><b>VERDE:</b></font> Ponto de Partida (O Rato).</li>"
                    + "<li><font color='red'><b>VERMELHO:</b></font> Destino Final (O Queijo).</li>"
                    + "<li><b>PRETO:</b> Muros/Obstáculos (Onde não dá para passar).</li>"
                    + "<li><font color='blue'><b>AZUL:</b></font> O caminho mais curto encontrado!</li>"
                    + "</ul>"
                    + "<br>"
                    + "<h2>🎮 Controles</h2>"
                    + "<ul>"
                    + "<li><b>Arrastar (Esquerdo):</b> Move os pontos Verde/Vermelho ou desenha Muros.</li>"
                    + "<li><b>Botão Direito:</b> Apaga os Muros (Borracha).</li>"
                    + "<li><b>Botão INICIAR:</b> Manda o computador procurar o caminho.</li>"
                    + "<li><b>Botão LIMPAR:</b> Apaga o caminho azul para tentar de novo.</li>"
                    + "</ul>"
                    + "</body></html>";

            JLabel text = new JLabel(htmlText, SwingConstants.CENTER);
            add(text, BorderLayout.CENTER);

            JButton backBtn = new JButton("VOLTAR AO MENU");
            backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
            backBtn.setPreferredSize(new Dimension(200, 60));
            add(backBtn, BorderLayout.SOUTH);
        }
    }

    static class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
        private int cols = 50;
        private int rows = 50;
        private int size = 15;
        private Node[][] grid = new Node[cols][rows];
        private Node startNode;
        private Node endNode;
        private boolean running = false;
        
        private boolean draggingStart = false;
        private boolean draggingEnd = false;

        public GamePanel() {
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.addKeyListener(this);
            this.setFocusable(true);
            this.setBackground(Color.WHITE);
            this.setLayout(null);

            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    grid[i][j] = new Node(i, j);
                }
            }
            startNode = grid[5][rows / 2];
            endNode = grid[cols - 6][rows / 2];
            
            JButton menuBtn = new JButton("Voltar");
            menuBtn.setBounds(10, 10, 80, 30);
            menuBtn.addActionListener(e -> {
            	reset();
            	cardLayout.show(mainPanel, "MENU");
            });
            menuBtn.setFocusable(false);
            this.add(menuBtn);
            
            JButton startBtn = new JButton("INICIAR");
            startBtn.setBounds(100, 10, 100, 30);
            startBtn.setBackground(new Color(34, 139, 34));
            startBtn.setForeground(Color.WHITE);
            startBtn.addActionListener(e -> {
            	if(!running) solve();
            	this.requestFocusInWindow();
            });
            startBtn.setFocusable(false);
            this.add(startBtn);

            JButton resetBtn = new JButton("LIMPAR");
            resetBtn.setBounds(210, 10, 100, 30);
            resetBtn.setBackground(new Color(220, 20, 60));
            resetBtn.setForeground(Color.WHITE);
            resetBtn.addActionListener(e -> {
            	reset();
            	this.requestFocusInWindow();
            });
            resetBtn.setFocusable(false);
            this.add(resetBtn);
        }

        public void solve() {
            new Thread(() -> {
                running = true;
                PriorityQueue<Node> openSet = new PriorityQueue<>((n1, n2) -> Double.compare(n1.g, n2.g));
                startNode.g = 0;
                openSet.add(startNode);
                
                boolean pathFound = false;

                while (!openSet.isEmpty()) {
                    Node current = openSet.poll();
                    
                    if (current == endNode) {
                        pathFound = true;
                        reconstructPath();
                        break;
                    }
                    
                    current.visited = true;

                    for (Node neighbor : getNeighbors(current)) {
                        if (!neighbor.visited && !neighbor.wall) {
                            double tempG = current.g + 1;
                            if (tempG < neighbor.g) {
                                neighbor.parent = current;
                                neighbor.g = tempG;
                                if (!openSet.contains(neighbor)) {
                                    openSet.add(neighbor);
                                    neighbor.checking = true;
                                }
                            }
                        }
                    }
                    try { Thread.sleep(3); } catch (Exception e) {}
                    repaint();
                }
                
                if (!pathFound) {
                	SwingUtilities.invokeLater(() -> {
                        Object[] options = {"Manter Mapa", "Reiniciar"};
                        int choice = JOptionPane.showOptionDialog(this,
                                "IMPOSSÍVEL!\nNão há caminho livre até o destino.",
                                "Sem Saída",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE,
                                null,
                                options,
                                options[0]);
                        
                        if (choice == 1) reset();
                    });
                }
                
                running = false;
            }).start();
        }

        private void reconstructPath() {
            Node current = endNode;
            int nodes = 0;
            
            while (current != null) {
            	nodes++;
                current.path = true;
                current = current.parent;
                repaint();
                try { Thread.sleep(10); } catch (Exception e) {}
            }
            
            int steps = nodes > 0 ? nodes - 1 : 0;
            
            SwingUtilities.invokeLater(() -> {
                Object[] options = {"Manter Mapa", "Reiniciar"};
                int choice = JOptionPane.showOptionDialog(this,
                        "SUCESSO!\nCaminho encontrado em " + steps + " passos.",
                        "Vitória",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
                
                if (choice == 1) reset();
            });
        }

        private ArrayList<Node> getNeighbors(Node node) {
            ArrayList<Node> neighbors = new ArrayList<>();
            int x = node.col;
            int y = node.row;
            if (x > 0) neighbors.add(grid[x - 1][y]);
            if (x < cols - 1) neighbors.add(grid[x + 1][y]);
            if (y > 0) neighbors.add(grid[x][y - 1]);
            if (y < rows - 1) neighbors.add(grid[x][y + 1]);
            return neighbors;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    Node n = grid[i][j];
                    if (n == startNode) g.setColor(Color.GREEN);
                    else if (n == endNode) g.setColor(Color.RED);
                    else if (n.wall) g.setColor(Color.BLACK);
                    else if (n.path) g.setColor(Color.BLUE);
                    else if (n.visited) g.setColor(new Color(175, 238, 238));
                    else if (n.checking) g.setColor(Color.YELLOW);
                    else g.setColor(Color.WHITE);
                    g.fillRect(i * size, j * size, size, size);
                    g.setColor(new Color(230, 230, 230));
                    g.drawRect(i * size, j * size, size, size);
                }
            }
        }

        public void mousePressed(MouseEvent e) {
        	this.requestFocusInWindow();
            if (running) return;
            int x = e.getX() / size;
            int y = e.getY() / size;
            
            if (isValid(x, y)) {
                Node clicked = grid[x][y];
                if (clicked == startNode) draggingStart = true;
                else if (clicked == endNode) draggingEnd = true;
                else mouseDragged(e);
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (running) return;
            int x = e.getX() / size;
            int y = e.getY() / size;

            if (isValid(x, y)) {
                Node n = grid[x][y];
                
                if (draggingStart) {
                    if (n != endNode && !n.wall) startNode = n;
                } 
                else if (draggingEnd) {
                    if (n != startNode && !n.wall) endNode = n;
                } 
                else {
                    if (n != startNode && n != endNode) {
                        if (SwingUtilities.isLeftMouseButton(e)) n.wall = true;
                        if (SwingUtilities.isRightMouseButton(e)) n.wall = false;
                    }
                }
                repaint();
            }
        }

        public void mouseReleased(MouseEvent e) {
            draggingStart = false;
            draggingEnd = false;
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER && !running) solve();
            if (e.getKeyCode() == KeyEvent.VK_R) reset();
        }
        
        private boolean isValid(int x, int y) {
            return x >= 0 && x < cols && y >= 0 && y < rows;
        }

        private void reset() {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    grid[i][j].reset();
                }
            }
            repaint();
        }

        public void mouseMoved(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void keyTyped(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
    }

    static class Node {
        int col, row;
        double g = Double.POSITIVE_INFINITY;
        boolean wall = false, visited = false, checking = false, path = false;
        Node parent = null;
        public Node(int col, int row) { this.col = col; this.row = row; }
        public void reset() {
            wall = false; visited = false; checking = false; path = false;
            g = Double.POSITIVE_INFINITY; parent = null;
        }
    }
}