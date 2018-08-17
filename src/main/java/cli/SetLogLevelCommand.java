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

package cli;


import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import picocli.CommandLine;

import java.util.Enumeration;

/**
 * Sets log level (i.e. DEBUG, INFO, WARN,ERROR)
 */
@CommandLine.Command(name = "setlog", header = "%n@|red log configuration command|@")
class SetLogLevelCommand implements Runnable {

    @CommandLine.Option(names = {"-l", "--level"}, description = "Set log level", required = false)
    String logLevel;


    public void run() {

        Logger root = Logger.getRootLogger();
        Enumeration allLoggers = root.getLoggerRepository().getCurrentCategories();


        boolean logLevelRecognized = true;
        if ("DEBUG".equalsIgnoreCase(logLevel)) {
            root.setLevel(Level.DEBUG);
            while (allLoggers.hasMoreElements()) {
                Category tmpLogger = (Category) allLoggers.nextElement();
                tmpLogger.setLevel(Level.DEBUG);
            }
        } else if ("INFO".equalsIgnoreCase(logLevel)) {
            root.setLevel(Level.INFO);
            while (allLoggers.hasMoreElements()) {
                Category tmpLogger = (Category) allLoggers.nextElement();
                tmpLogger.setLevel(Level.INFO);
            }
        } else if ("WARN".equalsIgnoreCase(logLevel)) {
            root.setLevel(Level.WARN);
            while (allLoggers.hasMoreElements()) {
                Category tmpLogger = (Category) allLoggers.nextElement();
                tmpLogger.setLevel(Level.WARN);
            }
        } else if ("ERROR".equalsIgnoreCase(logLevel)) {
            root.setLevel(Level.ERROR);
            while (allLoggers.hasMoreElements()) {
                Category tmpLogger = (Category) allLoggers.nextElement();
                tmpLogger.setLevel(Level.ERROR);
            }
        } else if ("FATAL".equalsIgnoreCase(logLevel)) {
            root.setLevel(Level.FATAL);
            while (allLoggers.hasMoreElements()) {
                Category tmpLogger = (Category) allLoggers.nextElement();
                tmpLogger.setLevel(Level.FATAL);
            }
        } else {
            logLevelRecognized = false;
        }

        if (logLevelRecognized) {
            System.out.println("Log level has been set to: " + logLevel + "<br/>");
        } else {
            System.out.println("logLevel parameter '" + logLevel + "' level not recognized<br/>");
        }


    }
}
