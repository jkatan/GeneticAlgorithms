package Utils;

public class Utils {

    public static double getRandomInRange(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }
}
