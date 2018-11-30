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


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Class implementing JSON array builder.
 */
public class JsonBuilder implements JsonBuilderInterface {

    /**
     * Create a JSON array based on a given buffer.
     *
     * @param br an instance of BufferReader
     * @return a JSON object contains retrieved information usign REST-API.
     */
    public JSONObject createJsonArray(BufferedReader br) {

        JSONObject jsonObject = null;
        Object obj = null;
        JSONParser parser = new JSONParser();

        String line;
        StringBuilder sb = new StringBuilder();

        try {

            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                sb.append(line);
            }
        } catch (IOException e) {
            System.out.println("error1");
            e.printStackTrace();
        }

        //System.out.println("string:"+sb.toString());
        try {
            //System.out.println(sb);
            obj = parser.parse(sb.toString());
        } catch (ParseException e) {
            System.out.println("error2");
            e.printStackTrace();
        }

        return (JSONObject) obj;
    }


}