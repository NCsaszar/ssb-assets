//package com.smoothstack.branchservice.model.converter;
//
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//
//@Converter
//public class EncryptionConverter implements AttributeConverter<String, String> {
//
//    @Override
//    public String convertToDatabaseColumn(String attribute) {
//        // Implement encryption logic (e.g., using a library like Jasypt)
//        return encrypt(attribute);
//    }
//
//    @Override
//    public String convertToEntityAttribute(String dbData) {
//        // Implement decryption logic
//        return decrypt(dbData);
//    }
//
//    // Implement encryption and decryption methods
//}