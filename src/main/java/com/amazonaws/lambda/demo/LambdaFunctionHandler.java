package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import java.io.File;


public class LambdaFunctionHandler implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);
        try {
        	String key_name = "teste";
        	String bucket_name = "data-lake-teste-1";
        	System.out.format("Uploading %s to S3 bucket %s...\n", input, bucket_name);
        	final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("sa-east-1").build();
        	File file = new File("teste");
        	s3.putObject(bucket_name, key_name, file);
        } 
        catch (AmazonServiceException e) {
        	System.err.println(e.getErrorMessage());
        	System.exit(1);
    	}
		return null;
    }
}
