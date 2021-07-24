package mca.network;

import mca.client.gui.Constraint;
import mca.cobalt.network.Message;
import mca.cobalt.network.NetworkHandler;
import mca.entity.VillagerEntityMCA;
import mca.entity.ai.Relationship;
import mca.network.client.GetInteractDataResponse;
import mca.server.world.data.FamilyTreeEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import java.util.Set;
import java.util.UUID;

public class GetInteractDataRequest implements Message {
    private static final long serialVersionUID = -4363277735373237564L;

    UUID uuid;

    public GetInteractDataRequest(UUID villager) {
        this.uuid = villager;
    }

    @Override
    public void receive(PlayerEntity player) {
        Entity entity = ((ServerWorld) player.world).getEntity(uuid);

        if (entity instanceof VillagerEntityMCA && player instanceof ServerPlayerEntity) {
            VillagerEntityMCA villager = (VillagerEntityMCA) entity;

            //get constraints
            Set<Constraint> constraints = Constraint.allMatching(villager, player);

            Relationship relationship = villager.getRelationships();
            FamilyTreeEntry family = relationship.getFamily();

            String fatherName = relationship.getFamilyTree().getOrEmpty(family.father()).map(FamilyTreeEntry::name).orElse(null);
            String motherName = relationship.getFamilyTree().getOrEmpty(family.mother()).map(FamilyTreeEntry::name).orElse(null);

            NetworkHandler.sendToPlayer(new GetInteractDataResponse(constraints, fatherName, motherName), (ServerPlayerEntity)player);
        }
    }
}