package com.zhack.datas;

import com.jme3.math.Vector3f;

public enum Facing {

	N(Const.Z, Const.Z, Const.N), //
	NU(Const.Z, Const.U, Const.N), //
	ND(Const.Z, Const.D, Const.N), //
	NE(Const.E, Const.Z, Const.N), //
	NEU(Const.E, Const.U, Const.N), //
	NED(Const.E, Const.D, Const.N), //
	E(Const.E, Const.Z, Const.Z), //
	EU(Const.E, Const.U, Const.Z), //
	ED(Const.E, Const.D, Const.Z), //
	SE(Const.E, Const.Z, Const.S), //
	SEU(Const.E, Const.U, Const.S), //
	SED(Const.E, Const.D, Const.S), //
	S(Const.Z, Const.Z, Const.S), //
	SU(Const.Z, Const.U, Const.S), //
	SD(Const.Z, Const.D, Const.S), //
	SW(Const.W, Const.Z, Const.S), //
	SWU(Const.W, Const.U, Const.S), //
	SWD(Const.W, Const.D, Const.S), //
	W(Const.W, Const.Z, Const.Z), //
	WU(Const.W, Const.U, Const.Z), //
	WD(Const.W, Const.D, Const.Z), //
	NW(Const.W, Const.Z, Const.N), //
	NWU(Const.W, Const.U, Const.N), //
	NWD(Const.W, Const.D, Const.N), //
	;

	private int x;
	private int y;
	private int z;

	Facing(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
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

	public static final Facing fromVector(Vector3f vector) {
		return fromVector(vector.x, vector.y, vector.z);
	}

	public static final Facing fromVector(float x, float y, float z) {

		float normalizedX = 0f;
		float normalizedY = 0f;
		float normalizedZ = 0f;
		{ // normalize
			float mag = (float) Math.sqrt((x * x) + (y * y) + (z * z));
			float absMag = Math.abs(mag);
			normalizedX = x / absMag;
			normalizedY = y / absMag;
			normalizedZ = z / absMag;
		}
		

		return null;
	}

}
