import PlayerSound.SoundPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class Airplane {
    private int x, y;
    private int width = 50, height = 20;
    private double angle;
    private List<Projectile> projectiles;

    public Airplane(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.angle = 0;
        this.projectiles = new ArrayList<>();
    }

    public void moveLeft() {
        angle = Math.toRadians(-15);
        x = Math.max(x - 10, 0);
    }

    public void moveRight(int screenWidth) {
        angle = Math.toRadians(15);
        x = Math.min(x + 10, screenWidth - width);
    }

    public void resetAngle() {
        angle = 0;
    }

    public void draw(Graphics2D g2d, boolean isDay) {
        AffineTransform old = g2d.getTransform();
        g2d.translate(x + width / 2, y + height / 2);
        g2d.rotate(angle);
        g2d.translate(-width / 2, -height / 2);

        // Muda a cor do avião de acordo com o ciclo de dia/noite
        g2d.setColor(isDay ? Color.BLACK : Color.YELLOW);
        g2d.fillRect(0, 0, width, height);
        g2d.fillRect(15, -10, 20, 10);
        g2d.fillRect(10, 20, 30, 10);

        g2d.setTransform(old);

        for (Projectile projectile : projectiles) {
            projectile.draw(g2d);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void handleKeyPress(int keyCode, int screenWidth) {
        if (keyCode == KeyEvent.VK_LEFT) {
            moveLeft();
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            moveRight(screenWidth);
        } else if (keyCode == KeyEvent.VK_SPACE) {
            shoot();
        }
    }

    public void handleKeyRelease() {
        resetAngle();
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void shoot() {
        SoundPlayer ShootSound = new SoundPlayer("8-bit-kit-lazer-5.wav");
        ShootSound.play();
        projectiles.add(new Projectile(x + width / 2 - 2, y + height / 2 - 10));
    }

    public void updateProjectiles() {
        List<Projectile> toRemove = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            projectile.move();
            if (projectile.getBounds().y > 600) {
                toRemove.add(projectile);
            }
        }
        projectiles.removeAll(toRemove);
    }
}
