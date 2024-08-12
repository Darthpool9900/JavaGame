package CloudsMechanicsSystem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CloudManager {
    private List<Cloud> clouds;
    private CloudPool cloudPool;

    public CloudManager(int initialPoolSize) {
        clouds = new ArrayList<>();
        cloudPool = new CloudPool(initialPoolSize);

        // Adiciona algumas nuvens iniciais ao jogo
        for (int i = 0; i < 5; i++) {
            addNewCloud();
        }
    }

    public void addNewCloud() {
        Cloud cloud = cloudPool.acquireCloud();
        cloud.init(800 + (int)(Math.random() * 400), (int)(Math.random() * 200));
        clouds.add(cloud);
    }

    public void moveAndDrawClouds(Graphics2D g2d, boolean isDay) {
        List<Cloud> cloudsToRemove = new ArrayList<>();

        for (Cloud cloud : clouds) {
            cloud.move();
            cloud.draw(g2d, isDay);

            // Se a nuvem saiu da tela, prepare para removê-la e adicionar uma nova
            if (cloud.getBounds().x + cloud.getBounds().width < 0) {
                cloudsToRemove.add(cloud);
            }
        }

        // Remove nuvens que saíram da tela e as retorna ao pool
        for (Cloud cloud : cloudsToRemove) {
            clouds.remove(cloud);
            cloudPool.releaseCloud(cloud, 800, (int)(Math.random() * 200));
            addNewCloud(); // Adiciona uma nova nuvem
        }
    }
}
