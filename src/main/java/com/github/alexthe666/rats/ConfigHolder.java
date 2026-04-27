package com.github.alexthe666.rats;

import com.github.alexthe666.rats.client.ClientConfig;
import com.github.alexthe666.rats.server.ServerConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigHolder {

	public static final ModConfigSpec CLIENT_SPEC;
	public static final ModConfigSpec SERVER_SPEC;
	static final ClientConfig CLIENT;
	static final ServerConfig SERVER;

	static {
		{
			final Pair<ClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
			CLIENT = specPair.getLeft();
			CLIENT_SPEC = specPair.getRight();
		}
		{
			final Pair<ServerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ServerConfig::new);
			SERVER = specPair.getLeft();
			SERVER_SPEC = specPair.getRight();
		}
	}
}