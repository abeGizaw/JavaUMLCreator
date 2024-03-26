package domain.diagramconverter.ClassUmlMockTestClasses;

import domain.diagramconverter.RelationsManager;

import java.util.*;

public class DependsOnRelations {
    public void hasParams(RelationsManager r){}
    public void hasParams2(ImplementingClass r){}
    public void hasParams3(ParameterTypeVariety r){}

    public  ReturnTypeVariety returnsParams(){
        return new ReturnTypeVariety();
    }
    public ArrayFieldsConverter returnsParams2(){
        return new ArrayFieldsConverter();
    }
    public AccessModifierVariety returnsParams3(){
        return new AccessModifierVariety();
    }

    public List<ReturnTypeVariety> returnList(){
        return new ArrayList<>();
    }

    public void hasMap(HashMap<PrimitiveFieldsConverter, ObjectFieldsConverter> hash){}

    public MockInterface[] returnJavaLis(){return null;}

    public void hasLotsOfCollections(Set<MockEnum> mockEnumSet, MockAbstract[] mockAbstracts, ArrayList<HasATest> implementingClasses){
    }

    public String testDontAdd(String s, double d){
        return "silly";
    }


}
