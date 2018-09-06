package com.gdrp.sapgcpspring;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.sapgcp.service.SARService;
import com.sapgcp.xml.sar.FolderRequest;
import com.sapgcp.xml.sar.FolderResponse;
import com.sapgcp.xml.sar.SARRequest;
import com.sapgcp.xml.sar.SARResponse;

@Endpoint
public class SAREndpoint {

	
	private static final String NAMESPACE_URI = "http://www.sapgcp.com/xml/sar";
	
	SARService sarService = new SARService();
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "FolderRequest")
    @ResponsePayload
	public FolderResponse createFolder(@RequestPayload FolderRequest folderRequest) {
		FolderResponse folderResponse = new FolderResponse();
		Boolean res = sarService.createFolder(folderRequest.getCustomerId());
		folderResponse.setStatus(res.toString());
		return folderResponse;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "SARRequest")
    @ResponsePayload
	public SARResponse createSARReport(@RequestPayload SARRequest request) {
		SARResponse sarResponse = new SARResponse();
		sarResponse.setUrl(sarService.createSARReport(request));
		return sarResponse;
	}
	
}
