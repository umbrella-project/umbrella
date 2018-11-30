load("//tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "//drivers/controller:umbrella-driver-controller",
    "//drivers/onos/flowservice/impl:umbrella-onos-driver-flowservice",
    "//drivers/onos/topostore/impl:umbrella-onos-driver-topostore",


]

java_library(
    name = "umbrella-onos-driver-controller",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)