package com.github.alexthe666.rats.server.world.village;

public class WorldGenGarbageHeap{} /* extends WorldGenerator {

    private static final ResourceLocation STRUCTURE = new ResourceLocation(RatsMod.MODID, "village_garbage_heap");
    private VillageComponentGarbageHeap component;
    private Rotation rotation;
    private Direction facing;

    public WorldGenGarbageHeap(VillageComponentGarbageHeap component, Direction facing) {
        this.component = component;
        this.facing = facing;
        switch (facing) {
            case SOUTH:
                rotation = Rotation.CLOCKWISE_180;
                break;
            case EAST:
                rotation = Rotation.CLOCKWISE_90;
                break;
            case WEST:
                rotation = Rotation.COUNTERCLOCKWISE_90;
                break;
            default:
                rotation = Rotation.NONE;
                break;
        }
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (worldIn == null) {
            return false;
        }
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(WorldGenRatRuin.getRotationFromFacing(facing)).setReplacedBlock(Blocks.AIR);
        Template template = templateManager.getTemplate(server, STRUCTURE);
        Biome biome = worldIn.getBiome(position);
        BlockPos genPos = position.down(3).offset(facing, template.getSize().getZ()).offset(facing.rotateYCCW(), template.getSize().getX());
        template.addBlocksToWorld(worldIn, genPos, new RatsVillageProcessor(position.up(3), null, settings, biome), settings, 2);
        return true;
    }
}*/
