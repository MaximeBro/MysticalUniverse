package fr.universecorp.mysticaluniverse.entity;

import dev.architectury.event.events.common.InteractionEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtCustomerGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.*;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class DruidEntity extends MerchantEntity implements InteractionObserver /*, Angerable */{

    public static final TrackedData<DruidState> STATE = DataTracker.registerData(DruidEntity.class, ModEntities.DRUID_STATE);
    private int levelUpTimer;
    private boolean levelingUp;
    private int experience;
    private int level;
    private long lastGossipDecayTime;
    private long lastRestockCheckTime;
    private long lastRestockTime;
    private int restocksToday;

    @Nullable
    private PlayerEntity lastCustomer;

    public DruidEntity(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
        this.experience = 0;
        this.level = 1; // IMPORTANT : the first trade offer is index (1) !
        this.levelingUp = true;
        this.getNavigation().setCanSwim(true);
    }

    public static DefaultAttributeContainer.Builder setAttribute() {
        return MerchantEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new LookAroundGoal(this));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(3, new LookAtCustomerGoal(this));
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.dataTracker.set(LAST_STATE_TICK, (float) world.toServerWorld().getTime() - DAMAGE_LENGTH);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void tick() {
        super.tick();
        this.decayGossip();

        if(this.world.isClient()) {
            this.updateAnimations();
        }
        this.updateState();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(STATE, DruidState.STANDING);
        this.dataTracker.startTracking(LAST_STATE_TICK, (float) -DAMAGE_LENGTH);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PLAYER_DEATH;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        super.playStepSound(pos, state);
    }

    @Override
    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_WANDERING_TRADER_YES;
    }

    @Override
    protected SoundEvent getTradingSound(boolean sold) {
        return SoundEvents.ENTITY_ILLUSIONER_AMBIENT;
    }

    @Override
    public boolean isTarget(LivingEntity entity, TargetPredicate predicate) {
        return false;
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


    private boolean canLevelUp() {
        int i = this.getLevel();
        return VillagerData.canLevelUp(i) && this.experience >= VillagerData.getUpperLevelExperience(i);
    }

    @Override
    protected void fillRecipes() {
        Int2ObjectMap<TradeOffers.Factory[]> int2ObjectMap =  new Int2ObjectOpenHashMap<>(ModEntities.TRADE_LIST);
        if (int2ObjectMap == null || int2ObjectMap.isEmpty()) {
            return;
        }
        TradeOffers.Factory[] factorys = int2ObjectMap.get(this.getLevel());
        if (factorys == null) {
            return;
        }
        TradeOfferList tradeOfferList = this.getOffers();
        this.fillRecipesFromPool(tradeOfferList, factorys, 2);
    }

    @Override
    public VillagerEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(this.isAlive() && !this.hasCustomer()) {
            if (!this.world.isClient && this.offers != null && !this.offers.isEmpty()) {
                this.beginTradeWith(player);
            }
            return ActionResult.success(this.world.isClient);
        }

        return super.interactMob(player, hand);
    }

    private void decayGossip() {
        long l = this.world.getTime();
        if (this.lastGossipDecayTime == 0L) {
            this.lastGossipDecayTime = l;
            return;
        }
        if (l < this.lastGossipDecayTime + 24000L) {
            return;
        }
        this.lastGossipDecayTime = l;
    }

    private void beginTradeWith(PlayerEntity customer) {
        this.setCustomer(customer);
        this.sendOffers(customer, Text.of("Druid"), this.getLevel());
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

        if(this.shouldRestock()) {
            this.restock();
            this.playSound(SoundEvents.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
        }

        super.mobTick();
    }

    public int getLevel() { return this.level; }
    public void setLevel(int level) { this.level = level; }

    @Override
    public int getExperience() { return this.experience; }

    private void levelUp() {
        this.setLevel(this.getLevel() + 1);
        this.fillRecipes();
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        return super.canSpawn(world, spawnReason);
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }

    private boolean needsRestock() {
        for (TradeOffer tradeOffer : this.getOffers()) {
            if (!tradeOffer.hasBeenUsed()) continue;
            return true;
        }
        return false;
    }

    private void clearDailyRestockCount() {
        this.restock();
        this.restocksToday = 0;
    }
    private boolean canRestock() {
        return this.restocksToday == 0 || this.restocksToday < 2 && this.world.getTime() > this.lastRestockTime + 2400L;
    }

    public boolean shouldRestock() {
        long l = this.lastRestockTime + 12000L;
        long m = this.world.getTime();
        boolean bl = m > l;
        long n = this.world.getTimeOfDay();
        if (this.lastRestockCheckTime > 0L) {
            long p = n / 24000L;
            long o = this.lastRestockCheckTime / 24000L;
            bl |= p > o;
        }
        this.lastRestockCheckTime = n;
        if (bl) {
            this.lastRestockTime = m;
            this.clearDailyRestockCount();
        }
        return this.canRestock() && this.needsRestock();
    }

    public void restock() {
        for (TradeOffer tradeOffer : this.getOffers()) {
            tradeOffer.resetUses();
        }
        this.lastRestockTime = this.world.getTime();
        ++this.restocksToday;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("Xp", NbtElement.INT_TYPE)) {
            this.experience = nbt.getInt("Xp");
        }

        if(nbt.contains("level", NbtElement.INT_TYPE)) {
            this.level = nbt.getInt("level");
        }

        this.lastRestockTime = nbt.getLong("LastRestock");
        this.lastGossipDecayTime = nbt.getLong("LastGossipDecay");
        this.restocksToday = nbt.getInt("RestocksToday");
        this.setCanPickUpLoot(false);

        if(nbt.getBoolean(STANDING_KEY)) {
            this.dataTracker.set(STATE, DruidState.STANDING);
        }

        if(nbt.getBoolean(DAMAGED_KEY)) {
            this.dataTracker.set(STATE, DruidState.DAMAGE);
        }

        this.setLastStateTick(nbt.getFloat(LAST_STATE_TICK_KEY));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putInt("level", this.level);
        nbt.putInt("Xp", this.experience);
        nbt.putLong("LastRestock", this.lastRestockTime);
        nbt.putLong("LastGossipDecay", this.lastGossipDecayTime);
        nbt.putInt("RestocksToday", this.restocksToday);

        nbt.putBoolean(DAMAGED_KEY, this.isDamaged());
        nbt.putBoolean(STANDING_KEY, this.isStanding());
        nbt.putFloat(LAST_STATE_TICK_KEY, this.dataTracker.get(LAST_STATE_TICK));
    }

    @Override
    protected void applyDamage(DamageSource source, float amount) {
        Entity entity = source.getAttacker();
        if(entity instanceof PlayerEntity player) {
            this.dataTracker.set(STATE, DruidState.DAMAGE);
        }

        super.applyDamage(source, amount);
    }

    @Override
    public void onInteractionWith(EntityInteraction interaction, Entity entity) { }

    @Override
    protected BodyControl createBodyControl() {
        return new DruidBodyControl(this);
    }

    static class DruidBodyControl extends BodyControl {
        public DruidBodyControl(DruidEntity entity) {
            super(entity);
        }

        @Override
        public void tick() {
            super.tick();
        }
    }


    /*
     * Animation Part :
     * - STANDING (basic body control)
     * - DAMAGE (custom animation)
     */

    public static final TrackedData<Float> LAST_STATE_TICK =
            DataTracker.registerData(DruidEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public final AnimationState damageAnimation = new AnimationState();
    public static final String LAST_STATE_TICK_KEY = "LastStateTick";
    public static final String DAMAGED_KEY = "Damaged";
    public static final String STANDING_KEY = "Standing";
    public static final long DAMAGE_LENGTH = (long) (SharedConstants.TICKS_PER_SECOND * 2.54167f);
    @Environment(EnvType.CLIENT)
    private void updateAnimations() {

        if(this.isDamaged()) {
            this.damageAnimation.startIfNotRunning(this.age);
        }

        if(this.isStanding()) {
            this.damageAnimation.stop();
        }
    }

    private void updateState() {
        if(this.dataTracker.get(STATE) == DruidState.DAMAGE && this.getLastStateTickDelta() > DAMAGE_LENGTH) {
            this.standUp();
        }

    }

    public boolean isDamaged() { return this.dataTracker.get(STATE) == DruidState.DAMAGE; }
    public boolean isStanding() { return this.dataTracker.get(STATE) == DruidState.STANDING; }

    public void setLastStateTick(float t) {
        this.dataTracker.set(LAST_STATE_TICK, t);
    }

    public float getLastStateTickDelta() {
        return this.world.getTime() - this.dataTracker.get(LAST_STATE_TICK);
    }


    public void standUp() {
        this.dataTracker.set(STATE, DruidState.STANDING);
        this.setLastStateTick(this.world.getTime());
    }

}
