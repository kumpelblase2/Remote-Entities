package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConstructorSerializer {
    public static ParameterData[] serializedConstructor(Object[] constructionals)
    {
        ParameterData[] data = new ParameterData[constructionals.length];
        int index = 0;

        for (Object object : constructionals) {
            data[index] = new ParameterData(object);

            index++;
        }

        return data;
    }

    public static ParameterData[] constructionalsFromArrayForEntity(ParameterData[] parameterData, RemoteEntity entity)
    {
        ParameterData[] data = new ParameterData[parameterData.length];

        int index = 0;
        for (ParameterData paramData : parameterData) {
            data[index] = paramData.getSynthesizedParameterDataForEntity(entity);

            index++;
        }

        return data;
    }
}
