package surreal.fixeroo;

import gregtech.core.sound.GTSoundEvents;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.item.tool.ItemToolPainter;
import ic2.core.util.Ic2Color;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import java.util.Objects;

public class IntegrationHandler {

    // Shulker coloring integration for GTCEu Spray Can and IC2 Painter.
    public static EnumDyeColor getColor(EntityPlayer player, EnumHand hand, ItemStack stack) {
        String gt = "gregtech", ic2 = "ic2";
        ResourceLocation regName = Objects.requireNonNull(stack.getItem().getRegistryName());

        String mod = regName.getNamespace();
        String path = regName.getPath();

        if (mod.equals(gt) && path.equals("meta_item_1")) {
            String usesLeft = "GT.UsesLeft";
            EnumDyeColor color = null;

            if (stack.getMetadata() == 60) color = EnumDyeColor.PURPLE;
            if (stack.getMetadata() >= 62 && stack.getMetadata() <= 77) {
                int dye = stack.getMetadata() - 62;
                color = EnumDyeColor.byMetadata(dye);
            }
            if (color != null) {
                NBTTagCompound tag = stack.getTagCompound();
                if (tag == null) {
                    tag = new NBTTagCompound();
                    if (stack.getMetadata() == 60) tag.setInteger(usesLeft, 1024);
                    else tag.setInteger(usesLeft, 512);
                    stack.setTagCompound(tag);
                }
                tag.setInteger(usesLeft, tag.getInteger(usesLeft) - 1);
                player.world.playSound(null, player.posX, player.posY, player.posZ, GTSoundEvents.SPRAY_CAN_TOOL, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            return color;
        }
        else if (mod.equals(ic2) && stack.getItem() instanceof ItemToolPainter) {
            ItemToolPainter painter = (ItemToolPainter) stack.getItem();
            Ic2Color color = painter.getColor(stack);
            painter.damagePainter(player, hand, color);
            if (player.world.isRemote) {
                IC2.audioManager.playOnce(player, PositionSpec.Hand, "Tools/Painter.ogg", true, IC2.audioManager.getDefaultVolume());
            }
            return color.mcColor;
        }

        return null;
    }
}
