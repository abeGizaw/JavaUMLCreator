package domain.umlconverter;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParameterTypeVariety {
    public void singlePrimitive(int a) {}
    public void multiplePrimitives(int a, double b, char c) {}
    public void singleObject(String s) {}
    public void multipleObjects(String s, Integer num, Double d) {}
    public void mixPrimitiveAndObject(int a, String b, double c) {}
    private void withCollections(List<String> list, Set<Character> set, Map<String, Double> map) {}
    protected void withArrayAndList(int[] nums, List<Double> doubles) {}
    void complexParameters(int a, String b, List<List<Integer>> nums, double[] arr, Map<String, Set<Character>> map) {}
}
