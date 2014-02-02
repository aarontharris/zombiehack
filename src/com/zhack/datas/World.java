package com.zhack.datas;

public class World {
	public static final int WORLD_SEED = 12345; // I have the same combination on my matched luggage
	
	/** Generate a new chunk at the given chunk coordinates */
	public Chunk generateChunk( int chunkx, int chunky ) {
		Chunk chunk = new Chunk( 0, 0, 0 );
		return chunk;
	}

}
