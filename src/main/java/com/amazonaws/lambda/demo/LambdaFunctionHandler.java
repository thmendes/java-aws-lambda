package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;


public class LambdaFunctionHandler implements RequestHandler<SNSEvent, String> {

    @Override
    public String handleRequest(SNSEvent input, Context context) {
    	try {
    		String key_name = input.getRecords().get(0).getSNS().getSubject() + ".json";
    		String bucket_name = "data-lake-teste-1";  		
			String message = input.getRecords().get(0).getSNS().getMessage();			
			
			InputStream stream = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType("application/json");
			metadata.setContentDisposition("attachment; filename=\"" + key_name);			
			
			PutObjectRequest put = new PutObjectRequest(bucket_name, key_name, stream, metadata);
			put.setStorageClass(StorageClass.ReducedRedundancy);
	        put.setMetadata(metadata);
	        
	        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.SA_EAST_1).build();
	        s3Client.putObject(put);
        }
        catch (AmazonServiceException e) {
        	System.err.println(e.getErrorMessage());
        	System.exit(1);
    	}
		return null;
    }    
}
