package com.github.alexthe666.rats.client.model;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;

import java.util.Calendar;

public class ModelChristmasChest extends SegmentedModel<Entity> {
    private final ModelRenderer field_228862_a_;
    private final ModelRenderer field_228863_c_;
    private final ModelRenderer field_228864_d_;
    private final ModelRenderer field_228865_e_;
    private final ModelRenderer field_228866_f_;
    private final ModelRenderer field_228867_g_;
    private final ModelRenderer field_228868_h_;
    private final ModelRenderer field_228869_i_;
    private final ModelRenderer field_228870_j_;
    private boolean isChristmas;

    public ModelChristmasChest() {
        this.isChristmas = true;
        this.field_228863_c_ = new ModelRenderer(64, 64, 0, 19);
        this.field_228863_c_.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        this.field_228862_a_ = new ModelRenderer(64, 64, 0, 0);
        this.field_228862_a_.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        this.field_228862_a_.rotationPointY = 9.0F;
        this.field_228862_a_.rotationPointZ = 1.0F;
        this.field_228864_d_ = new ModelRenderer(64, 64, 0, 0);
        this.field_228864_d_.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
        this.field_228864_d_.rotationPointY = 8.0F;
        this.field_228866_f_ = new ModelRenderer(64, 64, 0, 19);
        this.field_228866_f_.addBox(1.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F, 0.0F);
        this.field_228865_e_ = new ModelRenderer(64, 64, 0, 0);
        this.field_228865_e_.addBox(1.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, 0.0F);
        this.field_228865_e_.rotationPointY = 9.0F;
        this.field_228865_e_.rotationPointZ = 1.0F;
        this.field_228867_g_ = new ModelRenderer(64, 64, 0, 0);
        this.field_228867_g_.addBox(15.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F, 0.0F);
        this.field_228867_g_.rotationPointY = 8.0F;
        this.field_228869_i_ = new ModelRenderer(64, 64, 0, 19);
        this.field_228869_i_.addBox(0.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F, 0.0F);
        this.field_228868_h_ = new ModelRenderer(64, 64, 0, 0);
        this.field_228868_h_.addBox(0.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, 0.0F);
        this.field_228868_h_.rotationPointY = 9.0F;
        this.field_228868_h_.rotationPointZ = 1.0F;
        this.field_228870_j_ = new ModelRenderer(64, 64, 0, 0);
        this.field_228870_j_.addBox(0.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F, 0.0F);
        this.field_228870_j_.rotationPointY = 8.0F;
    }

    @Override
    public void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return null;
    }
}
