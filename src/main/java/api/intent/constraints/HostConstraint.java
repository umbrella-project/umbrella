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

package api.intent.constraints;

import api.topostore.TopoHost;

import java.util.ArrayList;
import java.util.List;

public class HostConstraint extends ConstraintAbstract implements Constraint {

    private List<TopoHost> srcHostsList;
    private List<TopoHost> dstHostsList;

    public HostConstraint() {
        srcHostsList = new ArrayList<>();
        dstHostsList = new ArrayList<>();
    }

    protected HostConstraint(List<TopoHost> srcHostsList,
                             List<TopoHost> dstHostsList) {
        this.srcHostsList = srcHostsList;
        this.dstHostsList = dstHostsList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<TopoHost> getDstHostsList() {
        return this.dstHostsList;
    }

    public List<TopoHost> getSrcHostsList() {
        return this.srcHostsList;
    }

    @Override
    public void parse() {

    }

    public static class Builder {
        private List<TopoHost> srcHostsList;
        private List<TopoHost> dstHostsList;


        public Builder srcHostsList(List<TopoHost> srcHostsList) {
            this.srcHostsList = srcHostsList;
            return this;
        }

        public Builder dstHostsList(List<TopoHost> dstHostsList) {
            this.dstHostsList = dstHostsList;
            return this;
        }


        public HostConstraint builder() {
            return new HostConstraint(srcHostsList, dstHostsList);
        }


    }
}
