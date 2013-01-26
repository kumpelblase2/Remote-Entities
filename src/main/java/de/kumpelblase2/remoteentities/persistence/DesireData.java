package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.thinking.Desire;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class DesireData implements ConfigurationSerializable
{
    public String name;
    public int priority;

    public DesireData() {

    }

    public DesireData(Desire desire) {
        this.name = desire.getClass().getSimpleName();
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", this.name);
        data.put("priority", this.priority);

        return data;
    }
}
