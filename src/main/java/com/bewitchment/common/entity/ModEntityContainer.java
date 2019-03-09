package com.bewitchment.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;

import java.util.List;

/**
 * @author its_meow
 *         <p>
 *         Mar 8, 2019
 */
public class ModEntityContainer {

	public Class<? extends Entity> entityClazz;
	public String entityName;
	public EnumCreatureType type;
	public int eggColorSolid;
	public int eggColorSpot;
	public int weight;
	public int minGroup;
	public int maxGroup;
	public Biome[] spawnBiomes = {};
	public boolean doRegister = true;
	public boolean doSpawning = true;

	public ModEntityContainer(Class<? extends Entity> EntityClass, String entityNameIn, EnumCreatureType type, int solidColorIn, int spotColorIn, int prob, int min, int max, List<Biome> biomes) {
		this.entityClazz = EntityClass;
		this.entityName = entityNameIn;
		this.eggColorSolid = solidColorIn;
		this.eggColorSpot = spotColorIn;
		this.weight = prob;
		this.minGroup = min;
		this.maxGroup = max;
		this.type = type;


		// Convert biomes to single array

		try {
			this.spawnBiomes = biomes.toArray(this.spawnBiomes);
		} catch (NullPointerException e) {
			this.spawnBiomes = new Biome[0];
		}
	}

}
