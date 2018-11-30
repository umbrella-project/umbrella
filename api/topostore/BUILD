load("//tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "@jgraph_core//jar",
    "@onlab_misc//jar"
]

java_library(
    name = "umbrella-topostore-api",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)