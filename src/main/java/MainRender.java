import CloudsMechanicsSystem.CloudManager;
import PlayerSound.SoundPlayer;
import java.util.List;  // Certifique-se de importar a interface genérica List do pacote java.util
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class MainRender extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Airplane airplane;
    private City city;
    private CloudManager managerClouds;
    private int score;
    private int dayNightCycle;
    private boolean isDay;

    public MainRender() {
        setPreferredSize(new Dimension(800, 600));
        isDay = true;
        airplane = new Airplane(100, 50);
        city = new City(this);
        managerClouds = new CloudManager(7);
        score = 0;
        dayNightCycle = 0;

        timer = new Timer(30, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        setBackground(isDay ? Color.CYAN : Color.BLACK);

        city.draw(g2d);
        airplane.draw(g2d, isDay);
        managerClouds.moveAndDrawClouds(g2d, isDay);
        drawProjectiles(g2d);
        drawScore(g2d);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        airplane.updateProjectiles();
        List<Projectile> projectilesToRemove = city.checkCollisions(airplane.getProjectiles(), this);

        airplane.getProjectiles().removeAll(projectilesToRemove);

        dayNightCycle++;
        if (dayNightCycle % 500 == 0) {
            city.toggleDayNight();
            isDay = !isDay;
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        airplane.handleKeyPress(e.getKeyCode(), getWidth());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        airplane.handleKeyRelease();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    private void drawProjectiles(Graphics2D g2d) {
        for (Projectile projectile : airplane.getProjectiles()) {
            projectile.draw(g2d);
        }
    }

    private void drawScore(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Score: " + score, 10, 20);
    }

    public void updateScore(int increment) {
        score += increment;
    }

    public static void main(String[] args) {
        SoundPlayer backgroundMusic = SoundPlayer.getInstance("flat-8-bit-gaming-music-instrumental-211547.wav", true);
        backgroundMusic.play();

        JFrame frame = new JFrame("City Game");
        MainRender game = new MainRender();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
