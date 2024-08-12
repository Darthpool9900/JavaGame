import PlayerSound.SoundPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class City {
    private int[] xPoints = {0, 100, 200, 250, 300, 350, 400, 450, 500, 550, 600, 800};
    private int[] yPoints = {600, 500, 550, 450, 500, 400, 500, 450, 550, 500, 600, 600};
    private List<Building> buildings;
    private boolean isDay;
    private Component gameComponent;

    private class Building {
        int x, y, width, height;
        boolean destroyed;

        Building(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.destroyed = false;
        }

        Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }

    public City(Component gameComponent) {
        this.gameComponent = gameComponent;
        initializeBuildings();
        isDay = true;
    }

    private void initializeBuildings() {
        int minBuildingHeight = 100;
        int maxBuildingHeight = 200;
        int buildingWidth = 50;
        int panelHeight = 600;
        int minSpacing = 10;
        buildings = new ArrayList<>();
        Random rand = new Random();

        int currentX = 0;

        for (int i = 0; i < xPoints.length - 1; i++) {
            int nextX = xPoints[i + 1];
            int width = Math.min(nextX - currentX, buildingWidth);
            int height = minBuildingHeight + rand.nextInt(maxBuildingHeight - minBuildingHeight);
            int y = panelHeight - height;
            buildings.add(new Building(currentX, y, width, height));
            currentX += width + minSpacing;
        }
    }

    public void draw(Graphics2D g2d) {
        for (Building building : buildings) {
            if (!building.destroyed) {
                g2d.setColor(Color.GRAY);
                g2d.fillRect(building.x, building.y, building.width, building.height);

                g2d.setColor(isDay ? Color.BLACK : Color.YELLOW);
                int windowSize = 10;
                int windowSpacing = 5;

                for (int winX = building.x + 5; winX < building.x + building.width; winX += windowSize + windowSpacing) {
                    for (int winY = building.y + 5; winY < building.y + building.height; winY += windowSize + windowSpacing) {
                        if (winY + windowSize <= building.y + building.height) {
                            g2d.fillRect(winX, winY, windowSize, windowSize);
                        }
                    }
                }
            }
        }
    }

    public List<Projectile> checkCollisions(List<Projectile> projectiles, MainRender game) {
        List<Projectile> toRemove = new ArrayList<>();
        SoundPlayer explosion = SoundPlayer.getInstance("16-bit-explosion_120bpm_C_major.wav", false);
        for (Projectile projectile : projectiles) {
            Rectangle projectileBounds = projectile.getBounds();
            for (Building building : buildings) {
                if (!building.destroyed && projectileBounds.intersects(building.getBounds())) {
                    explosion.play();
                    building.destroyed = true;
                    toRemove.add(projectile);
                    gameComponent.repaint();
                    game.updateScore(10);
                    break;
                }
            }
        }

        if (buildings.stream().allMatch(b -> b.destroyed)) {
            initializeBuildings();
        }

        return toRemove;
    }

    public void toggleDayNight() {
        isDay = !isDay;
    }
}
