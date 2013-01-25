package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.thinking.Desire;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class DesireData implements ConfigurationSerializable
{
    public String desireName;
    public int priority;
    public int type;

    public DesireData() {

    }

    public DesireData(Desire desire) {
        this.desireName = desire.getClass().getSimpleName();
        this.type = desire.getType();
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("desireName", this.desireName);
        data.put("priority", this.priority);
        data.put("type", this.type);

        return data;
    }
}
