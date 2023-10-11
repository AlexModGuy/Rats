package com.github.alexthe666.rats.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

	public final ForgeConfigSpec.BooleanValue plagueHearts;
	public final ForgeConfigSpec.BooleanValue synesthesiaShader;
	public final ForgeConfigSpec.BooleanValue funnyFluteSound;
	public final ForgeConfigSpec.BooleanValue ratFartNoises;
	public final ForgeConfigSpec.BooleanValue ratGodGlint;
	public final ForgeConfigSpec.BooleanValue ratAngelGlint;


	public ClientConfig(final ForgeConfigSpec.Builder builder) {
		builder.push("general");
		this.plagueHearts = buildBoolean(builder, "Plague Heart Overlay", true, "True if player UI has plague hearts render when the effect is active.");
		this.synesthesiaShader = buildBoolean(builder, "Synesthesia Shader", true, "If true, being under the effect of Synesthesia will tint your screen yellow.");
		this.funnyFluteSound = buildBoolean(builder, "Rat Flutes play Spongebob flute sound", true, "True if rats flutes can occasionally play the flute sound at the end of the Spongebob intro. Might be smart to disable this while streaming.");
		this.ratFartNoises = buildBoolean(builder, "Rat Fart Noises", true, "True if rats should rarely make a funny noise when creating rat nuggets.");
		this.ratGodGlint = buildBoolean(builder, "Rat God Upgrade Glint", true, "If true, rats that have the rat upgrade: god will display with an enchantment overlay.");
		this.ratAngelGlint = buildBoolean(builder, "Rat Angel Upgrade Glint", true, "If true, rats that have the rat upgrade: angel will display with a white overlay when respawning.");
	}

	private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, boolean defaultValue, String comment) {
		return builder.comment(comment).translation(name).define(name, defaultValue);
	}
}
