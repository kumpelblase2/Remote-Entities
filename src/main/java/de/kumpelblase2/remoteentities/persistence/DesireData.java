package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.thinking.Desire;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DesireData implements ConfigurationSerializable
{
    public String name;
    public int priority;
    public String[] constructionData;

    public DesireData() {

    }

    public DesireData(Desire desire)
    {
        this.name = desire.getClass().getCanonicalName();
        this.constructionData = desire.getConstructionData();
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", this.name);
        data.put("priority", this.priority);

        if (this.constructionData != null)
            data.put("constructionData", this.constructionData);

        return data;
    }

    @SuppressWarnings("unchecked")
    public DesireData(Map<String, Object> inData)
    {
        this.name = (String)inData.get("name");
        this.priority = ((Integer)inData.get("priority")).intValue();
        this.constructionData = (String[])inData.get("constructionData");
    }
}
