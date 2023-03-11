package fr.universecorp.mysticaluniverse.entity.renderers;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.MysticalUniverseClient;
import fr.universecorp.mysticaluniverse.entity.DruidEntity;
import fr.universecorp.mysticaluniverse.entity.DruidEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class DruidEntityRenderer<E extends DruidEntity> extends MobEntityRenderer<E, DruidEntityModel<E>> {

    public DruidEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DruidEntityModel<>(context.getPart(MysticalUniverseClient.DRUID_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(DruidEntity entity) {
        return new Identifier(MysticalUniverse.MODID, "textures/entity/druid/druid.png");
    }
}
