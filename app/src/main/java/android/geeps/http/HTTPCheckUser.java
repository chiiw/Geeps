package android.geeps.http;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HTTPCheckUser extends AsyncTask<String, Void, String> {

   private static final String HEADER_VALUE = "application/json";
   private static final String URL = "http://geeps2.herokuapp.com/usuario/check";
   private JSONObject jsonObject;

   public boolean check(String phoneUser) {
      try {
         String response = this.execute(phoneUser).get();
         this.jsonObject = new JSONObject(response);
         if (this.jsonObject.getString("success") != null) {
            return true;
         }
      } catch (JSONException e) {
      } catch (InterruptedException e) {
      } catch (ExecutionException e) {
      }
      return false;
   }

   public JSONObject getJsonClient(){
      return this.jsonObject;
   }

   @Override
   protected final String doInBackground(final String... params) {
      String phone = params[0];
      Map<String, String> comment = new HashMap<String, String>();
      comment.put("phone", phone);

      String json = new GsonBuilder().create().toJson(comment, Map.class);
      String response = "";
      try {
         HttpPost httpPost = new HttpPost(URL);
         httpPost.setEntity(new StringEntity(json));
         httpPost.setHeader("Accept", HEADER_VALUE);
         httpPost.setHeader("Content-type", HEADER_VALUE);
         HttpClient client = new DefaultHttpClient();

         HttpResponse serverAnswer = client.execute(httpPost);

         BufferedReader br =
               new BufferedReader(new InputStreamReader(
                     serverAnswer.getEntity().getContent(),
                     Charset.defaultCharset()));
         StringBuffer sb = new StringBuffer("");

         while ((response = br.readLine()) != null) {
            sb.append(response);
         }

         br.close();

         response = sb.toString();
      } catch (Exception e) {
         Log.e("POST", e.getMessage());
      }

      return response;
   }
}
