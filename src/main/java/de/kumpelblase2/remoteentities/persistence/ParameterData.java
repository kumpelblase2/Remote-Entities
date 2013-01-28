package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class ParameterData implements ConfigurationSerializable {
    public Object type;
    public Object value;

    public ParameterData()
    {

    }

    public ParameterData(Object type, Object value)
    {
        this.type = type;
        this.value = value;
    }

    public ParameterData(Object object) {
        this.type = object.getClass().getCanonicalName();
        this.value = object;

        if (object instanceof RemoteEntity) {
            this.type = "predefined_reference";
            this.value = "entity";
        }
    }

    public ParameterData(Map<String, Object> inData)
    {
        this.type = inData.get("type");
        this.value = inData.get("value");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("type", this.type);
        data.put("value", this.value);

        return data;
    }

    public ParameterData getSynthesizedParameterDataForEntity(RemoteEntity entity)
    {
        ParameterData returnData = null;
        String replacementType = null;
        Object replacementValue = null;

        if (this.type.equals("predefined_reference")) {
            if (this.value.equals("entity")) {
                replacementType = RemoteEntity.class.getCanonicalName();
                replacementValue = entity;
            }
        }

        if (replacementType == null) {
            replacementType = (String)this.type;
        }

        if (replacementValue == null) {
            replacementValue = this.value;
        }

        try {
            returnData = new ParameterData(Class.forName(replacementType), replacementValue);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }



        return returnData;
    }
}
