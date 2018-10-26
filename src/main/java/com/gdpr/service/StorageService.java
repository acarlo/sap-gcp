package com.gdpr.service;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import javax.inject.Singleton;

@Singleton
public class StorageService {

    private static Storage storage = null;

    static {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public static Storage getStorage(){
        return storage;
    }
}
