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

package api.flowservice;

/**
 * Class representing match fields.
 */
public class FlowMatch implements FlowMatchInterface {

    private final Integer ETH_TYPE;
    private final String ETH_DST;
    private final String ETH_SRC;
    private final Integer IN_PORT;
    private final Integer IP_PROTO;
    private final String IPV4_SRC;
    private final String IPV4_DST;
    private final Integer ICMPV4_TYPE;
    private final Integer ICMPV4_CODE;
    private final Integer TCP_SRC;
    private final Integer TCP_DST;
    private final String type;

    public FlowMatch(Integer eth_type,
                     String eth_dst,
                     String eth_src,
                     Integer in_port,
                     Integer ip_proto,
                     String ipv4_src,
                     String ipv4_dst,
                     Integer icmpv4_type,
                     Integer icmpv4_code,
                     Integer tcp_src,
                     Integer tcp_dst,
                     String type) {


        this.ETH_TYPE = eth_type;
        this.ETH_DST = eth_dst;
        this.ETH_SRC = eth_src;
        this.IN_PORT = in_port;
        this.IP_PROTO = ip_proto;
        this.IPV4_DST = ipv4_dst;
        this.ICMPV4_TYPE = icmpv4_type;
        this.ICMPV4_CODE = icmpv4_code;
        this.TCP_SRC = tcp_src;
        this.TCP_DST = tcp_dst;
        this.type = type;
        this.IPV4_SRC = ipv4_src;
    }

    public FlowMatch() {

        ICMPV4_TYPE = null;
        ETH_TYPE = null;
        ETH_DST = null;
        ETH_SRC = null;
        IN_PORT = null;
        IP_PROTO = null;
        IPV4_SRC = null;
        IPV4_DST = null;
        TCP_SRC = null;
        TCP_DST = null;
        ICMPV4_CODE = null;
        type = null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getETH_DST() {
        return this.ETH_DST;
    }

    public Integer getETH_TYPE() {
        return this.ETH_TYPE;
    }

    public String getETH_SRC() {
        return this.ETH_SRC;
    }

    public Integer getIP_PROTO() {
        return this.IP_PROTO;
    }

    public Integer getIN_PORT() {
        return this.IN_PORT;
    }

    public String getIPV4_SRC() {
        return this.IPV4_SRC;
    }

    public String getIPv4_DST() {
        return this.IPV4_DST;
    }

    public Integer getICMPV4_CODE() {
        return this.ICMPV4_CODE;
    }

    public Integer getICMPV4_TYPE() {
        return this.ICMPV4_TYPE;
    }

    public Integer getTCP_DST() {
        return this.TCP_DST;
    }

    public Integer getTCP_SRC() {
        return this.TCP_SRC;
    }

    public String getType() {
        return this.type;
    }

    public static class Builder {
        private Integer ETH_TYPE;
        private String ETH_DST;
        private String ETH_SRC;
        private Integer IN_PORT;
        private Integer IP_PROTO;
        private String IPV4_SRC;
        private String IPV4_DST;
        private Integer ICMPV4_TYPE;
        private Integer ICMPV4_CODE;
        private Integer TCP_SRC;
        private Integer TCP_DST;
        private String type;

        protected Builder() {
        }


        public Builder ethType(int ETH_TYPE) {
            this.ETH_TYPE = new Integer(ETH_TYPE);
            return this;
        }

        public Builder ethDst(String ETH_DST) {
            this.ETH_DST = ETH_DST;
            return this;
        }

        public Builder ethSrc(String ETH_SRC) {
            this.ETH_SRC = ETH_SRC;
            return this;
        }

        public Builder inPort(int IN_PORT) {
            this.IN_PORT = new Integer(IN_PORT);
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder ipv4Src(String IPV4_SRC) {
            this.IPV4_SRC = IPV4_SRC;
            return this;
        }

        public Builder ipv4Dst(String IPV4_DST) {
            this.IPV4_DST = IPV4_DST;
            return this;
        }

        public Builder ipProto(int IP_PROTO) {
            this.IP_PROTO = new Integer(IP_PROTO);
            return this;
        }

        public Builder tcpSrc(int TCP_SRC) {
            this.TCP_SRC = new Integer(TCP_SRC);
            return this;
        }

        public Builder tcpDst(int TCP_DST) {
            this.TCP_DST = new Integer(TCP_DST);
            return this;
        }

        public Builder icmpv4_type(int ICMPV4_TYPE) {
            this.ICMPV4_TYPE = new Integer(ICMPV4_TYPE);
            return this;
        }

        public Builder icmpv4_code(int ICMPV4_CODE) {
            this.ICMPV4_CODE = new Integer(ICMPV4_CODE);
            return this;
        }

        public FlowMatch build() {
            return new FlowMatch(ETH_TYPE,
                    ETH_DST,
                    ETH_SRC,
                    IN_PORT,
                    IP_PROTO,
                    IPV4_SRC,
                    IPV4_DST,
                    ICMPV4_TYPE,
                    ICMPV4_CODE,
                    TCP_SRC,
                    TCP_DST,
                    type);
        }
    }
}