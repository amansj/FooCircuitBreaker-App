package com.foo.operation;



import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestEndPointOperation implements Operation {

	@Override
	public void run() throws Exception {
		OkHttpClient client = new OkHttpClient().newBuilder()
  			  .build();
  			Request request = new Request.Builder()
  			  .url("http://localhost:9090/test/data")
  			  .method("GET", null)
  			  .build();
  			Response response= client.newCall(request).execute();
  			if(!response.isSuccessful()) {
  				throw new RuntimeException();
  			}
					
	}

}
