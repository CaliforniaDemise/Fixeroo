package surreal.fixeroo;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import surreal.fixeroo.events.EventXPOrb;

@Mod.EventBusSubscriber
@Mod(modid = Fixeroo.MODID, name = "Fixeroo", version = "@VERSION@", dependencies = "required-after:configanytime")
@SuppressWarnings("unused")
public class Fixeroo {

    public static final String MODID = "xporbclump";

    @Mod.EventHandler
    public void construction(FMLConstructionEvent event) {
        if (FixerooConfig.xpOrbClump.enable) MinecraftForge.EVENT_BUS.register(EventXPOrb.class);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new Item() {
            @Override
            public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
                if (!worldIn.isRemote) {
                    System.out.println(playerIn.getPosition());
                }
                return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            }
        }.setRegistryName(MODID, "test").setCreativeTab(CreativeTabs.MISC));
    }
}
