package com.github.alexthe666.rats.client.render;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.events.ModClientEvents;
import com.github.alexthe666.rats.client.render.block.RatlantisPortalRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.List;

public class RatsRenderType extends RenderType {

	protected static final RenderStateShard.ShaderStateShard RENDERTYPE_RATLANTIS_PORTAL_SHADER = new RenderStateShard.ShaderStateShard(ModClientEvents::getRendertypeRatlantisPortalShader);
	private static final RenderType RATLANTIS_PORTAL = create("ratlantis_portal", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_RATLANTIS_PORTAL_SHADER).setTextureState(RenderStateShard.MultiTextureStateShard.builder().add(RatlantisPortalRenderer.PORTAL_BG, false, false).add(RatlantisPortalRenderer.PORTAL_FG, false, false).build()).createCompositeState(false));

	protected static final RenderStateShard.TexturingStateShard RAINBOW_GLINT_TEXTURING = new RenderStateShard.TexturingStateShard("rainbow_glint_texturing", RatsRenderType::setupRainbowRendering, RenderSystem::resetTextureMatrix);
	private static final RenderType ACE_GLINT = create("ace_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/ace_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
	private static final RenderType AGENDER_GLINT = create("agender_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/agender_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
	private static final RenderType ARO_GLINT = create("aro_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/aro_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
	private static final RenderType BI_GLINT = create("bi_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/bi_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
	private static final RenderType ENBY_GLINT = create("enby_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/enby_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
	private static final RenderType GAY_GLINT = create("gay_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/gay_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
	private static final RenderType GENDERFLUID_GLINT = create("genderfluid_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/genderfluid_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
	private static final RenderType LESBIAN_GLINT = create("lesbian_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/lesbian_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
	private static final RenderType PAN_GLINT = create("pan_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/pan_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
	private static final RenderType RAINBOW_GLINT = create("rainbow_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/rainbow_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
	private static final RenderType TRANS_GLINT = create("trans_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/trans_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));

	private static final RenderType PISS_GLINT = create("piss_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/piss_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
	private static final RenderType UNPLEASANT_GLINT = create("unpleasant_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/special_dyes/unpleasant_glint.png"), true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(RAINBOW_GLINT_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));

	private static final RenderType GREEN_ENTITY_GLINT = create("green_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/green_glint.png"), true, false)).setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
	private static final RenderType YELLOW_ENTITY_GLINT = create("yellow_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/yellow_glint.png"), true, false)).setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
	private static final RenderType WHITE_ENTITY_GLINT = create("white_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/white_glint.png"), true, false)).setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
	private static final RenderType GOLD_ENTITY_GLINT = create("gold_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(RatsMod.MODID, "textures/misc/gold_glint.png"), true, false)).setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));

	public RatsRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumble, boolean sort, Runnable builder1, Runnable builder2) {
		super(name, format, mode, bufferSize, affectsCrumble, sort, builder1, builder2);
	}

	public static RenderType getYellowGlint() {
		return YELLOW_ENTITY_GLINT;
	}

	public static RenderType getGreenGlint() {
		return GREEN_ENTITY_GLINT;
	}

	public static RenderType getWhiteGlint() {
		return WHITE_ENTITY_GLINT;
	}

	public static RenderType getGoldGlint() {
		return GOLD_ENTITY_GLINT;
	}

	public static RenderType getRatlantisPortal() {
		return RATLANTIS_PORTAL;
	}

	public static RenderType getRainbowGlint() {
		return RAINBOW_GLINT;
	}

	public static RenderType getGlowingTranslucent(ResourceLocation location) {
		RenderStateShard.TextureStateShard texture = new RenderStateShard.TextureStateShard(location, false, true);
		return create("glowing_translucent", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false,
				RenderType.CompositeState.builder().setTextureState(texture)
						.setShaderState(RenderStateShard.RENDERTYPE_ENERGY_SWIRL_SHADER)
						.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
						.setOutputState(TRANSLUCENT_TARGET)
						.setWriteMaskState(COLOR_DEPTH_WRITE)
						.createCompositeState(true)
		);
	}

	private static void setupRainbowRendering() {
		long i = Util.getMillis() * 8L;
		float f = (float) (i % 10000L) / 10000.0F;
		Matrix4f matrix4f = new Matrix4f().translation(0.0F, f, 0.0F);
		matrix4f.scale(0.16F);
		RenderSystem.setTextureMatrix(matrix4f);
	}

	public enum GlintType {
		AGENDER(AGENDER_GLINT, true, "agender"),
		AROMANTIC(ARO_GLINT, true, "aromantic", "aro"),
		ASEXUAL(ACE_GLINT, true, "asexual", "ace"),
		BISEXUAL(BI_GLINT, true, "bisexual", "bi"),
		GAY(GAY_GLINT, true, "gay", "mlm"),
		GENDERFLUID(GENDERFLUID_GLINT, true, "genderfluid", "fluid"),
		NONBINARY(ENBY_GLINT, true, "non-binary", "nonbinary", "enby", "nb"),
		LESBIAN(LESBIAN_GLINT, true, "lesbian", "wlw"),
		PANSEXUAL(PAN_GLINT, true, "pansexual", "pan"),
		TRANSGENDER(TRANS_GLINT, true, "transgender", "trans", "tratsgender"),

		PISS(PISS_GLINT, false, "piss"),
		UNPLEASANT(UNPLEASANT_GLINT, false, "unpleasant");

		private final RenderType type;
		private final boolean changesTexture;
		private final List<String> keywords;

		GlintType(RenderType type, boolean changesItemTex, String... matchingKeywords) {
			this.type = type;
			this.changesTexture = changesItemTex;
			this.keywords = Arrays.stream(matchingKeywords).toList();
		}

		public boolean changesItemTexture() {
			return this.changesTexture;
		}

		public RenderType getRenderType() {
			return this.type;
		}

		@Nullable
		public static RenderType getRenderTypeBasedOnKeyword(String word) {
			for (GlintType type : GlintType.values()) {
				for (String possibleWord : type.getKeywords()) {
					if (possibleWord.equalsIgnoreCase(word)) {
						return type.getRenderType();
					}
				}
			}
			return null;
		}

		@Nullable
		public static GlintType getGlintBasedOnKeyword(String word) {
			for (GlintType type : GlintType.values()) {
				for (String possibleWord : type.getKeywords()) {
					if (possibleWord.equalsIgnoreCase(word)) {
						return type;
					}
				}
			}
			return null;
		}

		public List<String> getKeywords() {
			return this.keywords;
		}
	}
}
