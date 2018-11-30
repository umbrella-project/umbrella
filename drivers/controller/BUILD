load("//tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "//api/flowservice:umbrella-flow-service-api",
    "//api/topostore:umbrella-topostore-api",
]

java_library(
    name = "umbrella-driver-controller",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)