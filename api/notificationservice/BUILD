load("//tools/build/bazel:generate_workspace.bzl", "COMPILE")

COMPILE_DEPS = COMPILE + [
    "//drivers/controller:umbrella-driver-controller",
]

java_library(
    name = "umbrella-notification-service-api",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)