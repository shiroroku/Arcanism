package team.arcanism.Registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.arcanism.Arcanism;
import team.arcanism.Block.ArcaneInfuser.ArcaneInfuserContainer;

public class ContainerRegistry {

	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Arcanism.MODID);

	public static final RegistryObject<MenuType<ArcaneInfuserContainer>> arcanist_workbench = CONTAINERS.register("arcanist_workbench", () -> IForgeMenuType.create((id, playerInv, data) -> new ArcaneInfuserContainer(id, data.readBlockPos(), playerInv, playerInv.player)));

}
