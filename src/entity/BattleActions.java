package entity;

/**
 * Interface untuk mendefinisikan aksi dasar dalam pertarungan.
 */
public interface BattleActions {
    String attack(Entity target);

    String skill(Entity target);

    String ultimate(Entity target);
}
