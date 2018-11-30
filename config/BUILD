load("//tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "//drivers/controller:umbrella-driver-controller",
    "//drivers/onos:umbrella-onos-driver-controller",


]

java_library(
    name = "umbrella-config-service",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)