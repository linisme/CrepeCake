package net.idik.crepecake

class Utils {

    static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase(Locale.US) + str.substring(1)
    }
}