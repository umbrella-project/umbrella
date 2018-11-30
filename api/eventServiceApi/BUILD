load("//tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "//protobuf/proto:onos-protobuf-models-proto",
    "@kafka_clients//jar",
]

java_library(
    name = "dnos-event-service-api",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)
