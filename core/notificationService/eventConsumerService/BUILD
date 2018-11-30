load("//tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "//tools/utility:umbrella-utility-rest-tools",
    "@kafka_clients//jar",
    "@com_googlecode_json_simple//jar",
    "@httpcomponents_httpcore//jar",
    "@org_apache_httpcomponents//jar",
]

java_library(
    name = "dnos-event-consumer-service",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)
