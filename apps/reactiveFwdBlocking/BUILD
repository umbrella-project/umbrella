load("//tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "//protobuf/proto:onos-protobuf-models-proto",
    "//protobuf/proto:FlowRule_proto",
    "//protobuf/proto:onos-packet-out-service-grpc",
    "//protobuf/proto:OutboundPacket_proto",
    "//api/flowservice:umbrella-flow-service-api",
    "//api/topostore:umbrella-topostore-api",
    "//api/notificationservice:umbrella-notification-service-api",
    "//api/eventServiceApi:dnos-event-service-api",
    "//core/notificationService/eventConsumerService:dnos-event-consumer-service",
    "//core/notificationService/packetService:dnos-packet-service",
    "//drivers/onos/topostore/impl:umbrella-onos-driver-topostore",
    "//drivers/onos/flowservice/impl:umbrella-onos-driver-flowservice",
    "//drivers/onos/onosurls:umbrella-onos-urls",
    "//drivers/controller:umbrella-driver-controller",
    "//tools/utility:umbrella-utility-rest-tools",
    "//config:umbrella-config-service",
    "@com_google_protobuf//:protobuf_java",
    "@com_googlecode_json_simple//jar",
    "@onos_openflow//jar",
    "@io_netty//jar",
    "@guava//jar",
    "@kafka_clients//jar",
    "@httpcomponents_httpcore//jar",
    "@onlab_misc//jar",
    "@io_grpc_grpc_java//core",
    "@io_grpc_grpc_java//stub",
    "@io_grpc_grpc_java//netty",
]

java_binary(
    name = "reactiveFwdKafkaBlocking_deploy",
    srcs = glob(["*.java"]),
    main_class = "apps.reactiveForwardingKafka.ReactiveForwardingKafka",
    deps = COMPILE_DEPS,
)
