/*
 * Copyright ${2018} [Adib Rastegarnia, Rajas H.Karandikar, Douglas Comer]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package tools.utility;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * REST API helper functions.
 */
public class DefaultRestApiHelper extends RestApiHelper {


    private static Logger log = Logger.getLogger(DefaultRestApiHelper.class);

    /**
     * Create an HTTP client to connect to REST API server.
     *
     * @param username username
     * @param password password
     * @return an http client instance
     */
    public static HttpClient createHttpClient(final String username,
                                              final String password) {

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(username, password);
        credentialsProvider.setCredentials(AuthScope.ANY, usernamePasswordCredentials);


        HttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        return httpClient;
    }

    /**
     * Get an http request.
     *
     * @param httpClient an instance of http client
     * @param uri        address of http page
     * @return an instance of HttpGet
     */
    public static HttpGet getRequest(HttpClient httpClient, String uri) {

        HttpGet httpReq = new HttpGet(uri);
        httpReq.addHeader("accept", "application/json");
        return httpReq;
    }

    /**
     * Execute a http request to get a HTTP response.
     *
     * @param httpRequest an http request.
     * @param httpClient  an instance of http client.
     * @return an HttpResponse instance
     */
    public static HttpResponse getResponse(HttpGet httpRequest, HttpClient httpClient) {

        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (statusCode / 100 != 2) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + httpResponse.getStatusLine().getStatusCode());
        }
        return httpResponse;
    }

    /**
     * Shut down http client connection.
     *
     * @param httpClient an instance of HTTP client.
     */
    public static void httpClientShutDown(HttpClient httpClient) {

    }

    /**
     * Post an HTTP Request.
     *
     * @param httpClient   an instance of HTTPClient
     * @param uri          URL address
     * @param stringEntity an instance of the JSON entity which should
     *                     be posted.
     */
    public static BufferedReader httpPostRequest(HttpClient httpClient,
                                                 String uri,
                                                 StringEntity stringEntity) {

        HttpPost postRequest = new HttpPost(uri);
        stringEntity.setContentType("application/json");
        postRequest.setEntity(stringEntity);


        HttpResponse response = null;
        try {
            response = httpClient.execute(postRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int statusCode = response.getStatusLine().getStatusCode();
        //System.out.println(statusCode);

        if (statusCode / 100 != 2) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*String output;
        System.out.println("Output from Server .... \n");
        try {
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return br;
    }

    public static BufferedReader httpXmlPostRequest(HttpClient httpClient,
                                                    String uri,
                                                    StringEntity stringEntity) {

        HttpPost postRequest = new HttpPost(uri);
        stringEntity.setContentType("application/xml;charset=utf-8");
        postRequest.setEntity(stringEntity);


        HttpResponse response = null;
        try {
            response = httpClient.execute(postRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode / 100 != 2) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode() + response.getStatusLine().getReasonPhrase());
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*String output;
        System.out.println("Output from Server .... \n");
        try {
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return br;

    }

    public static void httpDelRequest(HttpClient httpClient,
                                      String uri) {

        HttpDelete delRequest = new HttpDelete(uri);
        delRequest.addHeader("accept", "application/json");

        HttpResponse response = null;
        try {
            response = httpClient.execute(delRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode / 100 != 2) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }


    }

    /*public void httpPostRequest(int port, long dpid, OFMessage ofMessage) throws IOException {


        URL url = null;
        try {
            url = new URL("http://127.0.0.1:" + port + "/oftee/" + String.format("0x%016X", dpid));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String urlString = "http://127.0.0.1:" + port + "/oftee/" + String.format("0x%016X", dpid);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://127.0.0.1:" + port + "/oftee/" + String.format("0x%016X", dpid));

        ByteBuf OfMsgBuffer = Unpooled.buffer();
        ofMessage.writeTo(OfMsgBuffer);
        byte OfMsgBytes[] = new byte[OfMsgBuffer.readableBytes()];
        OfMsgBuffer.readBytes(OfMsgBytes);

        post.setEntity(new ByteArrayEntity(OfMsgBytes, ContentType.APPLICATION_OCTET_STREAM));
        HttpResponse response = client.execute((HttpUriRequest) post);

        if (response == null) {
            log.info(" response is null\n");
        }

        HttpEntity entity = response.getEntity();

        if (entity != null) {
            log.info("response is not null\n");
            System.out.println(response.getStatusLine());
            log.info(EntityUtils.toString(entity));
        }


    }*/

}
