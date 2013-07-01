package de.kumpelblase2.remoteentities.utilities;

import java.lang.reflect.Type;
import org.bukkit.craftbukkit.libs.com.google.gson.*;
import de.kumpelblase2.remoteentities.persistence.EntityData;
import de.kumpelblase2.remoteentities.persistence.ParameterData;

public class ParameterDataDeserializer implements JsonDeserializer<ParameterData>
{
	@Override
	public ParameterData deserialize(JsonElement inJsonElement, Type inType, JsonDeserializationContext inJsonDeserializationContext) throws JsonParseException
	{
		ParameterData data = new ParameterData();
		JsonObject object = inJsonElement.getAsJsonObject();
		data.pos = object.get("pos").getAsInt();
		data.type = object.get("type").getAsString();
		data.special = (object.has("special") ? object.get("special").getAsString() : "");
		data.value = object.get("value").getAsString();
		data.value = EntityData.objectParser.deserialize(data);

		return data;
	}
}