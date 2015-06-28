package org.kohsuke.args4j;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class DefaultOption {
    @Option(name="-str",usage="set a string")
    public String str = "pretty string";
    
    @Option(name="-req",usage="set a string", required = true)
    public String req = "required";
    
    @Option(name="-noDefault")
    public String noDefault;
    
    @Option(name="-noDefaultReq", required = true)
    public String noDefaultReq;
    
    @Option(name="-byteVal", usage = "my favorite byte")
    public byte byteVal;
    
    @Option(name="-strArray", usage="my favorite strarr")
    public String strArray[] = new String[] { "san", "dra", "chen"};
    
    public enum DrinkName {
        BEER,
        WHISKEY,
        SCOTCH,
        BOURBON,
        BRANDY
    };
    
    @Option(name="-drinkArray", usage="my favorite drinks")
    public DrinkName drinkArray[] = new DrinkName[] { DrinkName.BEER, DrinkName.BOURBON };
    
    @Option(name="-drink", usage="my favorite drink")
    public DrinkName drink = DrinkName.BEER;
    
    @Option(name="-drinkList", usage="my favorite drinks")
    public List<DrinkName> drinkList = Arrays.asList(DrinkName.BEER, DrinkName.BRANDY);
    
    @Argument
    public String arguments[] = new String[] { "foo", "bar" };
}
