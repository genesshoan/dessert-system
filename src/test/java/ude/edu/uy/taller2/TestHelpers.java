package ude.edu.uy.taller2;

/**
 * Helpers reutilizables para pruebas manuales en src/test.
 * Contiene utilidades de impresión para dejar las salidas más legibles.
 */
public final class TestHelpers {
    public static void printHeader(String title) {
        System.out.println();
        System.out.println("==================== " + title + " ====================");
    }

    public static void printSub(String text) {
        System.out.println("-- " + text);
    }
}

