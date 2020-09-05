package GeneticComponents.Implementations.Mutators;

import GeneticComponents.Interfaces.Mutator;
import Utils.Utils;
import classes.GameClass;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MutatorManager {

    private static MutatorManager instance;

    private final Mutator mutator;

    private final File armasFile;
    private final File botasFile;
    private final File cascosFile;
    private final File guantesFile;
    private final File pecherasFile;

    public MutatorManager(Mutator mutator, File armasFile, File botasFile, File cascoFile, File guanterasFile,
                          File pecherasFile) {
        this.mutator = mutator;
        this.armasFile = armasFile;
        this.botasFile = botasFile;
        this.cascosFile = cascoFile;
        this.guantesFile = guanterasFile;
        this.pecherasFile = pecherasFile;
    }

    public void mutate(List<GameClass> populationToMutate) throws IOException {
        for (GameClass individual : populationToMutate) {
            this.mutator.mutate(individual, this);
        }
    }

    public void mutateGene(GameClass individual, int genePosition) throws IOException {
        switch (genePosition) {
            case 0:
                individual.setHeight(Utils.generateRandomHeight());
                break;
            case 1:
                individual.getEquipment().set(genePosition-1, Utils.selectRandomEquipmentFromFile(armasFile));
                break;
            case 2:
                individual.getEquipment().set(genePosition-1, Utils.selectRandomEquipmentFromFile(botasFile));
                break;
            case 3:
                individual.getEquipment().set(genePosition-1, Utils.selectRandomEquipmentFromFile(cascosFile));
                break;
            case 4:
                individual.getEquipment().set(genePosition-1, Utils.selectRandomEquipmentFromFile(guantesFile));
                break;
            case 5:
                individual.getEquipment().set(genePosition-1, Utils.selectRandomEquipmentFromFile(pecherasFile));
                break;
        }
    }
}
