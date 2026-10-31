package fr.angrymobs;

import fr.angrymobs.mixin.MobAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;

public class AngryMobs implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            // Only passive mobs: monsters and neutral mobs (wolves, golems, bees...) already fight back
            if (!(entity instanceof PathfinderMob mob) || entity instanceof Enemy || entity instanceof NeutralMob) {
                return;
            }

            MobAccessor accessor = (MobAccessor) mob;
            GoalSelector goals = accessor.angrymobs$getGoalSelector();
            GoalSelector targets = accessor.angrymobs$getTargetSelector();

            // No more running away: they stand and fight
            goals.removeAllGoals(goal -> goal instanceof PanicGoal);
            goals.addGoal(1, new RetaliateGoal(mob, 1.3D));
            // Hit one sheep and the whole herd comes for you
            targets.addGoal(1, new HurtByTargetGoal(mob).setAlertOthers());
        });
    }
}
