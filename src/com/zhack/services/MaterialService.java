package com.zhack.services;

import java.util.HashMap;
import java.util.Map;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;

public class MaterialService {
	private static final String MAT_UNSHADED = "Common/MatDefs/Misc/Unshaded.j3md";
	private static final String TEX_STONE = "Textures/stone.png";
	private static final String TEX_DIRT = "Textures/tex_dirt01.png";
        private static final String TEX_COBBLESTONE = "Textures/cobblestone.png";
	private static final String TEX_CURSORBOX = "Textures/cursorbox.png";
	private static final String TEX_CROSSHAIR = "Textures/crosshair.png";

	public static enum MaterialType {
		WHITE(false, MAT_UNSHADED, ColorRGBA.White), //
		CURSORBOX(true, MAT_UNSHADED, TEX_CURSORBOX), //
		CROSSHAIR(true, MAT_UNSHADED, TEX_CROSSHAIR), //
		STONE(false, MAT_UNSHADED, TEX_STONE), //
		DIRT(false, MAT_UNSHADED, TEX_DIRT), //
                COBBLESTONE(false, MAT_UNSHADED, TEX_COBBLESTONE), //
		;

		private String matDefFile;
		private String textureFile;
		private ColorRGBA color;
		private boolean hasAlpha;

		MaterialType(boolean hasAlpha, String matDefFile, ColorRGBA color) {
			this.hasAlpha = hasAlpha;
			this.matDefFile = matDefFile;
			this.color = color;
		}

		MaterialType(boolean hasAlpha, String matDefFile, String textureFile) {
			this.hasAlpha = hasAlpha;
			this.matDefFile = matDefFile;
			this.textureFile = textureFile;
		}
	}

	private static final MaterialService self = new MaterialService();

	public static MaterialService getInstance() {
		return self;
	}

	private Map<MaterialType, Material> materialsMap = new HashMap<MaterialType, Material>();

	private MaterialService() {
	}

	public void init(AssetManager assetManager) {
		Map<String, Texture> textures = new HashMap<String, Texture>();

		for (MaterialType mat : MaterialType.values()) {
			Material m = new Material(assetManager, mat.matDefFile);
			if (mat.color != null) {
				m.setColor("Color", mat.color);
			}
			if (mat.textureFile != null) {
				Texture t = textures.get(mat.textureFile);
				if (t == null) {
					t = assetManager.loadTexture(mat.textureFile);
					t.setMagFilter(MagFilter.Nearest); // maintain that pixel look even when scaled
					textures.put(mat.textureFile, t);
				}
				if (t != null) {
					m.setTexture("ColorMap", t);
					if (mat.hasAlpha) {
						m.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
					}
				}
			}
			materialsMap.put(mat, m);
		}
	}

	public Material getMaterial(MaterialType mat) {
		return materialsMap.get(mat);
	}

}
