package main;

import GeneticComponents.Implementations.Mutators.*;
import GeneticComponents.Implementations.ParentSelectors.*;
import GeneticComponents.Implementations.ConditionCheckers.TimeConditionChecker;
import GeneticComponents.Implementations.Reproductors.*;
import GeneticComponents.Implementations.ConditionCheckers.*;
import GeneticComponents.Implementations.ParentSelectors.EliteSelector;
import GeneticComponents.Implementations.Reproductors.CrossoverManager;
import GeneticComponents.Implementations.Reproductors.OnePointCrossover;
import GeneticComponents.Interfaces.*;
import Utils.Utils;
import classes.*;
import equipment.Equipment;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FindBestCombination {

    private static ConditionChecker conditionChecker;

    private static MutatorManager mutatorManager;
    private static CrossoverManager crossoverManager;

    private static ParentSelector parentSelectorOne;
    private static ParentSelector parentSelectorTwo;
    private static double parentSelectorPercentage;

    // TODO: implement population generators
    private static PopulationGenerator populationGeneratorOne;
    private static PopulationGenerator populationGeneratorTwo;
    private static double populationGeneratorPercentage;

    private static File armasFile;
    private static File botasFile;
    private static File cascosFile;
    private static File guantesFile;
    private static File pecherasFile;

    private static int initialPopulationSize;

    // At the end of each generation, we select new individuals from the set of {Children + Parents}.
    // This variable determines how many individuals will be selected from that set each generation
    private static int populationAmountToSelect;

    // Amount of parents that will be selected to cross and generate new children each generation
    private static int parentsAmountToSelect;

    private static String classSelection;

    private static int currentGeneration;

    // Lo que esta en este main son solo cosas que estuve testeando y que luego se reemplazara
    // por la lectura del archivo de configuracion, asi que puedes ignorar esta parte.
    public static void main(String[] args) throws IOException {
        String geneticAlgConfigPath = "src/config.properties";
        final Properties properties = new Properties();
        properties.load(new FileInputStream(geneticAlgConfigPath));


        initialPopulationSize = Integer.parseInt(properties.getProperty("initialPopSize"));

        armasFile = new File("src/equipment/tsvs/" + properties.getProperty("equipmentWeapons"));
        botasFile = new File("src/equipment/tsvs/" + properties.getProperty("equipmentBoots"));
        cascosFile = new File("src/equipment/tsvs/" + properties.getProperty("equipmentHelms"));
        guantesFile = new File("src/equipment/tsvs/" + properties.getProperty("equipmentGloves"));
        pecherasFile = new File("src/equipment/tsvs/" + properties.getProperty("equipmentCuirass"));

        classSelection = properties.getProperty("class");

        currentGeneration = Integer.parseInt(properties.getProperty("startingGeneration"));

        String value = properties.getProperty("value");
        switch (properties.getProperty("cutoff")) {
            case "time":
                conditionChecker = new TimeConditionChecker(Double.parseDouble(value));
                break;
            case "gens":
                conditionChecker = new GenerationConditionChecker(Integer.parseInt(value));
                break;
            case "asol":
                conditionChecker = new AcceptableSolutionConditionChecker(Double.parseDouble(value));
                break;
            case "structure":
                conditionChecker = new StructureConditionChecker(
                        Integer.parseInt(value), Double.parseDouble(properties.getProperty("significant")));
                break;
            case "content":
                conditionChecker = new ContentConditionChecker(Integer.parseInt(value));
                break;
            default:
                throw new IllegalArgumentException("No condition checker provided, iterations would never stop!");
        }

        String selectorOne = properties.getProperty("selectorOne");
        String selectorTwo = properties.getProperty("selectorTwo");
        parentSelectorPercentage = Double.parseDouble(properties.getProperty("parentSelectorPercentage"));
        parentsAmountToSelect = Integer.parseInt(properties.getProperty("parentsAmountToSelect"));
        switch (selectorOne) {
            case "elite":
                parentSelectorOne = new EliteSelector();
                break;
            case "roulette":
                parentSelectorOne = new RouletteWheelSelector();
                break;
            case "universal":
                parentSelectorOne = new UniversalSelector();
                break;
            case "boltzmann":
                parentSelectorOne = new BoltzmannSelector(
                        Double.parseDouble(properties.getProperty("t_0")),
                        Double.parseDouble(properties.getProperty("t_c")),
                        Double.parseDouble(properties.getProperty("k"))
                );
                break;
            case "dett":
                parentSelectorOne = new DeterministicTournamentSelector(Integer.parseInt(properties.getProperty("nTourPart")));
                break;
            case "probt":
                parentSelectorOne = new ProbabilisticTournamentSelector();
                break;
            default:
                throw new IllegalArgumentException("No parent selector chosen for selector 1!");
        }
        switch (selectorTwo) {
            case "elite":
                parentSelectorTwo = new EliteSelector();
                break;
            case "roulette":
                parentSelectorTwo = new RouletteWheelSelector();
                break;
            case "universal":
                parentSelectorTwo = new UniversalSelector();
                break;
            case "boltzmann":
                parentSelectorTwo = new BoltzmannSelector(
                        Double.parseDouble(properties.getProperty("t_0")),
                        Double.parseDouble(properties.getProperty("t_c")),
                        Double.parseDouble(properties.getProperty("k"))
                );
                break;
            case "dett":
                parentSelectorTwo = new DeterministicTournamentSelector(Integer.parseInt(properties.getProperty("nTourPart")));
                break;
            case "probt":
                parentSelectorTwo = new ProbabilisticTournamentSelector();
                break;
            default:
                throw new IllegalArgumentException("No parent selector chosen for selector 2!");
        }

        switch (properties.getProperty("mutation")) {
            case "gen":
                mutatorManager = new MutatorManager(new GeneMutator(Double.parseDouble(properties.getProperty("geneMutProb"))), armasFile, botasFile, cascosFile, guantesFile, pecherasFile);
                break;
            case "limmgen":
                mutatorManager = new MutatorManager(new LimitedMultiGeneMutator(Double.parseDouble(properties.getProperty("geneMutProb"))), armasFile, botasFile, cascosFile, guantesFile, pecherasFile);
                break;
            case "unimgen":
                mutatorManager = new MutatorManager(new UniformMultiGeneMutator(Double.parseDouble(properties.getProperty("geneMutProb"))), armasFile, botasFile, cascosFile, guantesFile, pecherasFile);
                break;
            case "complete":
                mutatorManager = new MutatorManager(new CompleteMutator(Double.parseDouble(properties.getProperty("geneMutProb"))), armasFile, botasFile, cascosFile, guantesFile, pecherasFile);
                break;
            default:
                throw new IllegalArgumentException("No mutator selected!");
        }

        // test?
        try {
            List<GameClass> firstGeneration = generateInitialPopulation();
            System.out.println("First generation individuals: ");
            for (GameClass individual : firstGeneration) {
                System.out.println(individual);
            }
            List<GameClass> parents = parentSelectorOne.selectParentsFromPopulation(firstGeneration, 7);
            System.out.println("Selected parents: ");
            for (GameClass parent : parents) {
                System.out.println(parent);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        switch (properties.getProperty("cross")) {
            case "pcross":
                crossoverManager = new CrossoverManager(new OnePointCrossover());
                break;
            case "2pcross":
                crossoverManager = new CrossoverManager(new TwoPointCrossover());
                break;
            case "across":
                crossoverManager = new CrossoverManager(new AnnularCrossover());
                break;
            case "unicross":
                crossoverManager = new CrossoverManager(new UniformCrossover());
                break;
            default:
                throw new IllegalArgumentException("No crossover chosen!");
        }

        //findBestCombination();
    }

    public static int getCurrentGeneration() {
        return currentGeneration;
    }

    private static void findBestCombination() throws IOException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        List<GameClass> parents;
        List<GameClass> children;
        List<GameClass> population = generateInitialPopulation();
        conditionChecker.initialize();
        while (!conditionChecker.isConditionMet()) {
            parents = selectParents(population);
            children = crossoverManager.cross(parents);
            mutatorManager.mutate(children);
            population = selectNextGeneration(parents, children);


            // do I need to do this here? or in update?
            currentGeneration++;
            // todo: add call to conditionChecker.update(something)
        }
    }

    private static List<GameClass> selectParents(List<GameClass> population) {

        int parentsMethodOne = (int)((double)parentsAmountToSelect * parentSelectorPercentage);
        List<GameClass> selectorOneParents = parentSelectorOne.selectParentsFromPopulation(population, parentsMethodOne);

        //To avoid selecting duplicate parents using the second selection method
        population.removeAll(selectorOneParents);

        int parentsMethodTwo = (int)((double)parentsAmountToSelect * (1.0 - parentSelectorPercentage));
        List<GameClass> selectorTwoParents = parentSelectorTwo.selectParentsFromPopulation(population, parentsMethodTwo);

        List <GameClass> parents = new ArrayList<>();
        parents.addAll(selectorOneParents);
        parents.addAll(selectorTwoParents);

        return parents;
    }

    private static List<GameClass> selectNextGeneration(List<GameClass> parents, List<GameClass> children) {

        List<GameClass> newPopulationOne = populationGeneratorOne.generateFromCurrentPopulation(parents, children,
                (int) ((double)populationAmountToSelect * populationGeneratorPercentage));

        parents.removeAll(newPopulationOne);
        children.removeAll(newPopulationOne);

        List<GameClass> newPopulationTwo = populationGeneratorTwo.generateFromCurrentPopulation(parents, children,
                (int) ((double)populationAmountToSelect * (1.0 - populationGeneratorPercentage)));

        List<GameClass> population = new ArrayList<>();
        population.addAll(newPopulationOne);
        population.addAll(newPopulationTwo);

        return population;
    }

    private static List<GameClass> generateInitialPopulation() throws IOException {
        List<GameClass> initialRandomPopulation = new ArrayList<>();
        for (int i = 0; i < initialPopulationSize; i++) {
            System.out.println("Generating individual " + i);
            double randomHeight = Utils.generateRandomHeight();
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

    private static List<Equipment> generateRandomEquipment() throws IOException {
        List<Equipment> randomEquipment = new ArrayList<>();
        randomEquipment.add(Utils.selectRandomEquipmentFromFile(armasFile));
        randomEquipment.add(Utils.selectRandomEquipmentFromFile(botasFile));
        randomEquipment.add(Utils.selectRandomEquipmentFromFile(cascosFile));
        randomEquipment.add(Utils.selectRandomEquipmentFromFile(guantesFile));
        randomEquipment.add(Utils.selectRandomEquipmentFromFile(pecherasFile));
        return randomEquipment;
    }
}
