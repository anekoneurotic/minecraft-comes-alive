package mca.network.client;

import mca.client.render.MCAPlayerEntityRenderer;
import mca.entity.EntitiesMCA;
import mca.entity.VillagerEntityMCA;
import mca.network.S2CNbtDataMessage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class PlayerDataMessage extends S2CNbtDataMessage {
    private final UUID uuid;

    public PlayerDataMessage(UUID uuid, NbtCompound nbt) {
        super(nbt);
        this.uuid = uuid;
    }

    @Override
    public void receive(PlayerEntity e) {
        VillagerEntityMCA mca = EntitiesMCA.MALE_VILLAGER.get().create(MinecraftClient.getInstance().world);
        assert mca != null;
        mca.readCustomDataFromNbt(getData());
        MCAPlayerEntityRenderer.playerData.put(uuid, mca);
    }
}
