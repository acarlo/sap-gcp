package com.gdpr.apis.controller;

import com.application.helper.Constants;
import com.exceptions.SARValidationException;
import com.exceptions.ValidationMessage;
import com.gdpr.apis.model.FileFolderEntity;
import com.gdpr.apis.service.DirectoryService;
import com.gdpr.service.StorageService;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.cloud.storage.Storage;

import javax.inject.Singleton;
import java.util.*;

@Singleton
@RestController
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:4200", "http://localhost:80", "http://127.0.0.1:80", "https://localhost", "https://127.0.0.1"})
@RequestMapping("/data")
public class DataController {

    Storage storage = StorageService.getStorage();
    DirectoryService directoryService = new DirectoryService();


    @RequestMapping("/files/list")
    public Set<FileFolderEntity> getFiles(@RequestParam("path") String path) throws SARValidationException {
        return directoryService.getFilesAndFolders(path);
    }

    @ExceptionHandler(SARValidationException.class)
    public ResponseEntity<SARValidationException> handleSARException(SARValidationException ex){
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }
}
