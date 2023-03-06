package fr.universecorp.mysticaluniverse.entity;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.MysticalUniverseClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class DruidEntityRenderer extends MobEntityRenderer<DruidEntity, DruidEntityModel> {

    public DruidEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DruidEntityModel(context.getPart(MysticalUniverseClient.DRUID_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(DruidEntity entity) {
        return new Identifier(MysticalUniverse.MODID, "textures/entity/druid/druid.png");
    }
}
