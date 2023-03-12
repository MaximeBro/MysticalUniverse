package fr.universecorp.mysticaluniverse.custom.blocks.entity;

import fr.universecorp.mysticaluniverse.registry.ModBlockEntities;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;


public class InfusedLilyEntity extends BlockEntity {

    private int tickDelta = 0;
    private int produceTime = 0;
    private int maxProduceTime = 600;
    private boolean retainEssence = false;

    public InfusedLilyEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INFUSED_LILY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.tickDelta = nbt.getInt("tickDelta");
        this.produceTime = nbt.getInt("produceTime");
        this.maxProduceTime = nbt.getInt("maxProduceTime");
        this.retainEssence = nbt.getBoolean("retainsEssence");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt("tickDelta", this.tickDelta);
        nbt.putInt("produceTime", this.produceTime);
        nbt.putInt("maxProduceTime", this.maxProduceTime);
        nbt.putBoolean("retainsEssence", this.retainEssence);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, InfusedLilyEntity entity) {

        entity.tickDelta++;
        if(entity.tickDelta >= 200) {
            entity.tickDelta = 0;
        }

        boolean hasEnoughSources = entity.checkFluidInRadius(world, blockPos, 5, 3);

        if(hasEnoughSources) {
            if(entity.produceTime < entity.maxProduceTime && !entity.retainEssence) {
                entity.produceTime++;
                entity.retainEssence = false;
                entity.playParticles(world, blockPos, entity.tickDelta);
            }

            if(entity.produceTime >= entity.maxProduceTime) {
                entity.retainEssence = true;
                entity.produceTime = 0;
            }
        }
    }


    public void playParticles(World world, BlockPos blockPos, int tickDelta) {
        Random random = Random.create();
        double x = (double)blockPos.getX();
        double y = (double)blockPos.getY() + 0.5;
        double z = (double)blockPos.getZ();
        double offSetX = random.nextDouble() * 1;
        double offSetY = random.nextDouble() * 0.5;
        double offSetZ = random.nextDouble() * 1;


        if(tickDelta % 5 == 0) {
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x + offSetX, y + offSetY, z + offSetZ, 0.0, 0.0, 0.0);

        }
    }


    /**
     * {@return true if there are nbOfSourcesNeeded in the radius, false is nbOfSources found are < nbOfSourcesNeeded}
     * <p>This method will check every block in the search radius at a y-1 layer.</p>
     * @param world the world of the BlockEntity
     * @param pos the BlockPos of the Block
     * @param radius the area, might be odd numbers (example : 5 -> search area = 5x5)
     * @param nbOfSourcesNeeded how many sources of EtherFluid needed to return true
     * @Author Pulsion
     * @Copyright UniverseCorp - Pulsion
     **/
    public boolean checkFluidInRadius(World world, BlockPos pos, int radius, int nbOfSourcesNeeded) {
        if(radius % 2 == 0 || radius < 3) {
            return false;
        }

        int nbOfBlocksToCheck = (int) Math.pow(radius, 2);
        int nbOfSources = 0;

        int actualHeight = 0;
        int actualWidth = 0;

        BlockPos startPos = new BlockPos(pos.getX() - (radius / 2), pos.getY() - 1, pos.getZ() - (radius / 2));
        for(int i=0; i < nbOfBlocksToCheck; i++) {
            if(actualWidth >= radius) { actualWidth = 0; actualHeight++; }

            BlockPos actualPos = new BlockPos(startPos.getX() + actualWidth, startPos.getY(), startPos.getZ() + actualHeight);
            Block block = world.getBlockState(actualPos).getBlock();
            if(block == ModFluids.LIQUID_ETHER_BLOCK && world.getFluidState(actualPos).getLevel() >= 8) {
                nbOfSources++;
            }

            actualWidth++;
        }

        return nbOfSources >= nbOfSourcesNeeded;
    }

    public boolean hasEssence() { return this.retainEssence; }
    public void setEssence(boolean hasEssence) { this.retainEssence = false; }
}
