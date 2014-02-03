package com.zhack.datas;

import java.util.Random;

import com.zhack.BaseObject;

public class Chunk extends BaseObject {
	public static final int CHUNK_DIAMETER = 10;

	private static final int NUM_DATA = 1;
	private static final int ID_BLOCKSTATE_TYPE = 0;

	/**
	 * <pre>
	 * [x][y][z][i] -- where xyz is the chunk coordinate and i is the data index.
	 * i=0 == block type
	 * i=1 == bitmask 1
	 *        -- Bit 1 0x0001 == TBD
	 * </pre>
	 */
	private short[][][][] chunkState;

	private int x;
	private int y;
	private int z;

	/**
	 * @param x
	 *            - X Coordinate within parent Sector
	 * @param y
	 *            - Y Coordinate within parent Sector
	 * @param z
	 *            - Z Coordinate within parent Sector
	 */
	public Chunk(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		chunkState = new short[CHUNK_DIAMETER][CHUNK_DIAMETER][CHUNK_DIAMETER][NUM_DATA];
	}

	public long toChunkSeed(long seed) {
		final long prime = 31;
		long result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result + seed;
	}

	public void generate(long seed) {
		Random rand = new Random(toChunkSeed(seed));

		for (short x = 0; x < CHUNK_DIAMETER; x++) {
			for (short y = 0; y < CHUNK_DIAMETER; y++) {
				for (short z = 0; z < CHUNK_DIAMETER; z++) {
					int r = rand.nextInt(1000);
					if (r < 1) { // 1 in 1000
						setBlockType(x, y, z, BlockType.AIR);
					} else if (r < 301) {
						setBlockType(x, y, z, BlockType.STONE);
					} else if (r < 601) {
						setBlockType(x, y, z, BlockType.COBBLESTONE);
					} else {
						setBlockType(x, y, z, BlockType.DIRT);
					}
				}
			}
		}

		setBlockType(0, 0, 0, BlockType.WHITE); // note our 0,0,0 point
	}

	public void setBlockType(int x, int y, int z, BlockType type) {
		chunkState[x][y][z][ID_BLOCKSTATE_TYPE] = type.getId();
	}

	public BlockType getBlockType(int x, int y, int z) {
		BlockType out = BlockType.getById(chunkState[x][y][z][ID_BLOCKSTATE_TYPE]);
		return out;
	}

	public int getChunkWidth() {
		return CHUNK_DIAMETER;
	}

	public int getChunkHeight() {
		return CHUNK_DIAMETER;
	}

	public int getChunkDepth() {
		return CHUNK_DIAMETER;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
	
	public int getLeftBoundary() {
		return getX() - CHUNK_DIAMETER;
	}

	public int getRightBoundary() {
		return getX() + CHUNK_DIAMETER;
	}

	public int getFrontBoundary() {
		return getZ() + CHUNK_DIAMETER;
	}

	public int getBackBoundary() {
		return getZ() - CHUNK_DIAMETER;
	}

	public int getTopBoundary() {
		return getY() + CHUNK_DIAMETER;
	}

	public int getBottomBoundary() {
		return getY() - CHUNK_DIAMETER;
	}
}
