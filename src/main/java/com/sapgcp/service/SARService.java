package com.sapgcp.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.management.ObjectInstance;
import javax.management.ObjectName;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sapgcp.xml.sar.SARReport;
import com.sapgcp.xml.sar.SARRequest;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class SARService {
	
	private static Storage storage = null;

	static {
		storage = StorageOptions.getDefaultInstance().getService();
	}

	public String createSARReport(SARRequest sarRequest) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Document document = new Document();
			PdfWriter.getInstance(document, out);
			document.open();
			Iterator<SARReport.DataMap.Entry> itr = sarRequest.getSARReport().getDataMap().getEntry().iterator();
			while(itr.hasNext()) {
				SARReport.DataMap.Entry entry = itr.next();
				document.add(new Paragraph(entry.getKey() + " : " + entry.getValue()));
			}
			document.close();
			InputStream inStream = new ByteArrayInputStream(out.toByteArray());
			final String fileName = "customer/" + System.currentTimeMillis() + ".pdf";

			BlobInfo blobInfo = storage.create(BlobInfo.newBuilder("sap-gcp", fileName)
					// Modify access list to allow all users with link to read file
					.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build(), inStream);
			// return the public download link
			return blobInfo.getMediaLink();
		} catch (Exception e) {
			System.out.println("Exception :"+e.getMessage());
			e.printStackTrace();
		}
		return "error";
	}
	
	public boolean createFolder(String customer_id) {
		String folderName = customer_id + "/";
		BlobInfo blobInfo = storage.create(BlobInfo.newBuilder("sap-gcp", folderName)
				// Modify access list to allow all users with link to read file
				.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build(), new ByteArrayInputStream(new byte[0]));
		return true;
	}
}
