load("//tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "//api/flowservice:umbrella-flow-service-api",
    "@org_apache_httpcomponents//jar",
    "@com_googlecode_json_simple//jar",
    "@httpcomponents_httpcore//jar",
    "//tools/utility:umbrella-utility-rest-tools",
    "//drivers/onos/onosurls:umbrella-onos-urls",



]

java_library(
    name = "umbrella-onos-driver-flowservice",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)