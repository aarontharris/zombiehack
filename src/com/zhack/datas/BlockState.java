package com.zhack.datas;

public class BlockState {

	public static final int NUM_DATA = 1;
	private static final int ID_BLOCKSTATE_TYPE = 0;

	private short[] data;

	public BlockState(short[] data) {
		this.data = data;
	}

	public BlockType getBlockType() {
		return readBlockType(data);
	}

	public void setBlockType(BlockType type) {
		writeBlockType(data, type);
	}

	public static BlockType readBlockType(short[] data) {
		BlockType out = BlockType.getById(data[ID_BLOCKSTATE_TYPE]);
		return out;
	}

	public static void writeBlockType(short[] data, BlockType type) {
		data[ID_BLOCKSTATE_TYPE] = type.getId();
	}

}
