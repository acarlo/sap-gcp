package com.gdpr.service;

import com.application.helper.Constants;
import com.exceptions.SARValidationException;
import com.gdpr.validator.Validator;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.gdpr.xml.sar.FolderRequest;
import com.gdpr.xml.sar.FolderResponse;

import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;

@Singleton
public class FolderService {

	Storage storage = null;
    {
        storage = StorageService.getStorage();
    }

    /*
     * This is to create a folder (/root/ {{region}} / {{country}} / Documents / {{customer_id}})
     */
    public FolderResponse createFolder(FolderRequest folderRequest) {
        FolderResponse folderResponse;

        try {


            Validator.validateFolderRequest(folderRequest);

            if(folderExists(rootFolderStructure(folderRequest.getRegion(), folderRequest.getCountry(), folderRequest.getEnvironment(), folderRequest.getCustomerId()))){
                folderResponse = prepareFolderResponse(1400, Constants.ERROR_STATUS, Constants.FOLDER_EXISTS, null);
                return folderResponse;
            }

            String folderName = rootFolderStructure(folderRequest.getRegion(), folderRequest.getCountry(), folderRequest.getEnvironment(), folderRequest.getCustomerId());

            BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(Constants.BUCKET, folderName)
                    .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))).build(), new ByteArrayInputStream(new byte[0]));

            String url = blobInfo.getMediaLink().replace("/download", "");
            folderResponse = prepareFolderResponse(1200, Constants.SUCCESS_STATUS, Constants.FOLDER_SUCCESS, url);

        } catch (SARValidationException e) {
            folderResponse = prepareFolderResponse(e.getCode(), Constants.ERROR_STATUS, e.getMessage(), null);
            return folderResponse;
        }

        catch (Exception e) {
            folderResponse = prepareFolderResponse(1500, Constants.ERROR_STATUS, e.getMessage(), null);
            return folderResponse;
        }

        return folderResponse;
    }

    private FolderResponse prepareFolderResponse(int code, String status, String message, String url) {
        FolderResponse response = new FolderResponse();
        response.setCode(code);
        response.setStatus(status);
        response.setMessage(message);
        response.setUrl(url);
        return response;
    }

    public boolean folderExists(String object){
        Blob blob = storage.get(Constants.BUCKET, object);
        boolean res = blob != null ? true : false;
        return res;
    }

    public String rootFolderStructure(String region, String country, String environment, String customer_id){
        return prepareStructure(Constants.ROOT, region, country, Constants.DOCUMENTS, environment, customer_id);
    }

    public void createTransactionFolders(String region, String country, String environment, String customer_id, String transaction_id){

        String internal = prepareStructure(Constants.ROOT, region, country, Constants.DOCUMENTS, environment, customer_id, transaction_id, Constants.INTERNAL);
        BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(Constants.BUCKET, internal)
                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))).build(), new ByteArrayInputStream(new byte[0]));

        String external = prepareStructure(Constants.ROOT, region, country, Constants.DOCUMENTS, environment, customer_id, transaction_id, Constants.EXTERNAL);
        blobInfo = storage.create(BlobInfo.newBuilder(Constants.BUCKET, external)
                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))).build(), new ByteArrayInputStream(new byte[0]));

        String shared = prepareStructure(Constants.ROOT, region, country, Constants.DOCUMENTS, environment, customer_id, transaction_id, Constants.SHARED);
        blobInfo = storage.create(BlobInfo.newBuilder(Constants.BUCKET, shared)
                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))).build(), new ByteArrayInputStream(new byte[0]));
    }

    public String prepareStructure(String ...objects){
        if(objects == null || objects.length == 0)
            return null;

        StringBuffer sb = new StringBuffer();
        for(String obj : objects){
            sb.append(Constants.FORWARD_SLASH).append(obj);
        }
        sb.append(Constants.FORWARD_SLASH);
        return sb.toString();
    }
}
