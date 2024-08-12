package CloudsMechanicsSystem;
import CloudsMechanicsSystem.Cloud;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class CloudPool {
    private Queue<Cloud> pool;
    private Random random;

    public CloudPool(int initialSize) {
        pool = new LinkedList<>();
        random = new Random();
        // Inicializa o pool com nuvens
        for (int i = 0; i < initialSize; i++) {
            pool.add(new Cloud());
        }
    }

    public Cloud acquireCloud() {
        if (pool.isEmpty()) {
            return new Cloud(); // Cria uma nova nuvem se o pool estiver vazio
        } else {
            return pool.poll(); // Retorna uma nuvem do pool
        }
    }

    public void releaseCloud(Cloud cloud, int x, int y) {
        cloud.init(x, y); // Reposiciona a nuvem
        pool.add(cloud); // Adiciona a nuvem de volta ao pool
    }
}
