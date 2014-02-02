package com.zhack.datas;

/**
 * Represents 20 x 20 x 20 chunks
 */
public class Sector {
	public static final int SEC_DIAMETER = 20;
	private static final int UNIT_PER_SECTOR_COORDINATE = Chunk.CHUNK_DIAMETER * SEC_DIAMETER;
	
	private int x;
	private int y;

	private Chunk[][][] chunks;

	public void generate() {
		chunks = new Chunk[SEC_DIAMETER][SEC_DIAMETER][SEC_DIAMETER];
		for (int x = 0; x < SEC_DIAMETER; x++) {
			for (int y = 0; y < SEC_DIAMETER; y++) {
				for (int z = 0; z < SEC_DIAMETER; z++) {
					chunks[x][y][z] = new Chunk( 0, 0, 0 );
				}
			}
		}
	}

}
