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

import org.apache.log4j.Logger;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.Scanner;

public class CliMain {
    private static Logger log = Logger.getLogger(CliMain.class);

    public static void main(String... args) {


        String signature = " __    __  .___  ___. .______   .______       _______  __       __          ___     \n" +
                "|  |  |  | |   \\/   | |   _  \\  |   _  \\     |   ____||  |     |  |        /   \\    \n" +
                "|  |  |  | |  \\  /  | |  |_)  | |  |_)  |    |  |__   |  |     |  |       /  ^  \\   \n" +
                "|  |  |  | |  |\\/|  | |   _  <  |      /     |   __|  |  |     |  |      /  /_\\  \\  \n" +
                "|  `--'  | |  |  |  | |  |_)  | |  |\\  \\----.|  |____ |  `----.|  `----./  _____  \\ \n" +
                " \\______/  |__|  |__| |______/  | _| `._____||_______||_______||_______/__/     \\__\\";


        log.info("\033[31m" + signature + "\n\n");


        Scanner scanner = new Scanner(System.in);
        while (true) {
            //System.out.print("\033[31;1mumbrella>> \033[0m");
            log.info("\033[31;1mumbrella>> \033[0m");


            String cmd = scanner.nextLine();
            if (cmd.equals("quit")) {
                break;
            }

            String[] arguments = cmd.split(" ");

            switch (arguments[0]) {
                case "topo":
                    CommandLine.run(new TopoCommand(), System.err, Arrays.copyOfRange(arguments, 1, arguments.length));
                    break;
                case "links":
                    CommandLine.run(new LinksCommand(), System.err, Arrays.copyOfRange(arguments, 1, arguments.length));
                    break;
                case "hosts":
                    CommandLine.run(new HostsCommand(), System.err, Arrays.copyOfRange(arguments, 1, arguments.length));
                    break;
                case "devices":
                    CommandLine.run(new DevicesCommand(), System.err, Arrays.copyOfRange(arguments, 1, arguments.length));
                    break;
                case "setlog":
                    CommandLine.run(new SetLogLevelCommand(), System.err, Arrays.copyOfRange(arguments, 1, arguments.length));
                    break;
                case "addRegion":
                    CommandLine.run(new AddRegionCommand(), System.err, Arrays.copyOfRange(arguments, 1, arguments.length));
                    break;


            }


        }


    }
}
