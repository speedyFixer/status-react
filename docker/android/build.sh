#!/usr/bin/env bash

set -e -x

export FASTLANE_DISABLE_COLORS=1
export REALM_DISABLE_ANALYTICS=1

make clean
# Prep
bundle install --quiet
make prepare-android
./scripts/gen_build_no.sh
# Lint
lein cljfmt check
# Test
lein test-cljs
# Build
lein prod-build-android
# Compile
./scripts/build-android.sh debug
