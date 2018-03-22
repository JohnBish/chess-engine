package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class GUI extends JFrame {
    //Declare constants
    private static final int WIDTH = 720;
    private static final int HEIGHT = 840;
    private static final int BORDER_WIDTH = 100;

    //Declare variables
    private Container window;
    private OpenPanel animationPanel;
    private BackgroundImage backgroundPanel;
    private JPanel inputPanel;
    private JPanel buttonGroupPanel;
    private JLabel titleLabel;
    public static JTextArea promptLabel;
    public static JLabel outputLabel;
    private JTextPane helpPane;
    private JTextField inputField;
    private JButton moveButton;
    public static JButton playButton;
    private JButton helpButton;
    private JScrollPane console;

    GUI() {
        initGUI();
    }

    private void initGUI() {
        /*
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(UnsupportedLookAndFeelException |
                InstantiationException |
                IllegalAccessException |
                ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        //Initializes all swing components
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        window = getContentPane();
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("src/com/company/assets/background.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }

        //Main JPanel with background image
        backgroundPanel = new BackgroundImage(backgroundImage);
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.PAGE_AXIS));
        window.add(backgroundPanel);
        //Background image was generated with trianglify by qrohlf:
        //https://github.com/qrohlf/trianglify

        titleLabel = new JLabel("<html><br>CHESS<br><br>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundPanel.add(titleLabel);

        //Group of Play and Help buttons
        buttonGroupPanel = new JPanel();
        buttonGroupPanel.setLayout(new FlowLayout());
        buttonGroupPanel.setOpaque(false);
        backgroundPanel.add(buttonGroupPanel);

        playButton = new JButton("PLAY");
        playButton.setFont(new Font("Arial", Font.PLAIN, 25));
        playButton.setBackground(Color.decode("#000080"));
        playButton.setForeground(Color.WHITE);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setBorderPainted(false);
        playButton.setFocusPainted(false);
        //playButton.setContentAreaFilled(false);
        playButton.addActionListener(e -> {
            playButton.setVisible(false);
            helpButton.setVisible(false);
            helpPane.setVisible(false);
            outputLabel.setVisible(false);
            animationPanel.start(outputLabel);
        });
        buttonGroupPanel.add(playButton);

        helpPane = new JTextPane();
        helpPane.setContentType("text/html");
        helpPane.setEditable(false);
        helpPane.setText("<html>Chess is a popular game that originated<br>" +
                "in India around the 6th century. The game<br>" +
                "spread to Europe and Russia by 1000 ce.<br>" +
                "It is a two-player board game where each player<br>" +
                "commands a legion of pieces and attempts to<br>" +
                "defeat the other in battle. The rules can be<br>" +
                "found at https://www.chess.com/learn-how-to-play-chess.<br>" +
                "In this application, you'll need to input moves<br>" +
                "using algebraic notation. You can find out how<br>" +
                "to use algebraic notation at<br>" +
                "https://en.wikipedia.org/wiki/Algebraic_notation_(chess).<br><br>" +
                "This game is in ALPHA. Pawn promotion is not<br>" +
                "yet implemented." +
                "<br><br><br><br><br><br><br><br><br><br><br><br>");
        //Centered text in JTextPane from
        //https://stackoverflow.com/questions/3213045/centering-text-in-a-jtextarea-or-jtextpane-horizontal-text-alignment
        StyledDocument d = helpPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        d.setParagraphAttributes(0, d.getLength(), center, false);
        helpPane.setBorder(null);
        helpPane.setVisible(false);
        backgroundPanel.add(helpPane);

        helpButton = new JButton("HELP");
        helpButton.setFont(new Font("Arial", Font.PLAIN, 25));
        helpButton.setBackground(Color.decode("#555555"));
        helpButton.setForeground(Color.WHITE);
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        helpButton.setBorderPainted(false);
        helpButton.setFocusPainted(false);
        //helpButton.setContentAreaFilled(false);
        helpButton.addActionListener(e -> {
            helpButton.setVisible(false);
            helpPane.setVisible(true);
        });
        buttonGroupPanel.add(helpButton);

        //Panel to do opening animation and parent to chessboard
        animationPanel = new OpenPanel();
        animationPanel.setLayout(new BorderLayout());
        backgroundPanel.add(animationPanel);

        outputLabel = new JLabel("", SwingConstants.CENTER);
        outputLabel.setFont(new Font("Monospaced", Font.PLAIN, 30));
        outputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        animationPanel.add(outputLabel);

        //Panel to get input and trigger re-rendering
        inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new FlowLayout());
        inputPanel.setVisible(false);
        backgroundPanel.add(inputPanel);

        promptLabel = new JTextArea(11, 4);
        promptLabel.setFont(new Font("Monospaced", Font.PLAIN, 18));
        DefaultCaret caret = (DefaultCaret)promptLabel.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        inputField = new JTextField("", 6);
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 18));
        inputPanel.add(inputField);

        moveButton = new JButton("MOVE");
        moveButton.setFont(new Font("Arial", Font.PLAIN, 20));
        moveButton.setBackground(Color.decode("#000080"));
        moveButton.setForeground(Color.WHITE);
        moveButton.setBorderPainted(false);
        moveButton.setFocusPainted(false);
        //moveButton.setContentAreaFilled(false);
        moveButton.addActionListener(e -> {
            Chess.move(inputField.getText());
            inputField.setText("");
        });
        inputPanel.add(moveButton);

        //Shows log of events
        console = new JScrollPane(promptLabel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        console.setVisible(false);
        backgroundPanel.add(console);

        setVisible(true);
        pack();
    }

    //Displays background image
    private class BackgroundImage extends JPanel {
        private Image backgroundImage;

        BackgroundImage(Image i) {
            setPreferredSize(new Dimension(GUI.WIDTH, GUI.HEIGHT));
            this.backgroundImage = i;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            //Draws background image
            g.drawImage(backgroundImage, 0, 0, this);
        }
    }

    //Plays opening animation
    private class OpenPanel extends JPanel{

        private int height;
        private int width;

        OpenPanel() {
            //Initializes panel
            setPreferredSize(new Dimension(GUI.WIDTH,GUI.WIDTH));
            setOpaque(false);
            setBackground(Color.BLACK);
        }

        private void start(JLabel l) {
            height = 1;
            width = 0;
            Timer timer = new Timer(1, new TimerListener()); //Starts timer to increment box height
            timer.start();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g); //Erases old pixels
            if (width < (GUI.WIDTH - GUI.BORDER_WIDTH) / 2) {
                width ++;
            }
            else if(height < (GUI.WIDTH - GUI.BORDER_WIDTH) / 2) {
                height ++; //Increments height of box
            }
            Color c = (Color.WHITE); //Sets colour to white
            g.setColor(c);
            //Fills rectangle
            g.fillRect(GUI.WIDTH / 2 - width, GUI.WIDTH / 2 - height, 2*width, 2*height);
        }

        private class TimerListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Stops timer when box is max height
                if (height >= (GUI.WIDTH - GUI.BORDER_WIDTH) / 2) {
                    ((Timer)e.getSource()).stop();
                    startGame();
                }
                repaint();
            }

            private void startGame() {
                inputPanel.setVisible(true);
                console.setVisible(true);
                //Initializes game
                Chess.board = Chess.initBoard();
                Chess.renderBoard(Chess.turn, outputLabel);
                outputLabel.setVisible(true);
                playButton.setText("PLAY AGAIN");
                promptLabel.append(Chess.COLOURS.get(Chess.turn) + "'s turn:" + "\n");
                getRootPane().setDefaultButton(moveButton);
                inputField.requestFocusInWindow();
            }
        }
    }
}