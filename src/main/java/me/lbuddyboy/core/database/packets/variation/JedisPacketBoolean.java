package me.lbuddyboy.core.database.packets.variation;

import me.lbuddyboy.libraries.redis.JedisPacket;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 12:20 PM
 * LBuddyBoy Development / me.lbuddyboy.core.database.type
 */
public interface JedisPacketBoolean extends JedisPacket {

	boolean value();

	default boolean response() {
		return value();
	}

}
