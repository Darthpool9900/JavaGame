import PlayerSound.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainRender extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Airplane airplane;
    private City city;
    private List<Projectile> projectiles;
    private List<Cloud> clouds;
    private int score;
    private int dayNightCycle; // Ciclo para alternar dia e noite
    private boolean isDay; // Variável para rastrear o ciclo de dia/noite

    public MainRender() {
        this.setPreferredSize(new Dimension(800, 600));
        this.isDay = true; // Começa com o dia
        this.airplane = new Airplane(100, 50);
        this.city = new City(this);
        this.projectiles = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.score = 0;
        this.dayNightCycle = 0;

        // Adiciona algumas nuvens
        for (int i = 0; i < 5; i++) {
            clouds.add(new Cloud((int) (Math.random() * 800), (int) (Math.random() * 200)));
        }

        this.timer = new Timer(20, this);
        this.timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Define a cor de fundo com base no ciclo dia/noite
        this.setBackground(isDay ? Color.CYAN : Color.BLACK);

        // Atualiza e desenha as nuvens
        for (Cloud cloud : clouds) {
            cloud.move();
            cloud.draw(g2d,isDay);
        }

        city.draw(g2d);
        airplane.draw(g2d, isDay); // Passa a variável isDay para o avião mudar de cor
        drawProjectiles(g2d);
        drawScore(g2d);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Atualiza o movimento dos projéteis e checa colisões
        airplane.updateProjectiles();
        List<Projectile> projectilesToRemove = city.checkCollisions(airplane.getProjectiles(), this);

        // Remove os projéteis que saíram da tela
        List<Projectile> toRemove = new ArrayList<>();
        for (Projectile projectile : airplane.getProjectiles()) {
            if (projectile.getBounds().y <= 0 || projectilesToRemove.contains(projectile)) {
                toRemove.add(projectile);
            }
        }
        airplane.getProjectiles().removeAll(toRemove);

        // Alterna entre dia e noite a cada 10 segundos
        dayNightCycle++;
        if (dayNightCycle % 500 == 0) {
            city.toggleDayNight();
            isDay = !isDay; // Alterna entre dia e noite
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

    // Método para atualizar a pontuação
    public void updateScore(int increment) {
        score += increment;
    }

    public static void main(String[] args) {
        SoundPlayer BackgroundMusic = new SoundPlayer("flat-8-bit-gaming-music-instrumental-211547.wav");
        BackgroundMusic.SetRepeatSound(true);
        BackgroundMusic.play();
        JFrame frame = new JFrame("City Game");
        MainRender game = new MainRender();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
