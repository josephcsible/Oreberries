package josephcsible.oreberries.config;

import josephcsible.oreberries.OreberriesMod;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

@Config(modid = OreberriesMod.MODID)
public class GeneralConfig {
	// TODO avoid duplication between @Comment and en_us.lang
	@RangeDouble(min = 0.0D, max = 1.0D)
	@LangKey("config.oreberries.tickGrowthChance")
	@Comment("The chance that a random block tick will cause an oreberry bush to advance by one growth state.")
	public static double tickGrowthChance = 0.05D;

	@RangeDouble(min = 0.0D, max = 1.0D)
	@LangKey("config.oreberries.bonemealGrowthChance")
	@Comment("The chance that an application of bone meal will cause an oreberry bush to advance by one growth state. A value of zero disables the use of bone meal on oreberry bushes.")
	public static double bonemealGrowthChance = 0.0D;

	@RequiresMcRestart
	@LangKey("config.oreberries.enableVillager")
	@Comment("Whether to enable a Tinker villager who sells oreberry bushes.")
	public static boolean enableVillager = true;

	@RequiresMcRestart
	@LangKey("config.oreberries.rewriteJson")
	@Comment("Whether to rewrite the JSON configuration after reading it, to clean and standardize its formatting.")
	public static boolean rewriteJson = false;
}
