package com.gdpr.apis.service;

import com.application.helper.Constants;
import com.gdpr.apis.model.FileFolderEntity;
import com.gdpr.service.StorageService;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class DirectoryService {

    Storage storage = StorageService.getStorage();


    public Set<FileFolderEntity> getFilesAndFolders(String path){
        Set<FileFolderEntity> folders = new HashSet<>();
        Page<Blob> blobs= storage.list(Constants.BUCKET, Storage.BlobListOption.prefix(path));
        Iterator<Blob> bItr = blobs.iterateAll().iterator();
        while(bItr.hasNext()){
            Blob blob = bItr.next();
            String pathTokens[] = blob.getName().replace(path, "").split("/");
            if(pathTokens.length > 0){
                String next = !(pathTokens[0].equalsIgnoreCase("/") || pathTokens[0].equalsIgnoreCase(""))
                        ? pathTokens[0] : pathTokens.length >=2 ? pathTokens[1] : null;
                if(next != null){
                    FileFolderEntity ffEntity = new FileFolderEntity();
                    ffEntity.name = next;
                    ffEntity.isFile = !blob.getName().contains(next + "/");
                    folders.add(ffEntity);
                }
            }
        }

        return folders;
    }
}
