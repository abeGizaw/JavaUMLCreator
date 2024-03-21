package domain.diagramconverter.ClassUmlMockTestClasses;

import domain.diagramconverter.RelationsManager;

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

    public String testDontAdd(String s, double d){
        return "silly";
    }


}
