package josephcsible.oreberries.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NuggetConfig {
	protected final OreberryConfig oreberry;
	public final String name;
	public final String color;
	public final List<String> oredictNames;
	public final List<String> ingotNames;

	public NuggetConfig(OreberryConfig oreberry, JsonObject json) {
		Gson gson = new Gson();
		this.oreberry = oreberry;

		JsonElement jsonName = json.get("name");
		if(jsonName == null) {
			name = getDefaultName();
		} else {
			name = jsonName.getAsString();
		}

		JsonElement jsonColor = json.get("color");
		if(jsonColor == null) {
			color = getDefaultColor();
		} else {
			color = jsonColor.getAsString();
		}

		JsonElement jsonOredictNames = json.get("oredictNames");
		if(jsonOredictNames == null) {
			oredictNames = getDefaultOredictNames();
		} else {
			oredictNames = Arrays.asList(gson.fromJson(jsonOredictNames, String[].class));
		}

		JsonElement jsonIngotNames = json.get("ingotNames");
		if(jsonIngotNames == null) {
			ingotNames = getDefaultIngotNames();
		} else {
			ingotNames = Arrays.asList(gson.fromJson(jsonIngotNames, String[].class));
		}
	}

	@SuppressWarnings("deprecation")
	protected String getDefaultName() {
		// TODO make this client-only and switch it to net.minecraft.client.resources.I18n
		return net.minecraft.util.text.translation.I18n.translateToLocalFormatted("item.oreberries.nugget.name", oreberry.name);
	}

	protected String getDefaultColor() {
		return oreberry.color;
	}

	protected List<String> getDefaultOredictNames() {
		return oreberry.oredictNames;
	}

	protected List<String> getDefaultIngotNames() {
		List<String> retval = new ArrayList<>();
		retval.add("ingot" + OreberryConfig.firstUpper(oreberry.name));
		return retval;
	}

	public JsonObject toJson() {
		Gson gson = new Gson();
		JsonObject json = new JsonObject();
		if(!name.equals(getDefaultName())) {
			json.addProperty("name", name);
		}
		if(!color.equals(getDefaultColor())) {
			json.addProperty("color", color);
		}
		if(!oredictNames.equals(getDefaultOredictNames())) {
			json.add("oredictNames", gson.toJsonTree(oredictNames));
		}
		if(!ingotNames.equals(getDefaultIngotNames())) {
			json.add("ingotNames", gson.toJsonTree(ingotNames));
		}
		return json;
	}
}