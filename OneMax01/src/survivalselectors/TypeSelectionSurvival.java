package survivalselectors;

public enum TypeSelectionSurvival {
    AGE, FITNESS;

    @Override
    public String toString() {
        switch (this) {
            case AGE:
                return "Age based";
            case FITNESS:
                return "Fitness based";
        }
        return "";
    }
}
