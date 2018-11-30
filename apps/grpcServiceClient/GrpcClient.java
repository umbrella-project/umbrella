package apps.grpcServiceClient;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.onosproject.grpc.net.flow.criteria.models.CriterionProtoOuterClass;
import org.onosproject.grpc.net.flow.instructions.models.InstructionProtoOuterClass;
import org.onosproject.grpc.net.flow.models.*;
import org.onosproject.grpc.net.models.PortProtoOuterClass;
import org.onosproject.grpc.net.packet.models.OutboundPacketProtoOuterClass.OutboundPacketProto;
import org.onosproject.grpc.net.packet.models.OutboundPacketProtoOuterClass.PacketOutStatus;
import org.onosproject.grpc.net.packet.models.PacketOutServiceGrpc;
import org.onosproject.grpc.net.packet.models.PacketOutServiceGrpc.PacketOutServiceBlockingStub;

import java.util.concurrent.TimeUnit;

public class GrpcClient {


    private final ManagedChannel channel;
    private final FlowServiceGrpc.FlowServiceBlockingStub flowServiceBlockingStub;
    private final PacketOutServiceBlockingStub packetOutServiceBlockingStub;


    public GrpcClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        flowServiceBlockingStub = FlowServiceGrpc.newBlockingStub(channel);
        packetOutServiceBlockingStub = PacketOutServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Shutdown");
    }


    public void addFlow() {



        CriterionProtoOuterClass.CriterionProto criterionProto =
                CriterionProtoOuterClass.CriterionProto.newBuilder().setTypeValue(0).build();
        TrafficSelectorProtoOuterClass.TrafficSelectorProto trafficSelectorProto =
                TrafficSelectorProtoOuterClass.TrafficSelectorProto.newBuilder()
                .addCriterion(criterionProto).build();

        try {
            FlowRuleProto flowRuleProto = FlowRuleProto.newBuilder().build();

            Status stats  = flowServiceBlockingStub



                    .addFlow(flowRuleProto);
            System.out.println("Stat: " + stats.getStat());

        } catch (RuntimeException e) {
            System.out.println(e.getLocalizedMessage());
            return;
        }
    }

    public void PacketOut()
    {


        InstructionProtoOuterClass.InstructionProto instructionProto  =
                InstructionProtoOuterClass.InstructionProto.newBuilder().setType(InstructionProtoOuterClass.TypeProto.OUTPUT)
                        .setPort(PortProtoOuterClass.PortProto.newBuilder().setPortNumber("2").build())
                        .build();

        TrafficTreatmentProtoOuterClass.TrafficTreatmentProto trafficTreatmentProto =
                TrafficTreatmentProtoOuterClass.TrafficTreatmentProto.newBuilder()
                        .addAllInstructions(instructionProto).build();

        OutboundPacketProto outboundPacketProto = OutboundPacketProto.newBuilder()
                .setDeviceId("of:0000000000000001")
                .setTreatment(trafficTreatmentProto)
                .setData(ByteString.copyFromUtf8("ffdddeewwww"))
                .build();



        PacketOutStatus packetOutStatus = packetOutServiceBlockingStub.emit(outboundPacketProto);

        System.out.println("Packet Out Status" + packetOutStatus.getStat());



    }




}
