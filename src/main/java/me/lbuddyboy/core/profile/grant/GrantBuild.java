package me.lbuddyboy.core.profile.grant;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.lbuddyboy.core.rank.Rank;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:16 AM
 * lCore / me.lbuddyboy.core.profile.grant
 */

@AllArgsConstructor
@Data
public class GrantBuild {

	private UUID sender;
	private UUID target;
	private Rank rank;
	private String time;
	private String reason;

}
