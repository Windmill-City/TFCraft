package TFC.WorldGen.Generators;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import TFC.Blocks.Flora.BlockTallSeaGrassStill;
import TFC.Core.TFC_Core;

public class WorldGenSeaGrass extends WorldGenerator
{
	/** The ID of the plant block used in this plant generator. */
	private Block plantBlock;
	private boolean isSwamp;

	public WorldGenSeaGrass(Block par1,boolean isSwamp)
	{
		this.plantBlock = par1;
		this.isSwamp = isSwamp;
	}

	public boolean generate(World par1World, Random par2Random, int x, int y, int z)
	{
		int n = isSwamp?25:10;
		for (int var6 = 0; var6 < n; ++var6)
		{
			int var7 = x + (par2Random.nextInt(8) + par2Random.nextInt(4)) - (par2Random.nextInt(3) + par2Random.nextInt(2));
			int var8 = y + par2Random.nextInt(4) - par2Random.nextInt(4);
			int var9 = z + (par2Random.nextInt(8) + par2Random.nextInt(4)) - (par2Random.nextInt(3) + par2Random.nextInt(2));

			if (par1World.isAirBlock(var7, var8, var9))
			{
				//How far underwater are we going
				int depthCounter = 0;
				//Effectively makes sea grass grow less frequently as depth increases beyond 6 m.
				boolean randomTooDeepFlag = false;
				//travel down until a solid surface is reached
				while(var8 > 0 && TFC_Core.isWater(par1World.getBlock(var7, --var8, var9)) && !randomTooDeepFlag)
				{
					depthCounter++;
					if(depthCounter >= 6)
					{
						//If depthCounter reaches 11, automatically prevents plants from growing
						randomTooDeepFlag = (par2Random.nextInt(12 - depthCounter)==0);
					}
				}
				var8++;
				if(!randomTooDeepFlag && depthCounter >0 && ((BlockTallSeaGrassStill)this.plantBlock).canBlockStay(par1World, var7, var8, var9))
				{
					par1World.setBlock(var7, var8, var9, this.plantBlock, 0, 1);
					//Gravelly areas will spawn fewer plants
					if(TFC_Core.isGravel(par1World.getBlock(var7, var8-1, var9)))
						n--;
				}
			}
		}

		return true;
	}
}