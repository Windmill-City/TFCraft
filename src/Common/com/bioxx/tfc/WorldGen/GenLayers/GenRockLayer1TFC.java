package com.bioxx.tfc.WorldGen.GenLayers;

import net.minecraft.world.WorldType;

public abstract class GenRockLayer1TFC extends GenLayerTFC
{
	/** seed from World#getWorldSeed that is used in the LCG prng */
	private long worldGenSeed;

	/** parent GenLayer that was provided via the constructor */
	protected GenRockLayer1TFC parent;

	/**
	 * final part of the LCG prng that uses the chunk X, Z coords along with the other two seeds to generate
	 * pseudorandom numbers
	 */
	private long chunkSeed;

	/** base seed to the LCG prng provided via the constructor */
	private long baseSeed;

	/**
	 * the first array item is a linked list of the bioms, the second is the zoom function, the third is the same as the
	 * first.
	 */
	public static GenLayerTFC[] initializeAllBiomeGenerators(long par0, WorldType par2WorldType)
	{
		GenLayerIslandTFC var3 = new GenLayerIslandTFC(1L);
		GenLayerFuzzyZoomTFC var9 = new GenLayerFuzzyZoomTFC(2000L, var3);
		GenLayerAddIslandTFC var10 = new GenLayerAddIslandTFC(1L, var9);
		GenLayerZoomTFC var11 = new GenLayerZoomTFC(2001L, var10);
		var10 = new GenLayerAddIslandTFC(2L, var11);
		GenLayerAddSnowTFC var12 = new GenLayerAddSnowTFC(2L, var10);
		var11 = new GenLayerZoomTFC(2002L, var12);
		var10 = new GenLayerAddIslandTFC(3L, var11);
		var11 = new GenLayerZoomTFC(2003L, var10);
		var10 = new GenLayerAddIslandTFC(4L, var11);
		byte var4 = 5;

		GenLayerTFC var5 = (GenLayerTFC)GenLayerZoomTFC.magnify(1000L, var10, 0);
		GenLayerRiverInitTFC var13 = new GenLayerRiverInitTFC(100L, var5);
		var5 = (GenLayerTFC) GenLayerZoomTFC.magnify(1000L, var13, var4+2);
		GenLayerSmoothTFC var15 = new GenLayerSmoothTFC(1000L, var5);
		GenLayerTFC var6 = (GenLayerTFC) GenLayerZoomTFC.magnify(1000L, var10, 0);
		GenLayerRockTypes1 var17 = new GenLayerRockTypes1(200L, var6, par2WorldType,1);
		var6 = (GenLayerTFC) GenLayerZoomTFC.magnify(1000L, var17, 2);

		Object var18 = new GenLayerSmoothTFC(1000L, var6);
		for (int var7 = 0; var7 < var4; ++var7)
		{
			var18 = new GenLayerZoomTFC((long)(1000 + var7), (GenLayerTFC)var18);
			if (var7 == 0)
				var18 = new GenLayerAddIslandTFC(3L, (GenLayerTFC)var18);
		}

		GenLayerSmoothTFC var19 = new GenLayerSmoothTFC(1000L, (GenLayerTFC)var18);
		GenLayerVoronoiZoomTFC var8 = new GenLayerVoronoiZoomTFC(10L, var19);
		var19.initWorldGenSeed(par0);
		var8.initWorldGenSeed(par0);
		return new GenLayerTFC[] {var19, var8};
	}

	public GenRockLayer1TFC(long par1)
	{
		super(par1);
	}

	/**
	 * Initialize layer's local worldGenSeed based on its own baseSeed and the world's global seed (passed in as an
	 * argument).
	 */
	public void initWorldGenSeed(long par1)
	{
		worldGenSeed = par1;
		if (this.parent != null)
			parent.initWorldGenSeed(par1);

		worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		worldGenSeed += baseSeed;
		worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		worldGenSeed += baseSeed;
		worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		worldGenSeed += baseSeed;
	}

	/**
	 * Initialize layer's current chunkSeed based on the local worldGenSeed and the (x,z) chunk coordinates.
	 */
	public void initChunkSeed(long par1, long par3)
	{
		chunkSeed = worldGenSeed;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += par1;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += par3;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += par1;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += par3;
	}

	/**
	 * returns a LCG pseudo random number from [0, x). Args: int x
	 */
	protected int nextInt(int par1)
	{
		int var2 = (int)((this.chunkSeed >> 24) % (long)par1);
		if (var2 < 0)
			var2 += par1;

		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += worldGenSeed;
		return var2;
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
	 * amounts, or biomeList[] indices based on the particular GenLayer subclass.
	 */
	public abstract int[] getInts(int var1, int var2, int var3, int var4);
}
