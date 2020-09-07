package Utils;

import equipment.Equipment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

    public static double getRandomInRange(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public static double generateRandomHeight() {
        return getRandomInRange(1.3, 2.0);
    }

    public static Equipment selectRandomEquipmentFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine(); // Consumes first line that contains the header
        String line;
        String[] itemAttributes;
        int randomItemId = (int) Math.ceil(Utils.getRandomInRange(0, 999999));
        while ((line = br.readLine()) != null) {
            itemAttributes = line.split("\t");
            if (Integer.parseInt(itemAttributes[0]) == randomItemId) {
                int id = Integer.parseInt(itemAttributes[0]);
                double strength = Double.parseDouble(itemAttributes[1]);
                double agility = Double.parseDouble(itemAttributes[2]);
                double expertise = Double.parseDouble(itemAttributes[3]);
                double resistance = Double.parseDouble(itemAttributes[4]);
                double health = Double.parseDouble(itemAttributes[5]);
                br.close();
                return new Equipment(strength, agility, expertise, resistance, health, id);
            }
        }
        br.close();
        return null;
    }
}
