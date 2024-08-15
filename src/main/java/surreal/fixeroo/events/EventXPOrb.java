package surreal.fixeroo.events;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nonnull;

public class EventXPOrb {
    @SubscribeEvent
    public void remapLegacyClumps(@Nonnull RegistryEvent.MissingMappings<EntityEntry> event) {
        for(RegistryEvent.MissingMappings.Mapping<EntityEntry> mapping : event.getAllMappings()) {
            if(mapping.key.equals(new ResourceLocation("clumps", "xp_orb_big"))) {
                mapping.remap(GameData.getEntityRegistry().getValue(2));
                return;
            }
        }
    }
}
