package fr.universecorp.mysticaluniverse.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DruidEntity extends MerchantEntity implements InteractionObserver, VillagerDataContainer {

    private static final TrackedData<VillagerData> VILLAGER_DATA = DataTracker.registerData(VillagerEntity.class, TrackedDataHandlerRegistry.VILLAGER_DATA);
    private final VillagerGossips gossip = new VillagerGossips();
    private int levelUpTimer;
    private boolean levelingUp;
    private int experience;

    @Nullable
    private PlayerEntity lastCustomer;

    public DruidEntity(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
        this.experience = 0;
        this.levelingUp = true;
    }

    @Override
    protected void afterUsing(TradeOffer offer) {
        int expAmount = 3 + this.random.nextInt(4);
        this.experience += offer.getMerchantExperience();
        this.lastCustomer = this.getCustomer();
        if (this.canLevelUp()) {
            this.levelUpTimer = 40;
            this.levelingUp = true;
            expAmount += 5;
        }

        if (offer.shouldRewardPlayerExperience()) {
            this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.getX(), this.getY() + 0.5, this.getZ(), expAmount));
        }
    }

    public VillagerData getVillagerData() {
        return this.dataTracker.get(VILLAGER_DATA);
    }

    @Override
    public void setVillagerData(VillagerData villagerData) {
        VillagerData villagerData2 = this.getVillagerData();
        if (villagerData2.getProfession() != villagerData.getProfession()) {
            this.offers = null;
        }
        this.dataTracker.set(VILLAGER_DATA, villagerData);
    }

    private boolean canLevelUp() {
        int i = this.getVillagerData().getLevel();
        return VillagerData.canLevelUp(i) && this.experience >= VillagerData.getUpperLevelExperience(i);
    }

    @Override
    protected void fillRecipes() {

    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public void onInteractionWith(EntityInteraction interaction, Entity entity) {
        if (interaction == EntityInteraction.TRADE) {
            this.gossip.startGossip(entity.getUuid(), VillageGossipType.TRADING, 2);
        }
    }

    @Override
    protected void mobTick() {
        if(this.hasCustomer() && this.levelUpTimer > 0) {
            --this.levelUpTimer;
            if(this.levelUpTimer <= 0) {
                if(this.levelingUp) {
                    this.levelUp();
                    this.levelingUp = false;
                }

                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0));
            }
        }

        if (this.lastCustomer != null && this.world instanceof ServerWorld) {
            ((ServerWorld)this.world).handleInteraction(EntityInteraction.TRADE, this.lastCustomer, this);
            this.world.sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
            this.lastCustomer = null;
        }

        super.mobTick();
    }

    private void levelUp() {
        this.setVillagerData(this.getVillagerData().withLevel(this.getVillagerData().getLevel() + 1));
        this.fillRecipes();
    }
}
