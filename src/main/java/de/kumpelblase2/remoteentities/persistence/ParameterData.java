package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.Plugin;
import sun.tools.tree.ThisExpression;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ParameterData implements ConfigurationSerializable {
    public Object type;
    public Object value;
    public Object requirement;

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
        } else if (object instanceof Class[]) {
            Class[] classes = (Class[])object;
            this.type = "class";
            this.value = classes[0].getCanonicalName();
            this.requirement = classes[1].getCanonicalName();
        } else if (object instanceof Plugin) {
            this.type = "predefined_reference";
            this.value = "plugin";
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

//        if (this.value instanceof Class) {
//            data.put("value", "invalid");
////            this.requirement("")
////            System.out.println(data.get("value"));
//
//        } else {
            data.put("value", this.value);
//        }


        if (requirement != null) {
            data.put("requirement", this.requirement);
        }

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
            } else if (this.value.equals("plugin")) {
                replacementType = Plugin.class.getCanonicalName();
                replacementValue = entity.getManager().getPlugin();
            }
        } else if (this.type.equals("class")) {
            replacementType = (String)this.requirement;
            try {
                replacementValue = Class.forName((String)this.value);
            } catch (Exception e) {

            }
        } else if (this.type.equals(Float.class.getCanonicalName()) && this.value.getClass() != float.class) {
            replacementValue = ((Double)this.value).floatValue();
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
