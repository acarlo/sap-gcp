package com.gdpr.validator;

import com.application.helper.Constants;
import com.exceptions.SARValidationException;
import com.exceptions.ValidationMessage;
import com.gdpr.xml.sar.FolderRequest;
import com.gdpr.xml.sar.SARRequest;
import com.util.Util;

public class Validator {

    public static void validateFolderRequest(FolderRequest folderRequest) throws SARValidationException {
        if(Util.isNullOrEmpty(folderRequest.getRegion()))
            throw new SARValidationException(ValidationMessage.INVALID_VALUE, "Please provide a valid region value");

        if(Util.isNullOrEmpty(folderRequest.getCountry()))
            throw new SARValidationException(ValidationMessage.INVALID_VALUE, "Please provide a valid country value");

        if(Util.isNullOrEmpty(folderRequest.getEnvironment()))
            throw new SARValidationException(ValidationMessage.INVALID_VALUE, "Please provide a environment value");

        String env = folderRequest.getEnvironment();
        if(!(env.equalsIgnoreCase(Constants.ENVIRONMENT_BCP) || env.equalsIgnoreCase(Constants.ENVIRONMENT_BCQ)
                || env.equalsIgnoreCase(Constants.ENVIRONMENT_BCD) || env.equalsIgnoreCase(Constants.ENVIRONMENT_ECP)
                || env.equalsIgnoreCase(Constants.ENVIRONMENT_ECQ) || env.equalsIgnoreCase(Constants.ENVIRONMENT_ECD)
                ||env.equalsIgnoreCase(Constants.ENVIRONMENT_CRD) || env.equalsIgnoreCase(Constants.ENVIRONMENT_CRQ)
                || env.equalsIgnoreCase(Constants.ENVIRONMENT_CRP)))
            throw new SARValidationException(ValidationMessage.INVALID_VALUE, "Please provide a valid environment value");

        if(Util.isNullOrEmpty(folderRequest.getCustomerId()))
            throw new SARValidationException(ValidationMessage.INVALID_VALUE, "Please provide a valid customer id value");
    }


    public static void validateSARRequest(SARRequest sarRequest) throws SARValidationException {
        if(Util.isNullOrEmpty(sarRequest.getRegion()))
            throw new SARValidationException(ValidationMessage.INVALID_VALUE, "Please provide a valid region value");

        if(Util.isNullOrEmpty(sarRequest.getCountry()))
            throw new SARValidationException(ValidationMessage.INVALID_VALUE, "Please provide a valid country value");

        if(Util.isNullOrEmpty(sarRequest.getEnvironment()))
            throw new SARValidationException(ValidationMessage.INVALID_VALUE, "Please provide a valid environment value");

        String env = sarRequest.getEnvironment();
        if(!(env.equalsIgnoreCase(Constants.ENVIRONMENT_BCP) || env.equalsIgnoreCase(Constants.ENVIRONMENT_BCQ)
                || env.equalsIgnoreCase(Constants.ENVIRONMENT_BCD) || env.equalsIgnoreCase(Constants.ENVIRONMENT_ECP)
                || env.equalsIgnoreCase(Constants.ENVIRONMENT_ECQ) || env.equalsIgnoreCase(Constants.ENVIRONMENT_ECD)
                ||env.equalsIgnoreCase(Constants.ENVIRONMENT_CRD) || env.equalsIgnoreCase(Constants.ENVIRONMENT_CRQ)
                || env.equalsIgnoreCase(Constants.ENVIRONMENT_CRP)))
            throw new SARValidationException(ValidationMessage.INVALID_VALUE, "Please provide a valid environment value");

        if(Util.isNullOrEmpty(sarRequest.getCustomerId()))
            throw new SARValidationException(ValidationMessage.INVALID_VALUE, "Please provide a valid customer id value");

        if(Util.isNullOrEmpty(sarRequest.getTransactionId()))
            throw new SARValidationException(ValidationMessage.INVALID_VALUE, "Please provide a valid transaction id value");

    }
}
