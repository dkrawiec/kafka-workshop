#!/bin/bash
kafka-topics --bootstrap-server broker-1:29092 --create --topic order-placed --partitions 10 --replication-factor 2
kafka-topics --bootstrap-server broker-1:29092 --create --topic product-stats --partitions 10 --replication-factor 2
kafka-topics --bootstrap-server broker-1:29092 --create --topic product-ready --partitions 10 --replication-factor 2
kafka-topics --bootstrap-server broker-1:29092 --create --topic order-ready --partitions 10 --replication-factor 2