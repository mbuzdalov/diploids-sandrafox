package parentselectors;

public enum TypeSelectionParents {
    RWS, SUS, TS, RS;

    @Override
    public String toString() {
        switch (this) {
            case RS:
                return "Rank Selection";
            case TS:
                return "Tournament Selection";
            case RWS:
                return "Roulete Wheel Selection";
            case SUS:
                return "Stochastic Universal Selection";
        }
        return "";
    }
}
