package gmail_plugin_v1;
/***************************************************************************
 * 
 * Copyright (c) 2012 Baidu.com, Inc. All Rights Reserved
 * 
 **************************************************************************/
import java.applet.Applet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.auth.BCSSignCondition;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.BCSClientException;
import com.baidu.inf.iis.bcs.model.BCSServiceException;
import com.baidu.inf.iis.bcs.model.BucketSummary;
import com.baidu.inf.iis.bcs.model.Empty;
import com.baidu.inf.iis.bcs.model.ObjectListing;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.model.ObjectSummary;
import com.baidu.inf.iis.bcs.model.Resource;
import com.baidu.inf.iis.bcs.model.SuperfileSubObject;
import com.baidu.inf.iis.bcs.model.X_BS_ACL;
import com.baidu.inf.iis.bcs.policy.Policy;
import com.baidu.inf.iis.bcs.policy.PolicyAction;
import com.baidu.inf.iis.bcs.policy.PolicyEffect;
import com.baidu.inf.iis.bcs.policy.Statement;
import com.baidu.inf.iis.bcs.request.CreateBucketRequest;
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;
import com.baidu.inf.iis.bcs.request.GetObjectRequest;
import com.baidu.inf.iis.bcs.request.ListBucketRequest;
import com.baidu.inf.iis.bcs.request.ListObjectRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.request.PutSuperfileRequest;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;

@SuppressWarnings("unused")
public class BCS {
	// ----------------------------------------
	String host = "bcs.duapp.com";
	String accessKey = "L4uUrC9k13AFVoYaqLBH7mS3";
	String secretKey = "68CvHSayturShHDybFjNRasLBnk0mzLc";
	String bucket = "gmailyunpan";
	// ----------------------------------------
	static String object = "/first-object";
	static File destFile = new File("test");
	BaiduBCS baiduBCS;
	/**
	 * @param args
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public BCS()
	{
		BCSCredentials credentials = new BCSCredentials(accessKey, secretKey);
		baiduBCS = new BaiduBCS(credentials, host);
		baiduBCS.setDefaultEncoding("UTF-8");
	}
	

	private void createBucket(BaiduBCS baiduBCS) {
		// baiduBCS.createBucket(bucket);
		baiduBCS.createBucket(new CreateBucketRequest(bucket, X_BS_ACL.Private));
	}

	private void deleteBucket(BaiduBCS baiduBCS) {
		baiduBCS.deleteBucket(bucket);
	}

	public void deleteObject(BaiduBCS baiduBCS) {
		Empty result = baiduBCS.deleteObject(bucket, object).getResult();
	}

	public Map<String,String> getObjectMetadata(BaiduBCS baiduBCS) {
		ObjectMetadata objectMetadata = baiduBCS.getObjectMetadata(bucket, object).getResult();
		return objectMetadata.getUserMetadata();
	}

	private void getObjectWithDestFile(BaiduBCS baiduBCS) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, object);
		baiduBCS.getObject(getObjectRequest, destFile);
	}
	private void listObject(BaiduBCS baiduBCS) {
		ListObjectRequest listObjectRequest = new ListObjectRequest(bucket);
		listObjectRequest.setStart(0);
		listObjectRequest.setLimit(20);
		// ------------------by dir
		{
			// prefix must start with '/' and end with '/'
			// listObjectRequest.setPrefix("/1/");
			// listObjectRequest.setListModel(2);
		}
		// ------------------only object
		{
			// prefix must start with '/'
			// listObjectRequest.setPrefix("/1/");
		}
		BaiduBCSResponse<ObjectListing> response = baiduBCS.listObject(listObjectRequest);
		System.out.println("we get [" + response.getResult().getObjectSummaries().size() + "] object record.");
		for (ObjectSummary os : response.getResult().getObjectSummaries()) {
			System.out.println(os.toString());
		}
	}
	public void putObjectByInputStream(BaiduBCS baiduBCS) throws FileNotFoundException {
		File file = createSampleFile();
		InputStream fileContent = new FileInputStream(file);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType("text/html");
		objectMetadata.setContentLength(file.length());
		PutObjectRequest request = new PutObjectRequest(bucket, object, fileContent, objectMetadata);
		ObjectMetadata result = baiduBCS.putObject(request).getResult();
	}
	public void setObjectMetadata(BaiduBCS baiduBCS,Map<String,String> metadatamap) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType("text/html12");
		for(Entry<String, String> iterator:metadatamap.entrySet()) objectMetadata.addUserMetadata(iterator.getKey(), iterator.getValue());
		baiduBCS.setObjectMetadata(bucket, object, objectMetadata);
	}
}