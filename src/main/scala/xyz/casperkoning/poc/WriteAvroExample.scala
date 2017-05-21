package xyz.casperkoning.poc

import java.util._

import com.sksamuel.avro4s._
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream._

import domain._
import serde._

object WriteAvroExample {
  def main(args: Array[String]): Unit = {
    val streamingConfig = {
      val props = new Properties
      props.put(StreamsConfig.APPLICATION_ID_CONFIG, "writer")
      props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      props
    }

    implicit val personFormat = RecordFormat[Value]
    val keySerde = new CaseClassSerde[Key](isKey = true)
    val personSerde = new CaseClassSerde[Value](isKey = false)

    val builder = new KStreamBuilder
    val values = builder.stream(Serdes.String(), Serdes.String(), "values")

    values
      .map[Key, Value]((key, value) => (Key(key), Value(value)))
      .to(keySerde, personSerde, "avro-values")

    val streams = new KafkaStreams(builder, streamingConfig)
    streams.start()
  }
}
