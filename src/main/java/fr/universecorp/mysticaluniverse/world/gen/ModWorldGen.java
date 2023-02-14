package fr.universecorp.mysticaluniverse.world.gen;

public class ModWorldGen {

    public static void generateWorldGen() {
        ModOreGeneration.generateOres();
        ModFlowerGeneration.generateFlowers();
        ModTreeGeneration.generateTrees();


    }
}
