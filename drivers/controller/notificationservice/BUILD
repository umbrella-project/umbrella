load("//tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "//drivers/controller:umbrella-driver-controller",
    "//api/notificationservice:umbrella-notification-service-api",
    "//api/topostore:umbrella-topostore-api",
    "@jgraph_core//jar"
]

java_library(
    name = "umbrella-driver-notificationservice",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)