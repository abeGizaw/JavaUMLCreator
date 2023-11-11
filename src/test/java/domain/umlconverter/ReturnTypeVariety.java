package domain.umlconverter;

import java.util.*;

public class ReturnTypeVariety {
    public int returnInt() { return 1; }
    public String returnString() { return "Hello"; }
    private List<Integer> returnList() { return Arrays.asList(1, 2, 3); }
    protected Set<Character> returnSet() { return new HashSet<>(Arrays.asList('a', 'b', 'c')); }
    Character returnCharacter() { return 'a'; }
    public double[] returnArray() { return new double[] {1.0, 2.0, 3.0}; }
    public Map<String, List<Double>> returnMap() {
        return Collections.singletonMap("key", Arrays.asList(1.2, 3.4));
    }
}

