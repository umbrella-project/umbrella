#!/bin/bash
# -----------------------------------------------------------------------------
# Generates catalog of third-party worspace dependencies for Bazel
# -----------------------------------------------------------------------------

set -e


java -jar  ./target/onos-libgen-2.1-SNAPSHOT.jar ./deps.json ./generate_workspace.bzl --bazel
