package com.zhack.datas;

import com.zhack.BaseObject;

/**
 * Represents 20 x 20 x 20 chunks
 */
public class Sector extends BaseObject {
	public static final int SEC_DIAMETER = 20;
	private static final int UNIT_PER_SECTOR_COORDINATE = Chunk.CHUNK_DIAMETER * SEC_DIAMETER;

	private final int x;
	private final int y;
	private final int z;

	private Chunk[][][] chunks;

	public Sector(int x, int y, int z) {
		chunks = new Chunk[SEC_DIAMETER][SEC_DIAMETER][SEC_DIAMETER];
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void update() {
		for (int x = 0; x < SEC_DIAMETER; x++) {
			for (int y = 0; y < SEC_DIAMETER; y++) {
				for (int z = 0; z < SEC_DIAMETER; z++) {
					Chunk chunk = chunks[x][y][z];
					if (chunk != null) {
						chunk.update();
					}
				}
			}
		}
	}

	public void generate() {
		for (int x = 0; x < SEC_DIAMETER; x++) {
			for (int y = 0; y < SEC_DIAMETER; y++) {
				for (int z = 0; z < SEC_DIAMETER; z++) {
					int dia = Chunk.CHUNK_DIAMETER;
					int wx = this.x + x * dia;
					int wy = this.y + y * dia;
					int wz = this.z + z * dia;
					chunks[x][y][z] = new Chunk(wx, wy, wz);
				}
			}
		}
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

	public static int toChunkPos(int worldPos) {
		// | | |
		// 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0

		int dia = Chunk.CHUNK_DIAMETER;
		if (worldPos >= 0) {
			return (worldPos / dia) * dia;
		}
		// Less than zero
		return (((worldPos + 1) / dia) * dia) - dia;
	}

	public Chunk getChunkAtWorldPos(int x, int y, int z) {
		int dia = Chunk.CHUNK_DIAMETER;
		int cx = toChunkPos(x - this.x) / dia;
		int cy = toChunkPos(y - this.y) / dia;
		int cz = toChunkPos(z - this.z) / dia;

		// log().debug("getChunkAtWorldPos(%s,%s,%s) -> (%s,%s,%s)", x, y, z, cx, cy, cz);

		// return chunks[x - this.x][y - this.y][z - this.z];

		if (chunks[cx][cy][cz] == null) {
			chunks[cx][cy][cz] = new Chunk(toChunkPos(x), toChunkPos(y), toChunkPos(z));
			chunks[cx][cy][cz].generate(World.WORLD_SEED);
		}

		return chunks[cx][cy][cz];
	}

	public short[] getBlockDataAtWorldPos(int x, int y, int z) {
		return getChunkAtWorldPos(x, y, z).getBlockDataAtWorldPos(x, y, z);
	}
}
