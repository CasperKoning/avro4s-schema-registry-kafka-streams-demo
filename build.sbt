name := "avro4s-schema-registry-kafka-streams"

version := "1.0"

scalaVersion := "2.12.2"

resolvers += "Confluent" at "http://packages.confluent.io/maven/"

val kafkaStreams =   "org.apache.kafka"    %  "kafka-streams"         % "0.10.0.0"
val avroSerializer = "io.confluent"        %  "kafka-avro-serializer" % "3.2.1"
val avro4s =         "com.sksamuel.avro4s" %% "avro4s-core"           % "1.6.4"

libraryDependencies ++= Seq(
  kafkaStreams,
  avroSerializer,
  avro4s
)
