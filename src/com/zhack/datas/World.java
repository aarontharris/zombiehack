package com.zhack.datas;

import java.util.HashMap;
import java.util.Map;

import com.zhack.BaseObject;

public class World extends BaseObject {
	public static final int WORLD_SEED = 12345; // I have the same combination on my matched luggage
	private static World self = new World();

	public static World getInstance() {
		return self;
	}

	private Map<String, Sector> sectors;

	private World() {
		self = this;
		sectors = new HashMap<String, Sector>();
	}

	public void update() {
		for (Sector s : sectors.values()) {
			s.update();
		}
	}

	// TODO: need to cleanup unoccupied sectors
	public Sector getSectorAtWorldPos(int x, int y, int z) {
		int sX = toSectorPos(x);
		int sY = toSectorPos(y);
		int sZ = toSectorPos(z);
		String key = toSectorKey(sX, sY, sZ);
		Sector out = sectors.get(key);
		if (out == null) {
			out = new Sector(sX, sY, sZ);
			// out.generate();
			sectors.put(key, out);
		}
		return out;
	}

	public static int toSectorPos(int worldPos) {
		int dia = Sector.SEC_DIAMETER * Chunk.CHUNK_DIAMETER;
		if (worldPos >= 0) {
			return (worldPos / dia) * dia;
		}
		// Less than zero
		return (((worldPos + 1) / dia) * dia) - dia;
	}

	private String toSectorKey(int x, int y, int z) {
		return x + "_" + y + "_" + z;
	}

	/**
	 * x,y,z is used to indicate the position of the chunk you want where they may only be -1, 0, 1 each.<br>
	 * 0,0,0 would be origin.<br>
	 * -1,0,0 would be left.<br>
	 * 1,0,0 would be right.<br>
	 * 0,0,-1 would be forward. // yes forward is -1<br>
	 * 0,0,1 would be backward.<br>
	 * etc
	 * 
	 * @param origin
	 * @return
	 */
	public Chunk getAdjacentChunksOrdered(Chunk origin, int x, int y, int z) {
		int ox = origin.getX();
		int oy = origin.getY();
		int oz = origin.getZ();
		int cx = ox + Chunk.CHUNK_DIAMETER * x;
		int cy = oy + Chunk.CHUNK_DIAMETER * y;
		int cz = oz + Chunk.CHUNK_DIAMETER * z;
		return getChunkAtWorldPos(cx, cy, cz);
	}

	public Chunk getChunkAtWorldPos(int x, int y, int z) {
		return getSectorAtWorldPos(x, y, z).getChunkAtWorldPos(x, y, z);
	}

	public short[] getBlockDataAtWorldPos(int x, int y, int z) {
		return getChunkAtWorldPos(x, y, z).getBlockDataAtWorldPos(x, y, z);
	}

	public String describePos(int x, int y, int z) {
		Sector sector = getSectorAtWorldPos(x, y, z);
		Chunk chunk = getChunkAtWorldPos(x, y, z);
		return String.format("World: %s,%s,%s, Sector: %s,%s,%s, Chunk: %s,%s,%s, Block %s,%s,%s", x, y, z, sector.getX(), sector.getY(),
				sector.getZ(), chunk.getX(), chunk.getY(), chunk.getZ(), Chunk.toBlockPos(x, chunk.getX()), Chunk.toBlockPos(y, chunk.getY()),
				Chunk.toBlockPos(z, chunk.getZ()));
	}
}
