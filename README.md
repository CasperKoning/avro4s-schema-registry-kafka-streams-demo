# Integrating Kafka Streams, Confluent Schema Registry, and Avro4s
This is an example project that demos that it is possible to integrate Kafka Streams, Confluent Schema Registry, and Avro4s.

We have a simple Kafka Streams application, for which serialization to Kafka will be done by the `KafkaAvroDeserializer` and `KafkaAvroSerializer`, which leverage the schema validation in Confluent Schema Registry. 
We use Avro4s to easily add Avro support to our Scala case classes.

## Dependencies
During this demo, we assume that an installation of [Confluent Platform](https://www.confluent.io/download/) is installed in `$CONFLUENT_HOME`

## Setup
- Start up Zookeeper
```bash
$CONFLUENT_HOME/bin/zookeeper-server-start $CONFLUENT_HOME/etc/kafka/zookeeper-properties
```
- Start up Kafka
```bash
$CONFLUENT_HOME/bin/kafka-server-start $CONFLUENT_HOME/etc/kafka/server.properties
```
- Start up Schema Registry
```bash
$CONFLUENT_HOME/bin/schema-registry-start $CONFLUENT_HOME/etc/schema-registry/schema-registry.properties
```
- Boot Writer application
- Start up ordinary console-producer and produce on input topic, and publish some messages in the `key;value` format
```bash
$CONFLUENT_HOME/bin/kafka-console-producer --topic values --broker-list localhost:9092 --property "parse.key=true" --property "key.separator=;"
 
key;value 
```
- Use avro-console-consumer to consume from output topic
```bash
$CONFLUENT_HOME/bin/kafka-avro-console-consumer --topic avro-values --from-beginning --zookeeper localhost:2181
```
- Note that the schemas have been registered at Schema Registry
```bash
curl localhost:8081/subjects/
```
- Boot Reader application
- Start up ordinary console-consumer to consume from second output topic
```bash
$CONFLUENT_HOME/bin/kafka-console-consumer --topic upper-case-values
```
