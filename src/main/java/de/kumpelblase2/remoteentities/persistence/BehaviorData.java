package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.thinking.Behavior;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class BehaviorData implements ConfigurationSerializable
{
    public String canonicallyWrittenClass;

    public BehaviorData()
    {

    }

    public BehaviorData(Behavior behavior)
    {
        this.canonicallyWrittenClass = behavior.getClass().getCanonicalName();
    }

    public BehaviorData(Map<String, Object> inData)
    {
        this.canonicallyWrittenClass = (String)inData.get("canonicallyWrittenClass");
    }

    public Behavior getConstructedClass()
    {
        try {
            return (Behavior)Class.forName(this.canonicallyWrittenClass).newInstance();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("canonicallyWrittenClass", this.canonicallyWrittenClass);

        return data;
    }
}
