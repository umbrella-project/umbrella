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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/**
 * REST API helper functions interface.
 */
public abstract class RestApiHelper {

    static HttpClient createHttpClient(String username, String password) {
        return null;
    }

    static HttpGet getRequest(HttpClient httpClient, String uri) {
        return null;
    }

    static HttpResponse getResponse(HttpGet HttpRequest, HttpClient httpClient) {
        return null;
    }

    static void httpClientShutDown(HttpClient httpClient) {
    }
}
