package xyz.casperkoning.poc

import java.util._

import com.sksamuel.avro4s._
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream._

import domain._
import serde._

object ReadAvroExample {
  def main(args: Array[String]): Unit = {
    val streamingConfig = {
      val props = new Properties
      props.put(StreamsConfig.APPLICATION_ID_CONFIG, "reader")
      props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      props
    }

    implicit val personFormat = RecordFormat[Value]
    val keySerde = new CaseClassSerde[Key](isKey = true)
    val personSerde = new CaseClassSerde[Value](isKey = false)

    val builder = new KStreamBuilder
    val persons = builder.stream(keySerde, personSerde, "avro-values")

    persons
      .map[String, String]((key, value) => (key.key, value.value.toUpperCase))
      .to(Serdes.String(), Serdes.String(), "upper-case-values")

    val streams = new KafkaStreams(builder, streamingConfig)
    streams.start()
  }

}
