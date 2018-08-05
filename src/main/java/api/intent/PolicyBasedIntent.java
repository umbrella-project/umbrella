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

package api.intent;

import api.flowservice.Flow;
import api.intent.constraints.Constraint;
import api.intent.networkOperation.NetworkOperationAbstract;
import api.intent.regionService.Region;

import java.util.ArrayList;
import java.util.List;

public class PolicyBasedIntent extends FlowIntent {

    private NetworkOperationAbstract networkOperation;
    private Region srcRegion;
    private Region dstRegion;
    private List<Constraint> constraints;


    public PolicyBasedIntent() {
        super();
        networkOperation = null;
        constraints = new ArrayList<>();
        srcRegion = null;
        dstRegion = null;


    }

    protected PolicyBasedIntent(Flow flow,
                                NetworkOperationAbstract networkOperation,
                                Region srcRegion,
                                Region dstRegion,
                                List<Constraint> constraints) {
        super(flow);
        this.srcRegion = srcRegion;
        this.dstRegion = dstRegion;
        this.constraints = constraints;


    }

    public static Builder builder() {
        return new Builder();
    }

    public Region getDstRegion() {
        return this.dstRegion;
    }

    public Region getSrcRegion() {
        return this.srcRegion;
    }

    public List<Constraint> getConstraints() {
        return this.constraints;
    }

    public NetworkOperationAbstract getNetworkOperation() {
        return this.networkOperation;
    }

    public static class Builder extends FlowIntent.Builder {

        protected NetworkOperationAbstract networkOperation;
        protected Region srcRegion;
        protected Region dstRegion;
        protected List<Constraint> constraints;


        protected Builder() {

        }

        @Override
        public Builder flow(Flow flow) {
            return (Builder) super.flow(flow);


        }

        public Builder constraints(List<Constraint> constraints) {
            this.constraints = constraints;
            return this;
        }

        public Builder networkOpetation(NetworkOperationAbstract networkOperation) {
            this.networkOperation = networkOperation;
            return this;
        }

        public Builder srcRegion(Region srcRegion) {
            this.srcRegion = srcRegion;
            return this;
        }

        public Builder dstRegion(Region dstRegion) {
            this.dstRegion = dstRegion;
            return this;
        }

        public PolicyBasedIntent build() {
            return new PolicyBasedIntent(flow,
                    networkOperation,
                    srcRegion,
                    dstRegion,
                    constraints);
        }


    }
}
