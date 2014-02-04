package com.zhack.datas;

import com.jme3.math.Vector3f;
import com.zhack.util.Pool;

public class Units {
	
	/**
	 * <pre>
	 * World Units are absolute.  The center of the world is 0,100,0
	 * 
	 * A sector is only measured by x,z -- there is no height as one sector spans from top to bottom of the world.
	 * A sector at 0,0 is centered in the world.
	 * A sector at 1,1 is centered at 200,200 in World Units.
	 * 
	 * A chunk is 
	 * </pre>
	 */
	
	public static final int UNIT = 1; // 1
	public static final int CHUNK_UNIT = Chunk.CHUNK_DIAMETER * UNIT; // 10 world units
	public static final int SECTOR_UNIT = Sector.SEC_DIAMETER * CHUNK_UNIT; // 200 world units
	
	public static final int convertWorldUnitsToChunkUnitsX( int worldX ) {
		return worldX / CHUNK_UNIT;
	}
	
	public static final int convertWorldUnitsToChunkUnitsY( int worldY ) {
		return worldY / CHUNK_UNIT;
	}
	
	public static final int convertWorldUnitsToSectorUnitsX( int worldX ) {
		return worldX / SECTOR_UNIT;
	}
	
	public static final int convertWorldUnitsToSectorUnitsY( int worldY ) {
		return worldY / SECTOR_UNIT;
	}
	
	public static final int convertChunkUnitsToWorldUnitsX( int chunkX ) {
		return chunkX * CHUNK_UNIT;
	}
	
	public static final int convertChunkUnitsToWorldUnitsY( int chunkY ) {
		return chunkY * CHUNK_UNIT;
	}
	
	public static final int convertSectorUnitsToWorldUnitsX( int sectorX ) {
		return sectorX * SECTOR_UNIT;
	}
	
	public static final int convertSectorUnitsToWorldUnitsY( int sectorY ) {
		return sectorY * SECTOR_UNIT;
	}
	
	public static final int convertChunkUnitsToSectorUnitsX ( int chunkX ) {
		return (chunkX * CHUNK_UNIT) / SECTOR_UNIT;
	}
	
	public static final int convertChunkUnitsToSectorUnitsY ( int chunkY ) {
		return (chunkY * CHUNK_UNIT) / SECTOR_UNIT;
	}
	
	public static final int convertSectorUnitsToChunkUnitsX ( int sectorX ) {
		return (sectorX * SECTOR_UNIT) / CHUNK_UNIT;
	}
	
	public static final int convertSectorUnitsToChunkUnitsY ( int sectorY ) {
		return (sectorY * SECTOR_UNIT) / CHUNK_UNIT;
	}
	

	/** Return b's distance from a */
	public static final float distance( Vector3f a, Vector3f b ) {
		return a.distance(b);
	}
	
}
