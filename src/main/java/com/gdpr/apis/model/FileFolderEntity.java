package com.gdpr.apis.model;

public class FileFolderEntity {
    public String name;
    public Boolean isFile;

    public FileFolderEntity(){}

    public FileFolderEntity(String name, Boolean isFile){
        this.isFile = isFile;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        FileFolderEntity ffEntity = (FileFolderEntity) obj;
        return this.name.equalsIgnoreCase(ffEntity.name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.name.hashCode();
        result = prime * result + this.isFile.hashCode();
        return result;
    }
}
