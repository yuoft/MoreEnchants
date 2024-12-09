package com.yuo.Enchants.Proxy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ColorBoltRender extends EntityRenderer<ColorBolt> {
    public ColorBoltRender(Context pContext) {
        super(pContext);
    }

    @Override
    public void render(ColorBolt pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float[] $$6 = new float[8];
        float[] $$7 = new float[8];
        float $$8 = 0.0F;
        float $$9 = 0.0F;
        Random $$10 = new Random(pEntity.seed);

        for(int $$11 = 7; $$11 >= 0; --$$11) {
            $$6[$$11] = $$8;
            $$7[$$11] = $$9;
            $$8 += (float)($$10.nextInt(11) - 5);
            $$9 += (float)($$10.nextInt(11) - 5);
        }

        VertexConsumer $$12 = pBuffer.getBuffer(RenderType.lightning());
        Matrix4f $$13 = pPoseStack.last().pose();

        for(int $$14 = 0; $$14 < 4; ++$$14) {
            Random $$15 = new Random(pEntity.seed);

            for(int $$16 = 0; $$16 < 3; ++$$16) {
                int $$17 = 7;
                int $$18 = 0;
                if ($$16 > 0) {
                    $$17 = 7 - $$16;
                }

                if ($$16 > 0) {
                    $$18 = $$17 - 2;
                }

                float $$19 = $$6[$$17] - $$8;
                float $$20 = $$7[$$17] - $$9;

                for(int $$21 = $$17; $$21 >= $$18; --$$21) {
                    float $$22 = $$19;
                    float $$23 = $$20;
                    if ($$16 == 0) {
                        $$19 += (float)($$15.nextInt(11) - 5);
                        $$20 += (float)($$15.nextInt(11) - 5);
                    } else {
                        $$19 += (float)($$15.nextInt(31) - 15);
                        $$20 += (float)($$15.nextInt(31) - 15);
                    }

                    float $$24 = 0.5F;
                    Random random = new Random(pEntity.seed);
                    float r = random.nextFloat();
                    float g = random.nextFloat();
                    float b = random.nextFloat();
                    float $$28 = 0.1F + (float)$$14 * 0.2F;
                    if ($$16 == 0) {
                        $$28 *= (float)$$21 * 0.1F + 1.0F;
                    }

                    float $$29 = 0.1F + (float)$$14 * 0.2F;
                    if ($$16 == 0) {
                        $$29 *= ((float)$$21 - 1.0F) * 0.1F + 1.0F;
                    }

                    quad($$13, $$12, $$19, $$20, $$21, $$22, $$23, r, g, b, $$28, $$29, false, false, true, false);
                    quad($$13, $$12, $$19, $$20, $$21, $$22, $$23, r, g, b, $$28, $$29, true, false, true, true);
                    quad($$13, $$12, $$19, $$20, $$21, $$22, $$23, r, g, b, $$28, $$29, true, true, false, true);
                    quad($$13, $$12, $$19, $$20, $$21, $$22, $$23, r, g, b, $$28, $$29, false, true, false, false);
                }
            }
        }
    }

    private static void quad(Matrix4f p_115273_, VertexConsumer p_115274_, float p_115275_, float p_115276_, int p_115277_, float p_115278_, float p_115279_, float p_115280_, float p_115281_, float p_115282_, float p_115283_, float p_115284_, boolean p_115285_, boolean p_115286_, boolean p_115287_, boolean p_115288_) {
        p_115274_.vertex(p_115273_, p_115275_ + (p_115285_ ? p_115284_ : -p_115284_), (float)(p_115277_ * 16), p_115276_ + (p_115286_ ? p_115284_ : -p_115284_)).color(p_115280_, p_115281_, p_115282_, 0.3F).endVertex();
        p_115274_.vertex(p_115273_, p_115278_ + (p_115285_ ? p_115283_ : -p_115283_), (float)((p_115277_ + 1) * 16), p_115279_ + (p_115286_ ? p_115283_ : -p_115283_)).color(p_115280_, p_115281_, p_115282_, 0.3F).endVertex();
        p_115274_.vertex(p_115273_, p_115278_ + (p_115287_ ? p_115283_ : -p_115283_), (float)((p_115277_ + 1) * 16), p_115279_ + (p_115288_ ? p_115283_ : -p_115283_)).color(p_115280_, p_115281_, p_115282_, 0.3F).endVertex();
        p_115274_.vertex(p_115273_, p_115275_ + (p_115287_ ? p_115284_ : -p_115284_), (float)(p_115277_ * 16), p_115276_ + (p_115288_ ? p_115284_ : -p_115284_)).color(p_115280_, p_115281_, p_115282_, 0.3F).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(ColorBolt colorBolt) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
