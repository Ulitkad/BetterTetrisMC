package space.ulitka.bettertetrismc.game.tetris;

public class Controls {
    public static boolean clockwiseRotate, counterclockwiseRotate, softDropMove, leftMove, rightMove, hardDropMove, uturnRotate;

    public static void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case 68 -> rightMove = true;   // D
            case 65 -> leftMove = true;    // A
            case 83 -> softDropMove = true; // S
            case 262 -> clockwiseRotate = true; // Right arrow
            case 265 -> uturnRotate = true;     // Up arrow
            case 263 -> counterclockwiseRotate = true; // Left arrow
            case 32 -> hardDropMove = true; // Space
        }
    }

    public static void pause() {
        clockwiseRotate = counterclockwiseRotate = softDropMove = leftMove = rightMove = hardDropMove = uturnRotate = false;
    }
}
