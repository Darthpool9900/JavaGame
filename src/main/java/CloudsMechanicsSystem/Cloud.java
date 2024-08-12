package CloudsMechanicsSystem;

import java.awt.*;

public class Cloud {
    private int x, y;
    private int width, height;
    private int speed;

    public Cloud() {
        this.width = 100;  // Largura padrão das nuvens
        this.height = 60;  // Altura padrão das nuvens
        this.speed = 1;    // Velocidade de movimento das nuvens
    }

    public void init(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        x -= speed;
        if (x + width < 0) {
            // Reposiciona a nuvem para o lado direito quando ela sai da tela
            x = 800; // Largura da tela
            y = (int) (Math.random() * 200); // Nova altura aleatória
        }
    }

    public void draw(Graphics2D g2d, boolean isDay) {
        g2d.setColor(isDay ? Color.WHITE : Color.DARK_GRAY);
        g2d.fillOval(x, y, width, height);  // Corpo principal da nuvem

        // Desenha "pufos" adicionais para a nuvem
        g2d.fillOval(x + 20, y - 20, width - 30, height - 30);
        g2d.fillOval(x - 20, y - 10, width - 20, height - 20);
        g2d.fillOval(x + 40, y + 10, width - 20, height - 20);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
