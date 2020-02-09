package visualization;

import geneticalgorithms.AlgorithmType;
import geneticalgorithms.DominanceType;
import individuals.Individual;
import parentselectors.ParentSelector;
import parentselectors.TypeSelectionParents;
import populations.Population;
import survivalselectors.SurvivalSelector;
import survivalselectors.TypeSelectionSurvival;

public class Params {
    public static int countOfPoint;
    public static double probabilityCrossover;
    public static TypeSelectionParents typeSelectionParents;
    public static TypeSelectionSurvival typeSelectionSurvival;
    public static int length;
    public static AlgorithmType type;
    public static DominanceType dt;
    public static int size;
}
