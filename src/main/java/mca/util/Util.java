package mca.util;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import mca.core.MCA;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Util {
    private static final String RESOURCE_PREFIX = "assets/mca/";

    private Util() {
    }

    public static String readResource(String path) {
        String data;
        String location = RESOURCE_PREFIX + path;

        try {
            data = IOUtils.toString(new InputStreamReader(MCA.class.getClassLoader().getResourceAsStream(location)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource from JAR: " + location);
        }

        return data;
    }

    public static <T> T readResourceAsJSON(String path, Class<T> type) {
        Gson gson = new Gson();
        T data = gson.fromJson(Util.readResource(path), type);
        return data;
    }

    public static Optional<Entity> getEntityByUUID(World world, UUID uuid) {
        for (Entity entity : world.loadedEntityList) {
            if (entity.getUniqueID().equals(uuid)) {
                return Optional.of(entity);
            }
        }
        return Optional.absent();
    }

    public static <T extends Entity> Optional<T> getEntityByUUID(World world, UUID uuid, Class<? extends T> clazz) {
        for (Entity entity : world.loadedEntityList) {
            if (entity.getClass().isAssignableFrom(clazz) && entity.getUniqueID().equals(uuid)) {
                return Optional.of((T) entity);
            }
        }
        return Optional.absent();
    }

    public static List<BlockPos> getNearbyBlocks(BlockPos origin, World world, @Nullable Class filter, int xzDist, int yDist) {
        final List<BlockPos> pointsList = new ArrayList<>();
        for (int x = -xzDist; x <= xzDist; x++) {
            for (int y = -yDist; y <= yDist; y++) {
                for (int z = -xzDist; z <= xzDist; z++) {
                    if (x != 0 || y != 0 || z != 0) {
                        BlockPos pos = new BlockPos(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                        if (filter != null && filter.isAssignableFrom(world.getBlockState(pos).getBlock().getClass())) {
                            pointsList.add(pos);
                        } else if (filter == null) {
                            pointsList.add(pos);
                        }
                    }
                }
            }
        }
        return pointsList;
    }

    public static BlockPos getNearestPoint(BlockPos origin, List<BlockPos> blocks) {
        double closest = 100.0D;
        BlockPos returnPoint = null;
        for (BlockPos point : blocks) {
            double distance = origin.getDistance(point.getX(), point.getY(), point.getZ());
            if (distance < closest) {
                closest = distance;
                returnPoint = point;
            }
        }

        return returnPoint;
    }
}