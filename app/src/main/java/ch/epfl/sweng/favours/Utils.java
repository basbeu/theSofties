package ch.epfl.sweng.favours;

public class Utils {

    public static boolean containsChar(String s){
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsDigit(String s){
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
}
