#!/usr/bin/env bash

usage() {
    cat <<EOM
    Usage:
    $(basename $0) version
EOM
    exit 0
}

[[ $# -ne 1 ]] && { usage; }

echo "version := \"$1\"" > version.sbt
sbt "Debian / packageBin"
