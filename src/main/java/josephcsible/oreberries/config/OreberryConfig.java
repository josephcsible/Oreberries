package josephcsible.oreberries.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import josephcsible.oreberries.item.ItemNugget;
import net.minecraft.world.World;

public class OreberryConfig {
	public final String name;
	public final String bushName;
	public final String berryName;
	public final String color;
	public final @Nullable String tooltip;
	public final List<String> oredictNames;

	public final @Nullable NuggetConfig smeltingResultNugget;
	public final @Nullable String smeltingResultString;

	public final @Nullable String special;
	public final boolean growsInLight;
	public final boolean tradeable;
	public final int sizeChance;
	public final int rarity;
	public final int density;

	public final int minHeight;
	protected final @Nullable Integer preferredHeight;
	protected final @Nullable Integer maxHeight;

	static String firstUpper(String s) {
		if(s.isEmpty()) return "";
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	public OreberryConfig(String name, JsonObject json) {
		Gson gson = new Gson();
		this.name = name;

		JsonElement jsonBushName = json.get("bushName");
		if(jsonBushName == null) {
			bushName = getDefaultBushName();
		} else {
			bushName = jsonBushName.getAsString();
		}

		JsonElement jsonBerryName = json.get("berryName");
		if(jsonBerryName == null) {
			berryName = getDefaultBerryName();
		} else {
			berryName = jsonBerryName.getAsString();
		}

		JsonElement jsonColor = json.get("color");
		if(jsonColor == null) {
			color = getDefaultColor();
		} else {
			color = jsonColor.getAsString();
		}

		JsonElement jsonTooltip = json.get("tooltip");
		if(jsonTooltip == null) {
			tooltip = null;
		} else {
			tooltip = jsonTooltip.getAsString();
		}

		JsonElement jsonOredictNames = json.get("oredictNames");
		if(jsonOredictNames == null) {
			oredictNames = getDefaultOredictNames();
		} else {
			oredictNames = Arrays.asList(gson.fromJson(jsonOredictNames, String[].class));
		}

		JsonElement jsonSmeltingResult = json.get("smeltingResult");
		if(jsonSmeltingResult == null) {
			smeltingResultNugget = null;
			smeltingResultString = getDefaultSmeltingResult();
		} else if(jsonSmeltingResult.isJsonNull()) {
			smeltingResultNugget = null;
			smeltingResultString = null;
		} else if(jsonSmeltingResult.isJsonObject()) {
			smeltingResultNugget = new NuggetConfig(this, (JsonObject) jsonSmeltingResult);
			smeltingResultString = ItemNugget.getFullName(name);
		} else {
			smeltingResultNugget = null;
			smeltingResultString = jsonSmeltingResult.getAsString();
		}

		JsonElement jsonSpecial = json.get("special");
		if(jsonSpecial == null) {
			special = null;
		} else {
			special = jsonSpecial.getAsString();
		}

		JsonElement jsonGrowsInLight = json.get("growsInLight");
		if(jsonGrowsInLight == null) {
			growsInLight = getDefaultGrowsInLight();
		} else {
			growsInLight = jsonGrowsInLight.getAsBoolean();
		}

		JsonElement jsonTradeable = json.get("tradeable");
		if(jsonTradeable == null) {
			tradeable = getDefaultTradeable();
		} else {
			tradeable = jsonTradeable.getAsBoolean();
		}

		JsonElement jsonSizeChance = json.get("sizeChance");
		if(jsonSizeChance == null) {
			sizeChance = getDefaultSizeChance();
		} else {
			sizeChance = jsonSizeChance.getAsInt();
		}

		JsonElement jsonRarity = json.get("rarity");
		if(jsonRarity == null) {
			rarity = getDefaultRarity();
		} else {
			rarity = jsonRarity.getAsInt();
		}

		JsonElement jsonDensity = json.get("density");
		if(jsonDensity == null) {
			density = getDefaultDensity();
		} else {
			density = jsonDensity.getAsInt();
		}

		JsonElement jsonMinHeight = json.get("minHeight");
		if(jsonMinHeight == null) {
			minHeight = getDefaultMinHeight();
		} else {
			minHeight = jsonMinHeight.getAsInt();
		}

		JsonElement jsonMaxHeight = json.get("maxHeight");
		if(jsonMaxHeight == null) {
			maxHeight = null;
		} else {
			maxHeight = jsonMaxHeight.getAsInt();
		}

		JsonElement jsonPreferredHeight = json.get("preferredHeight");
		if(jsonPreferredHeight == null) {
			preferredHeight = null;
		} else {
			preferredHeight = jsonPreferredHeight.getAsInt();
		}
	}

	@SuppressWarnings("deprecation")
	public String getDefaultBushName() {
		// TODO make this client-only and switch it to net.minecraft.client.resources.I18n
		return net.minecraft.util.text.translation.I18n.translateToLocalFormatted("item.oreberries.oreberry_bush.name", name);
	}

	@SuppressWarnings("deprecation")
	public String getDefaultBerryName() {
		// TODO make this client-only and switch it to net.minecraft.client.resources.I18n
		return net.minecraft.util.text.translation.I18n.translateToLocalFormatted("item.oreberries.oreberry.name", name);
	}

	public String getDefaultColor() {
		return "#FFFFFF";
	}

	public List<String> getDefaultOredictNames() {
		List<String> retval = new ArrayList<>();
		retval.add("nugget" + firstUpper(name));
		return retval;
	}

	public String getDefaultSmeltingResult() {
		return "nugget" + firstUpper(name);
	}

	public boolean getDefaultGrowsInLight() {
		return false;
	}

	public boolean getDefaultTradeable() {
		return true;
	}

	public int getDefaultSizeChance() {
		return 12;
	}

	public int getDefaultRarity() {
		return -1;
	}

	public int getDefaultDensity() {
		return 1;
	}

	public int getDefaultMinHeight() {
		return 0;
	}

	public int getPreferredHeight(World world) {
		if(preferredHeight == null) {
			return (minHeight + getMaxHeight(world))/2;
		}
		return preferredHeight;
	}

	public int getMaxHeight(World world) {
		if(maxHeight == null) return world.getSeaLevel();
		return maxHeight;
	}

	public JsonObject toJson() {
		Gson gson = new Gson();
		JsonObject json = new JsonObject();
		if(!bushName.equals(getDefaultBushName())) {
			json.addProperty("bushName", bushName);
		}
		if(!berryName.equals(getDefaultBerryName())) {
			json.addProperty("berryName", berryName);
		}
		if(!color.equals(getDefaultColor())) {
			json.addProperty("color", color);
		}
		if(tooltip != null) {
			json.addProperty("tooltip", tooltip);
		}
		if(!oredictNames.equals(getDefaultOredictNames())) {
			json.add("oredictNames", gson.toJsonTree(oredictNames));
		}

		if(smeltingResultNugget != null) {
			json.add("smeltingResult", smeltingResultNugget.toJson());
		} else if(!getDefaultSmeltingResult().equals(smeltingResultString)) { // flipped order of this test because smeltingResultString can be null
			json.addProperty("smeltingResult", smeltingResultString);
		}

		if(special != null) {
			json.addProperty("special", special);
		}
		if(growsInLight != getDefaultGrowsInLight()) {
			json.addProperty("growsInLight", growsInLight);
		}
		if(tradeable != getDefaultTradeable()) {
			json.addProperty("tradeable", tradeable);
		}
		if(sizeChance != getDefaultSizeChance()) {
			json.addProperty("sizeChance", sizeChance);
		}
		if(rarity != getDefaultRarity()) {
			json.addProperty("rarity", rarity);
		}
		if(density != getDefaultDensity()) {
			json.addProperty("density", density);
		}
		if(minHeight != getDefaultMinHeight()) {
			json.addProperty("minHeight", minHeight);
		}
		if(preferredHeight != null) {
			json.addProperty("preferredHeight", preferredHeight);
		}
		if(maxHeight != null) {
			json.addProperty("maxHeight", maxHeight);
		}

		return json;
	}
}
