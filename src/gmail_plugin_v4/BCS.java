package gmail_plugin_v1;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.model.BucketSummary;
import com.baidu.inf.iis.bcs.model.Empty;
import com.baidu.inf.iis.bcs.model.ObjectListing;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.model.ObjectSummary;
import com.baidu.inf.iis.bcs.model.X_BS_ACL;
import com.baidu.inf.iis.bcs.request.CreateBucketRequest;
import com.baidu.inf.iis.bcs.request.GetObjectRequest;
import com.baidu.inf.iis.bcs.request.ListBucketRequest;
import com.baidu.inf.iis.bcs.request.ListObjectRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;

public class BCS {
	// ----------------------------------------
	String host = "bcs.duapp.com";
	String accessKey = "SiYHO70TSG1UEV9pzMI29wnQ";
	String secretKey = "55YVSdGNEBFzlc2T6kNifScVZKFyCFGf";
	String bucket;
	BaiduBCS baiduBCS;

	public BCS(String user) {
		BCSCredentials credentials = new BCSCredentials(accessKey, secretKey);
		baiduBCS = new BaiduBCS(credentials, host);
		baiduBCS.setDefaultEncoding("UTF-8");
		bucket = user;
		if (!checkBucket(listBucket(), user))
			createBucket(user);
	}

	public BaiduBCSResponse<List<BucketSummary>> listBucket() {
		ListBucketRequest listBucketRequest = new ListBucketRequest();
		return baiduBCS.listBucket(listBucketRequest);

	}

	public boolean checkBucket(BaiduBCSResponse<List<BucketSummary>> response,
			String name) {
		for (BucketSummary bucket : response.getResult()) {
			if (bucket.getBucket().equals(name))
				return true;
		}
		return false;
	}

	public void download(List<memfile> l) throws FileNotFoundException,
			MessagingException, IOException {
		for (memfile p : l) {
			//if(p==null)System.out.println("P IS NULL");
			//if(p.data==null) System.out.println("p.data is nullc"+p.length);
			if (!checkobject("receive" + p.name))
				putObjectByInputStream("receive" + p.name,
						new ByteArrayInputStream(p.data), p.length);
//			if (!checkobject("receive" + p.name))
//				putObjectByInputStream("receive" + p.name,
//						p.p.getInputStream(), p.length);
		}
	}

	public void createBucket(String bucket) {
		baiduBCS.createBucket(new CreateBucketRequest(bucket, X_BS_ACL.Private));
	}

	public void deleteBucket(String bucket) {
		baiduBCS.deleteBucket(bucket);
	}

	public void deleteObject(String object) {
		Empty result = baiduBCS.deleteObject(bucket, object).getResult();
	}

	public BaiduBCSResponse<ObjectListing> listObject() {
		ListObjectRequest listObjectRequest = new ListObjectRequest(bucket);
		listObjectRequest.setStart(0);
		listObjectRequest.setLimit(20);
		BaiduBCSResponse<ObjectListing> response = baiduBCS
				.listObject(listObjectRequest);
		return response;
	}

	public boolean checkobject(String object) {
		for (ObjectSummary os : listObject().getResult().getObjectSummaries()) {
			String s = os.toString();
			int pos = s.indexOf("name=/");
			s = s.substring(pos);
			pos = s.indexOf(',');
			s = s.substring(0, pos);
			//System.out.println(s);
			if (s.contains('/' + object))
				return true;
		}
		return false;
	}

	public InputStream receive(String filename, InputStream stream, int length)
			throws FileNotFoundException {
		if (!checkobject(filename))
			putObjectByInputStream(filename, stream, length);
		return getObjectWithInputstream(filename);
	}

	public InputStream getObjectWithInputstream(String object) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucket,
				'/' + object);
		return baiduBCS.getObject(getObjectRequest).getResult().getContent();
	}

	public void putObjectByInputStream(String name, InputStream fileContent,
			int length) throws FileNotFoundException {
		System.out.print("gmailtobcs" + length);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType("application/octet-stream");
		objectMetadata.setContentLength(length);
		BufferedInputStream bufi = new BufferedInputStream(fileContent);
		PutObjectRequest request = new PutObjectRequest(bucket, '/' + name,
				bufi, objectMetadata);
		ObjectMetadata result = baiduBCS.putObject(request).getResult();
		System.out.print("gmailtobcsdone" + result);
	}

	public void setObjectMetadata(String object, Map<String, String> metadatamap) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType("application/octet-stream");
		for (Entry<String, String> iterator : metadatamap.entrySet())
			objectMetadata.addUserMetadata(iterator.getKey(),
					iterator.getValue());
		baiduBCS.setObjectMetadata(bucket, '/' + object, objectMetadata);
	}

	public Map<String, String> getObjectMetadata(String object) {
		ObjectMetadata objectMetadata = baiduBCS.getObjectMetadata(bucket,
				'/' + object).getResult();
		return objectMetadata.getUserMetadata();
	}
}