load("//tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "@org_apache_httpcomponents//jar",
    "@com_googlecode_json_simple//jar",
    "@httpcomponents_httpcore//jar",
]

java_library(
    name = "umbrella-utility-rest-tools",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)