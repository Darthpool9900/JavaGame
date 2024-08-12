import java.awt.*;

public class Projectile {
    private int x, y;
    private final int width = 5, height = 10;
    private final int speed = 5;

    public Projectile(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void move() {
        y += speed;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.fill(getBounds());
    }
}
