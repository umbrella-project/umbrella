load("//tools/build/bazel:generate_workspace.bzl", "generated_maven_jars")

generated_maven_jars()

load("//tools/build/bazel:grpc_workspace.bzl", "generate_grpc")

generate_grpc()

load("@io_grpc_grpc_java//:repositories.bzl", "grpc_java_repositories")

grpc_java_repositories()
