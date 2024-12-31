package com.ve.inventory_billing.exchange.services;

import java.lang.reflect.Field;

public class Mapper <U, T> {
    public T getObjectMappedForCreate(U objectFrom, Class <T>objectTo) {
        try {
            T result = objectTo.getDeclaredConstructor().newInstance();

            for(Field fromField: objectFrom.getClass().getDeclaredFields()) {
                try {
                    fromField.setAccessible(true);
                    Field toField = objectTo.getDeclaredField(fromField.getName());
                    toField.setAccessible(true);
                    if(fromField.getName().equals("id")) continue;
                    toField.set(result, fromField.get(objectFrom));
                } catch (Exception e) {
                    continue;
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T getObjectMappedForResponse(U objectFrom, Class <T>objectTo) {
        try {
            T result = objectTo.getDeclaredConstructor().newInstance();

            for(Field fromField: objectFrom.getClass().getDeclaredFields()) {
                try {
                    fromField.setAccessible(true);
                    Field toField = objectTo.getDeclaredField(fromField.getName());
                    toField.setAccessible(true);
                    toField.set(result, fromField.get(objectFrom));
                } catch (Exception e) {
                    continue;
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <ENTITY> ENTITY getObjectForUpdate(ENTITY newData, ENTITY oldData) throws NoSuchFieldException, IllegalAccessException {
        for(Field fieldWithNewData: newData.getClass().getDeclaredFields()) {
            fieldWithNewData.setAccessible(true);
            Field fieldToUpdate = oldData.getClass().getDeclaredField(fieldWithNewData.getName());
            fieldToUpdate.setAccessible(true);
            fieldToUpdate.set(oldData, fieldWithNewData.get(newData));
        }
        return oldData;
    }
}
