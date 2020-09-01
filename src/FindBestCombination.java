import GeneticComponents.Interfaces.*;
import classes.*;
import equipment.Equipment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FindBestCombination {

    private static ConditionChecker conditionChecker;
    private static Mutator mutator;
    private static ParentSelector parentSelector;
    private static PopulationGenerator populationGenerator;
    private static Reproductor reproductor;

    private static File armasFile;
    private static File botasFile;
    private static File cascosFile;
    private static File guantesFile;
    private static File pecherasFile;

    private static int initialPopulationSize;
    private static String classSelection;

    public static void main(String[] args) throws IOException {
        initialPopulationSize = Integer.parseInt(args[1]);
        armasFile = new File(args[2]);
        botasFile = new File(args[3]);
        cascosFile = new File(args[4]);
        guantesFile = new File(args[5]);
        pecherasFile = new File(args[6]);
        classSelection = args[7];
        findBestCombination();
    }

    private static void findBestCombination() throws IOException {
        List<GameClass> parents;
        List<GameClass> children;
        List<GameClass> population = generateInitialPopulation();
        while (!conditionChecker.isConditionMet()) {
            parents = parentSelector.selectParentsFromPopulation(population);
            children = reproductor.cross(parents);
            mutator.mutate(children);
            population = populationGenerator.generateFromCurrentPopulation(parents, children);
        }
    }

    private static List<GameClass> generateInitialPopulation() throws IOException {
        List<GameClass> initialRandomPopulation = new ArrayList<>();
        for (int i = 0; i < initialPopulationSize; i++) {
            double randomHeight = generateRandomHeight();
            List<Equipment> randomEquipment = generateRandomEquipment();
            GameClass randomIndividual = createCharacterClassSelection(randomHeight, randomEquipment);
            initialRandomPopulation.add(randomIndividual);
        }

        return initialRandomPopulation;
    }

    private static GameClass createCharacterClassSelection(double height, List<Equipment> equipment) {
        GameClass characterToCreate = null;
        switch (classSelection) {
            case "archer":
                characterToCreate = new Archer(height, equipment);
                break;
            case "defender":
                characterToCreate = new Defender(height, equipment);
                break;
            case "infiltrator":
                characterToCreate = new Infiltrator(height, equipment);
                break;
            case "warrior":
                characterToCreate = new Warrior(height, equipment);
                break;
        }
        return characterToCreate;
    }

    private static double generateRandomHeight() {
        return getRandomNumber(1.3, 2.0);
    }

    private static List<Equipment> generateRandomEquipment() throws IOException {
        List<Equipment> randomEquipment = new ArrayList<>();
        randomEquipment.add(selectRandomEquipmentFromFile(armasFile));
        randomEquipment.add(selectRandomEquipmentFromFile(botasFile));
        randomEquipment.add(selectRandomEquipmentFromFile(cascosFile));
        randomEquipment.add(selectRandomEquipmentFromFile(guantesFile));
        randomEquipment.add(selectRandomEquipmentFromFile(pecherasFile));
        return randomEquipment;
    }

    private static Equipment selectRandomEquipmentFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine(); // Consumes first line that contains the header
        String line;
        String[] itemAttributes;
        int randomItemId = (int) getRandomNumber(0, 999999);
        while ((line = br.readLine()) != null) {
            itemAttributes = line.split("\t");
            if (Integer.parseInt(itemAttributes[0]) == randomItemId) {
                double strength = Double.parseDouble(itemAttributes[1]);
                double agility = Double.parseDouble(itemAttributes[2]);
                double expertise = Double.parseDouble(itemAttributes[3]);
                double resistance = Double.parseDouble(itemAttributes[4]);
                double health = Double.parseDouble(itemAttributes[5]);
                return new Equipment(strength, agility, expertise, resistance, health);
            }
        }
        return null;
    }

    private static double getRandomNumber(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }
}
