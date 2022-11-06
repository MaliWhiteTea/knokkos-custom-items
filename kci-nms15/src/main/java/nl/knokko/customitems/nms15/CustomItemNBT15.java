package nl.knokko.customitems.nms15;

import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import nl.knokko.customitems.nms.BooleanRepresentation;
import nl.knokko.customitems.nms.CustomItemNBT;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;

public class CustomItemNBT15 implements CustomItemNBT {

    private final ItemStack nmsStack;
    private NBTTagCompound nbt;

    private boolean allowWrite;

    CustomItemNBT15(org.bukkit.inventory.ItemStack bukkitStack, boolean allowWrite) {
        this.nmsStack = CraftItemStack.asNMSCopy(bukkitStack);
        this.nbt = nmsStack.getTag();
        this.allowWrite = allowWrite;
    }

    org.bukkit.inventory.ItemStack getBukkitStack() {
        nmsStack.setTag(nbt);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    private NBTTagCompound getOurTag() {
        return nbt.getCompound(KEY);
    }

    public boolean hasOurNBT() {
        return nbt != null && nbt.hasKey(KEY);
    }

    private void assertOurNBT() throws UnsupportedOperationException {
        if (!hasOurNBT())
            throw new UnsupportedOperationException("This item stack doesn't have our nbt tag");
    }

    private NBTTagCompound getOrCreateOurNBT() {
        if (hasOurNBT()) {
            return getOurTag();
        } else {
            assertWrite();
            NBTTagCompound ourNBT = new NBTTagCompound();
            if (nbt == null) {
                nbt = new NBTTagCompound();
            }
            nbt.set(KEY, ourNBT);
            return ourNBT;
        }
    }

    private void assertWrite() {
        if (!allowWrite)
            throw new UnsupportedOperationException("This CustomItemNBT is read-only");
    }

    public String getName() throws UnsupportedOperationException {
        assertOurNBT();

        return getOurTag().getString(NAME);
    }

    public Long getLastExportTime() throws UnsupportedOperationException {
        assertOurNBT();

        if (getOurTag().hasKey(LAST_EXPORT_TIME)) {
            return getOurTag().getLong(LAST_EXPORT_TIME);
        } else {
            return null;
        }
    }

    public void setLastExportTime(long newLastExportTime) throws UnsupportedOperationException {
        assertWrite();
        assertOurNBT();
        getOurTag().setLong(LAST_EXPORT_TIME, newLastExportTime);
    }

    public BooleanRepresentation getBooleanRepresentation() throws UnsupportedOperationException {
        assertOurNBT();

        if (getOurTag().hasKey(BOOL_REPRESENTATION)) {
            byte[] byteRepresentation = getOurTag().getByteArray(BOOL_REPRESENTATION);
            return new BooleanRepresentation(byteRepresentation);
        } else {
            return null;
        }
    }

    public void setBooleanRepresentation(BooleanRepresentation newBoolRepresentation) throws UnsupportedOperationException {
        assertWrite();
        assertOurNBT();

        getOurTag().setByteArray(BOOL_REPRESENTATION, newBoolRepresentation.getAsBytes());
    }

    public Long getDurability() throws UnsupportedOperationException {
        assertOurNBT();

        NBTTagCompound ourTag = getOurTag();
        if (!ourTag.hasKey(DURABILITY))
            return null;

        return getOurTag().getLong(DURABILITY);
    }

    public void setDurability(long newDurability) throws UnsupportedOperationException {
        assertWrite();
        assertOurNBT();
        getOurTag().setLong(DURABILITY, newDurability);
    }

    public void removeDurability() throws UnsupportedOperationException {
        assertWrite();
        assertOurNBT();
        getOurTag().remove(DURABILITY);
    }

    public void set(String name, long lastExportTime, Long maxDurability,
                    BooleanRepresentation boolRepresentation) throws UnsupportedOperationException {
        assertWrite();
        NBTTagCompound nbt = getOrCreateOurNBT();
        nbt.setString(NAME, name);
        nbt.setLong(LAST_EXPORT_TIME, lastExportTime);
        if (maxDurability != null) {
            nbt.setLong(DURABILITY, maxDurability);
        }
        nbt.setByteArray(BOOL_REPRESENTATION, boolRepresentation.getAsBytes());
    }
}
