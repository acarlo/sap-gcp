package com.application.sapgcpspring;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.gdpr.service.FolderService;
import com.gdpr.service.SARService;
import com.gdpr.xml.sar.FolderRequest;
import com.gdpr.xml.sar.FolderResponse;
import com.gdpr.xml.sar.SARRequest;
import com.gdpr.xml.sar.SARResponse;

@Endpoint
public class SAREndpoint {

	
	private static final String NAMESPACE_URI = "http://www.gdpr.com/xml/sar";
	
	SARService sarService = new SARService();
	FolderService folderService = new FolderService();
	
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "FolderRequest")
    @ResponsePayload
	public FolderResponse createFolder(@RequestPayload FolderRequest folderRequest) {
		return folderService.createFolder(folderRequest);
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "SARRequest")
    @ResponsePayload
	public SARResponse createSARReport(@RequestPayload SARRequest request) {
		return sarService.createSARReport(request);
	}
	
}
