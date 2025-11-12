package space.ulitka.bettertetrismc.game.tetris.mino;

import space.ulitka.bettertetrismc.BetterTetrisMC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomMino {
    public static final java.util.Random RANDOM = new java.util.Random();


    private static final Class<? extends Mino>[] minoList = new Class[] {
            Mino_I.class,
            Mino_J.class,
            Mino_L.class,
            Mino_O.class,
            Mino_S.class,
            Mino_T.class,
            Mino_Z.class
    };

    private static List<Class<? extends Mino>> currentList = new ArrayList<>();

    public static Mino pickMino() {
        try {
            if (currentList.isEmpty()) {
                currentList = new ArrayList<>(List.of(minoList));
                Collections.shuffle(currentList, RANDOM);
            }

            Class<? extends Mino> next = currentList.removeFirst();
            return next.getDeclaredConstructor().newInstance();

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void resetMinoList() {
        currentList = new ArrayList<>(List.of(minoList));
        Collections.shuffle(currentList, RANDOM);
    }
}