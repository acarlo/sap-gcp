package com.gdpr.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import com.exceptions.SARValidationException;
import com.gdpr.validator.Validator;
import com.google.cloud.storage.Storage;
import com.gdpr.xml.sar.SARRequest;
import com.gdpr.xml.sar.SARResponse;
import com.application.helper.Constants;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.BlobInfo;

import javax.inject.Singleton;

@Singleton
public class SARService {

	static FolderService folderService = new FolderService();
	static DocumentService documentService = new DocumentService();

	Storage storage = null;
	{
		storage = StorageService.getStorage();
	}

	/*
	 * This is to create a SAR report based on the SAR request
	 */
	public SARResponse createSARReport(SARRequest sarRequest) {
		
		SARResponse sarResponse;

		try {
			Validator.validateSARRequest(sarRequest);

			if(!folderService.folderExists(folderService.rootFolderStructure(sarRequest.getRegion(), sarRequest.getCountry(), sarRequest.getEnvironment(), sarRequest.getCustomerId()))){
				sarResponse = prepareSARResponse(1409, Constants.ERROR_STATUS, Constants.FOLDER_NOT_EXISTS, null);
				return sarResponse;
			}

			folderService.createTransactionFolders(sarRequest.getRegion(), sarRequest.getCountry(), sarRequest.getEnvironment(), sarRequest.getCustomerId(), sarRequest.getTransactionId());
			final String fileName = folderService.prepareStructure(Constants.ROOT, sarRequest.getRegion(), sarRequest.getCountry(),
					Constants.DOCUMENTS, sarRequest.getEnvironment(), sarRequest.getCustomerId(), sarRequest.getTransactionId(), Constants.SHARED) + "sarreport-" + System.currentTimeMillis() + ".pdf";

			ByteArrayOutputStream out = documentService.preparePDFDocument(sarRequest.getSARReport());
			InputStream inStream = new ByteArrayInputStream(out.toByteArray());

			BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(Constants.BUCKET, fileName)
					.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build(), inStream);
			
			String url = prepareDownloadURL(blobInfo.getMediaLink());
			sarResponse = prepareSARResponse(1200, Constants.SUCCESS_STATUS, Constants.SAR_SUCCESS, url);
			
		} catch (SARValidationException e) {
			sarResponse = prepareSARResponse(e.getCode(), Constants.ERROR_STATUS, e.getMessage(), null);
			return sarResponse;
		}

		catch (Exception e) {
			sarResponse = prepareSARResponse(1500, Constants.ERROR_STATUS, e.getMessage(), null);
			return sarResponse;
		}
		
		return sarResponse;
	}
	
	public String readObject(String fileName) {
		Blob blob = storage.get(Constants.BUCKET, fileName);
		if(blob == null) {
			return "blob is null";
		}
		ReadChannel r = blob.reader();
		if(r == null) {
			return "channel is null";
		}
		return blob.getName();
	}
	
	private SARResponse prepareSARResponse(int code, String status, String message, String url) {
		SARResponse response = new SARResponse();
		response.setCode(code);
		response.setStatus(status);
		response.setMessage(message);
		response.setUrl(url);
		return response;
	}
	
	public String prepareDownloadURL(String url) throws UnsupportedEncodingException {
		return url.substring(0,  url.indexOf("?")+1) + "alt=media";
	}

}
