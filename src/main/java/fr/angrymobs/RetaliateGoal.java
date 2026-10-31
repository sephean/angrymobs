package fr.angrymobs;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

/**
 * Melee goal that works for mobs without an ATTACK_DAMAGE attribute
 * (vanilla doHurtTarget would crash on sheep, cows, pigs...).
 */
public class RetaliateGoal extends MeleeAttackGoal {
    private static final float DEFAULT_DAMAGE = 3.0F;

    public RetaliateGoal(PathfinderMob mob, double speed) {
        super(mob, speed, true);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        if (!this.canPerformAttack(target)) {
            return;
        }
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);

        float damage = this.mob.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)
                ? (float) this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE)
                : DEFAULT_DAMAGE;

        if (target.hurt(this.mob.damageSources().mobAttack(this.mob), damage)) {
            double dx = this.mob.getX() - target.getX();
            double dz = this.mob.getZ() - target.getZ();
            target.knockback(0.4D, dx, dz);
        }
    }
}
