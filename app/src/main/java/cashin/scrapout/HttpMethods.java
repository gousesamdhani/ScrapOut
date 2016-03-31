package cashin.scrapout;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.*;
import com.google.gson.*;

/**
 * Created by Sammy on 27/02/16.
 */
public class HttpMethods {

    public JSONObject post(final HashMap<String, String> params, final HashMap<String, String> headerParams, String api) {
        String result = "";
        int responseCode = -1;
        JSONObject res = null;
        try {
            URL url = new URL(Constants.URL+api);
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("admin", "1234".toCharArray());
                }
            });
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            Iterator it = headerParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                conn.setRequestProperty(pair.getKey().toString(), pair.getValue().toString());
                it.remove(); // avoids a ConcurrentModificationException
            }
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));
            writer.flush();
            writer.close();
            os.close();
            responseCode = conn.getResponseCode();
            StringBuilder sb = new StringBuilder();
            BufferedReader br;
            if(responseCode >= 200 && responseCode <= 300)
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            else
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                result = result + sb.append(line + "\n");
            }
            res = new JSONObject(result);
            res.accumulate("responseCode",responseCode);
            br.close();
        }
        catch (Exception e) {
            System.out.println("EXCEPTION OCCURED "+e.getMessage());
        }
        return res;
    }

    public JSONObject get(final HashMap<String, String> params, String api) {
        String result = "";
        int responseCode = -1;
        JSONObject res = null;
        try {
            URL url = new URL(Constants.URL+api);
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("admin", "1234".toCharArray());
                }
            });
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                conn.setRequestProperty(pair.getKey().toString(), pair.getValue().toString());
                it.remove(); // avoids a ConcurrentModificationException
            }
            responseCode = conn.getResponseCode();
            StringBuilder sb = new StringBuilder();
            BufferedReader br;
            if(responseCode >= 200 && responseCode <= 300)
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            else
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                result = result + sb.append(line + "\n");
            }
            res = new JSONObject(result);
            res.accumulate("responseCode",responseCode);
            br.close();
        }
        catch (Exception e) {
            System.out.println("EXCEPTION OCCURED "+e.getMessage());
        }
        return res;
    }

    public static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
