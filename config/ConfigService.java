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

package config;

import drivers.controller.Controller;
import drivers.onos.OnosController;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration service.
 */
public class ConfigService {

    protected Config config;

    public ConfigService() {

        config = new Config();
    }

    public Config getConfig() {
        return this.config;
    }

    public void readConfigFile() {
        Properties prop = new Properties();
        InputStream input = null;


        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);



            config.setControllerName(prop.getProperty("controller"));
            config.setAPI_ON(prop.getProperty("API_ON"));


        } catch (IOException ex) {

            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public int getApiOn() {
        ConfigService configService = new ConfigService();
        configService.readConfigFile();
        Config config = configService.getConfig();
        return Integer.parseInt(config.getAPI_ON());
    }

    public String getControllerName() {

        String controllerName;
        ConfigService configService = new ConfigService();
        configService.readConfigFile();
        Config config = configService.getConfig();

        controllerName = config.getControllerName();

        return controllerName;
    }


    public Controller init(String controllerName) {
        PropertyConfigurator.configure("resources/log4j.properties");
        Controller controller = null;
        if (controllerName.equalsIgnoreCase("onos")) {
            return controller = new OnosController();
        }
        //} else if (controllerName.equalsIgnoreCase("odl")) {
        //    return controller = new OdlController();

        //} else if(controllerName.equalsIgnoreCase("ryu"))
        //{
        //    return controller = new RyuController();
        //}
        else {

            return controller;
        }

    }

}
