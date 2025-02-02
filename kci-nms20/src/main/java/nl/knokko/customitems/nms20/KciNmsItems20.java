package nl.knokko.customitems.nms20;

import net.minecraft.nbt.NBTTagCompound;
import nl.knokko.customitems.nms.CustomItemNBT;
import nl.knokko.customitems.nms.GeneralItemNBT;
import nl.knokko.customitems.nms16plus.KciNmsItems16Plus;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class KciNmsItems20 extends KciNmsItems16Plus {

    @Override
    public GeneralItemNBT generalReadOnlyNbt(ItemStack bukkitStack) {
        return new GeneralItemNBT20(CraftItemStack.asNMSCopy(bukkitStack), false);
    }

    @Override
    public GeneralItemNBT generalReadWriteNbt(ItemStack bukkitStack) {
        return new GeneralItemNBT20(CraftItemStack.asNMSCopy(bukkitStack), true);
    }

    @Override
    public void customReadOnlyNbt(ItemStack bukkitStack, Consumer<CustomItemNBT> useNBT) {
        useNBT.accept(new CustomItemNBT20(bukkitStack, false));
    }

    @Override
    public void customReadWriteNbt(ItemStack original, Consumer<CustomItemNBT> useNBT, Consumer<ItemStack> getNewStack) {
        CustomItemNBT20 nbt = new CustomItemNBT20(original, true);
        useNBT.accept(nbt);
        getNewStack.accept(nbt.getBukkitStack());
    }

    @Override
    public String getStackName(ItemStack stack) {
        net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(stack);
        return nms.y().getString();
    }

    @Override
    public String getTagAsString(ItemStack stack) {
        net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nms.v();
        return tag != null ? tag.toString() : null;
    }
}
