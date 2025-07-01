FROM ubuntu:latest
LABEL authors="ORC"

ENTRYPOINT ["top", "-b"]