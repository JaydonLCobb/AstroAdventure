import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Main extends JFrame {
    public static int fWidth;
    public static int fHeight;
    public static int fCenterX;
    public static int fCenterY;
    public static Point p = null;
    public static int mouseX = 0;
    public static int mouseY= 0;

    public static boolean pause = false;
    Main() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        fWidth = screenSize.width - 40;
        fHeight = screenSize.height - 40;

        fCenterX = fWidth/2;
        fCenterY = fHeight/2;

        Frame f = this;
        Timer timer = new Timer(66, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(p, f);
                mouseX = (int)p.getX()+7;
                mouseY = (int)p.getY()-16;
            }
        });
        timer.start();

        Image image = ImageLoader.load("cursor.png");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor c = toolkit.createCustomCursor(image , new Point(mouseX, mouseY), "img");
        this.setCursor (c);



        Game g = new Game();
        add(g);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        setSize(fWidth,fHeight);
        setTitle("Bird's Eye");
        setVisible(true);
    }
    public static void main(String[] args) {
        Main m = new Main();

        m.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                    Player.range = true;
                if (SwingUtilities.isRightMouseButton(e))
                    Player.melee = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                    Player.range = false;
                if (SwingUtilities.isRightMouseButton(e))
                    Player.melee = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pause = false;
            }

            @Override
            public void mouseExited(MouseEvent e){
                System.out.println("PAUSED");
                pause = true;
            }
        });

        m.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    Player.goLeft = true;
                if (e.getKeyCode() == KeyEvent.VK_D)
                    Player.goRight = true;
                if (e.getKeyCode() == KeyEvent.VK_W)
                    Player.goUp = true;
                if (e.getKeyCode() == KeyEvent.VK_S)
                    Player.goDown = true;
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    Player.goLeft = false;
                if (e.getKeyCode() == KeyEvent.VK_D)
                    Player.goRight = false;
                if (e.getKeyCode() == KeyEvent.VK_W)
                    Player.goUp = false;
                if (e.getKeyCode() == KeyEvent.VK_S)
                    Player.goDown = false;
            }

            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'r') {
                    System.out.println("reload");
                    Game.reloading = true;
                }
            }

        });

    }
}
