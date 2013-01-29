package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.thinking.Behavior;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class BehaviorData implements ConfigurationSerializable
{
    public String canonicallyWrittenClass;
    public ParameterData[] parameterData;

    public BehaviorData()
    {

    }

    public BehaviorData(Behavior behavior)
    {
        this.canonicallyWrittenClass = behavior.getClass().getCanonicalName();
        this.parameterData = ConstructorSerializer.serializedConstructor(behavior.getConstructionals());
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

        Map<String, Object>[] serializedParameters = new HashMap[this.parameterData.length];

        int index = 0;
        for (ParameterData pData : this.parameterData) {
            serializedParameters[index] = pData.serialize();
            index++;
        }

        data.put("parameters", serializedParameters);

        return data;
    }
}
