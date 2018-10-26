package com.application.sapgcpspring;

import com.application.helper.Constants;
import com.gdpr.service.StorageService;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/testme")
public class TestMe {


    @GetMapping()
    public void test() {
        Storage storage = StorageService.getStorage();
        storage.list(Constants.BUCKET, Storage.BlobListOption.prefix("/root")).getValues().forEach(e -> {
            System.out.println(e.getName());
        });
    }
}
