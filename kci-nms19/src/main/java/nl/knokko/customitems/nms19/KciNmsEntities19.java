package nl.knokko.customitems.nms19;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSourceIndirect;
import net.minecraft.world.entity.projectile.EntitySmallFireball;
import net.minecraft.world.phys.Vec3D;
import nl.knokko.customitems.nms16plus.KciNmsEntities16Plus;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Optional;

class KciNmsEntities19 extends KciNmsEntities16Plus {

    @Override
    public void causeFakeProjectileDamage(Entity toDamage, Entity responsibleShooter, float damage, double projectilePositionX, double projectilePositionY, double projectilePositionZ, double projectileMotionX, double projectileMotionY, double projectileMotionZ) {
        ((CraftEntity) toDamage).getHandle().a(new EntityDamageSourceIndirect("thrown",
                new EntitySmallFireball(((CraftWorld) toDamage.getWorld()).getHandle(),
                        projectilePositionX, projectilePositionY, projectilePositionZ,
                        projectileMotionX, projectileMotionY, projectileMotionZ),
                ((CraftEntity) responsibleShooter).getHandle()), damage);
    }

    @Override
    public void causeCustomPhysicalAttack(Entity attacker, Entity target, float damage, String damageCauseName, boolean ignoresArmor, boolean isFire) {
        DamageSource damageSource = new CustomEntityDamageSource(damageCauseName, ((CraftEntity) attacker).getHandle())
                .setIgnoreArmor(ignoresArmor).setFire(isFire);

        ((CraftEntity) target).getHandle().a(damageSource, damage);
    }

    @Override
    public double distanceToLineStart(Entity entity, Location lineStartLocation, Vector direction, double safeUpperBound) {
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        Vec3D lineStart = new Vec3D(
                lineStartLocation.getX(),
                lineStartLocation.getY(),
                lineStartLocation.getZ()
        );
        Vec3D lineEnd = new Vec3D(
                lineStartLocation.getX() + safeUpperBound * direction.getX(),
                lineStartLocation.getY() + safeUpperBound * direction.getY(),
                lineStartLocation.getZ() + safeUpperBound * direction.getZ()
        );


        Optional<Vec3D> intersection = nmsEntity.cy().b(lineStart, lineEnd);
        return intersection.map(vec3D -> Math.sqrt(vec3D.g(lineStart))).orElse(Double.POSITIVE_INFINITY);
    }
}
