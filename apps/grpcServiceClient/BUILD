load("///tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "//protobuf/proto:onos-protobuf-models-proto",
    "//protobuf/proto:onos-flow-service-grpc",
    "//protobuf/proto:FlowRule_proto",
    "//protobuf/proto:onos-packet-out-service-grpc",
    "//protobuf/proto:OutboundPacket_proto",
    "@com_google_protobuf//:protobuf_java",
    "@onos_openflow//jar",
    "@io_netty//jar",
    "@guava//jar",
    "@onlab_misc//jar",
    "@io_grpc_grpc_java//core",
    "@io_grpc_grpc_java//stub",
    "@io_grpc_grpc_java//netty",
]

java_binary(
    name = "grpcClient_deploy",
    srcs = glob(["*.java"]),
    main_class = "apps.grpcServiceClient.GrpcClient",
    deps = COMPILE_DEPS,
)
