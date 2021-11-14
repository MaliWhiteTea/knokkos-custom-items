package nl.knokko.customitems.projectile.effect;

import nl.knokko.customitems.itemset.SItemSet;
import nl.knokko.customitems.trouble.UnknownEncodingException;
import nl.knokko.customitems.util.ProgrammingValidationException;
import nl.knokko.customitems.util.ValidationException;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

import static nl.knokko.customitems.util.Checks.isClose;

public class ExplosionValues extends ProjectileEffectValues {

    static ExplosionValues load(BitInput input, byte encoding) throws UnknownEncodingException {
        ExplosionValues result = new ExplosionValues(false);

        if (encoding == ENCODING_EXPLOSION_1) {
            result.load1(input);
        } else {
            throw new UnknownEncodingException("ExplosionProjectileEffect", encoding);
        }

        return result;
    }

    public static ExplosionValues createQuick(float power, boolean destroyBlocks, boolean setFire) {
        ExplosionValues result = new ExplosionValues(true);
        result.setPower(power);
        result.setDestroyBlocks(destroyBlocks);
        result.setSetFire(setFire);
        return result;
    }

    private float power;

    private boolean destroyBlocks;
    private boolean setFire;

    public ExplosionValues(boolean mutable) {
        super(mutable);
        this.power = 1f;
        this.destroyBlocks = true;
        this.setFire = false;
    }

    public ExplosionValues(ExplosionValues toCopy, boolean mutable) {
        super(mutable);
        this.power = toCopy.getPower();
        this.destroyBlocks = toCopy.destroysBlocks();
        this.setFire = toCopy.setsFire();
    }

    @Override
    public String toString() {
        return "Explosion(" + power + ")";
    }

    private void load1(BitInput input) {
        this.power = input.readFloat();
        this.destroyBlocks = input.readBoolean();
        this.setFire = input.readBoolean();
    }

    @Override
    public void save(BitOutput output) {
        output.addByte(ENCODING_EXPLOSION_1);
        output.addFloat(power);
        output.addBooleans(destroyBlocks, setFire);
    }

    @Override
    public ExplosionValues copy(boolean mutable) {
        return new ExplosionValues(this, mutable);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ExplosionValues) {
            ExplosionValues otherEffect = (ExplosionValues) other;
            return isClose(this.power, otherEffect.power) && this.destroyBlocks == otherEffect.destroyBlocks
                    && this.setFire == otherEffect.setFire;
        } else {
            return false;
        }
    }
    public float getPower() {
        return power;
    }

    public boolean destroysBlocks() {
        return destroyBlocks;
    }

    public boolean setsFire() {
        return setFire;
    }

    public void setPower(float newPower) {
        assertMutable();
        this.power = newPower;
    }

    public void setDestroyBlocks(boolean shouldDestroyBlocks) {
        assertMutable();
        this.destroyBlocks = shouldDestroyBlocks;
    }

    public void setSetFire(boolean shouldSetFire) {
        assertMutable();
        this.setFire = shouldSetFire;
    }

    @Override
    public void validate(SItemSet itemSet) throws ValidationException, ProgrammingValidationException {
        if (power <= 0f) throw new ValidationException("Power must be positive");
        if (Float.isNaN(power)) throw new ValidationException("Power can't be NaN");
    }
}
