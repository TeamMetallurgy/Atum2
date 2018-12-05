package com.teammetallurgy.atum.blocks.wood;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank.WoodType;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBranch extends Block {
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.<EnumFacing>create("facing", EnumFacing.class);
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    public static final Map<EnumFacing, AxisAlignedBB> bounds;
    public static final Map<EnumFacing, AxisAlignedBB> connectedBounds;
    
    public static final AxisAlignedBB EAST_AABB  = new AxisAlignedBB(5/16D, 5/16D, 5/16D, 1.0D,   11/16D, 11/16D);
    public static final AxisAlignedBB WEST_AABB  = new AxisAlignedBB(0.0D,  5/16D, 5/16D, 11/16D, 11/16D, 11/16D);
    public static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(5/16D, 5/16D, 0.0D,  11/16D, 11/16D, 11/16D);
    public static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(5/16D, 5/16D, 5/16D, 11/16D, 11/16D, 1.0D);
    public static final AxisAlignedBB UP_AABB    = new AxisAlignedBB(5/16D, 5/16D, 5/16D, 11/16D, 1.0D,   11/16D);
    public static final AxisAlignedBB DOWN_AABB  = new AxisAlignedBB(5/16D, 0.0D,  5/16D, 11/16D, 11/16D, 11/16D);
    
    static {
    	bounds = new HashMap<EnumFacing, AxisAlignedBB>();
    	connectedBounds = new HashMap<EnumFacing, AxisAlignedBB>();
    	bounds.put(EnumFacing.EAST,  EAST_AABB);
    	bounds.put(EnumFacing.WEST,  WEST_AABB);
    	bounds.put(EnumFacing.NORTH, NORTH_AABB);
    	bounds.put(EnumFacing.SOUTH, SOUTH_AABB);
    	bounds.put(EnumFacing.UP,    UP_AABB);
    	bounds.put(EnumFacing.DOWN,  DOWN_AABB);

    	for(EnumFacing facing : EnumFacing.VALUES) {
    		AxisAlignedBB box = bounds.get(facing);
    		AxisAlignedBB expandedBox = new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    		expandedBox.expand(5 * facing.getXOffset(), 5 * facing.getYOffset(), 5 * facing.getZOffset());
    		connectedBounds.put(facing, expandedBox);
    	}
    }
    
    
	public BlockBranch() {
		super(Material.WOOD);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
	}

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
    
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	super.breakBlock(worldIn, pos, state);
    	/*EnumFacing facing = state.getValue(FACING);
    	for(EnumFacing next : EnumFacing.VALUES) {
    		if(next == facing)
    			continue;
    		IBlockState neighbor = worldIn.getBlockState(pos.add(next.getDirectionVec()));
    		if(neighbor.getBlock() == this && neighbor.getValue(FACING) == next.getOpposite())
    			worldIn.destroyBlock(pos.add(next.getDirectionVec()), true);
    	}*/
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!this.canSurviveAt(worldIn, pos))
        {
            worldIn.destroyBlock(pos, true);
        }
    }
    
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!this.canSurviveAt(worldIn, pos))
        {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    public boolean canSurviveAt(World worldIn, BlockPos pos)
    {
		IBlockState state = worldIn.getBlockState(pos);
    	EnumFacing facing = state.getValue(FACING);
		IBlockState neighbor = worldIn.getBlockState(pos.add(facing.getDirectionVec()));
    	
		if(neighbor.getMaterial() == Material.WOOD) {
			return true;
		}
		
		return false;
    }
    
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return BlockAtumPlank.getStick(WoodType.DEADWOOD);
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }
    
    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
    	EnumFacing facing = state.getValue(FACING);
    	
    	
    	super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
    	EnumFacing facing = state.getValue(FACING);
    	
    	IBlockState neighbor = source.getBlockState(pos.add(facing.getDirectionVec()));
    	if(neighbor.getBlock() == this) {
    		AxisAlignedBB box = connectedBounds.get(facing);
    		AxisAlignedBB expandedBox = new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    		return expandedBox.expand(5/16D * facing.getXOffset(), 5/16D * facing.getYOffset(), 5/16D * facing.getZOffset());
    	} else {
    		return bounds.get(facing);    		
    	}
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing.getOpposite());
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
    	return this.getDefaultState().withProperty(FACING, EnumFacing.UP.VALUES[meta]);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {    	
        EnumFacing enumFacing = (EnumFacing)state.getValue(FACING);
        return enumFacing.ordinal();
    }


    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, NORTH, SOUTH, EAST, WEST, UP, DOWN});
    }
    
    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        EnumFacing enumFacing = (EnumFacing)state.getValue(FACING);
        return state.withProperty(NORTH, enumFacing != EnumFacing.NORTH && shouldConnect(EnumFacing.NORTH, worldIn, pos))
                    .withProperty(EAST,  enumFacing != EnumFacing.EAST && shouldConnect(EnumFacing.EAST, worldIn, pos))
                    .withProperty(SOUTH, enumFacing != EnumFacing.SOUTH && shouldConnect(EnumFacing.SOUTH, worldIn, pos))
                    .withProperty(WEST,  enumFacing != EnumFacing.WEST && shouldConnect(EnumFacing.WEST, worldIn, pos))
                    .withProperty(UP,  enumFacing != EnumFacing.UP && shouldConnect(EnumFacing.UP, worldIn, pos))
                    .withProperty(DOWN,  enumFacing != EnumFacing.DOWN && shouldConnect(EnumFacing.DOWN, worldIn, pos));
    }
    
    public boolean shouldConnect(EnumFacing direction, IBlockAccess worldIn, BlockPos pos) {
    	
    	IBlockState neighborState = worldIn.getBlockState(pos.add(direction.getDirectionVec()));
    	if(neighborState.getBlock() == this) {
    		return neighborState.getValue(FACING) == direction.getOpposite();
    	}
    			
    	return false;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case CLOCKWISE_180:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
            default:
                return super.withMirror(state, mirrorIn);
        }
    }
}
