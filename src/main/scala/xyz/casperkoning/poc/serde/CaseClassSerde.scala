package xyz.casperkoning.poc.serde

import com.sksamuel.avro4s._
import io.confluent.kafka.serializers._
import org.apache.avro.generic._
import org.apache.kafka.common.serialization._

class CaseClassSerde[CC](schemaRegistryUrl: String = "http://localhost:8081", isKey: Boolean)(implicit format: RecordFormat[CC]) extends Serde[CC] {
  private class CaseClassDeserializer(schemaRegistryUrl: String = "http://localhost:8081", isKey: Boolean)(implicit format: RecordFormat[CC]) extends Deserializer[CC] {
    private val deserializer = {
      val s = new KafkaAvroDeserializer()
      val configs = new java.util.HashMap[String, Any]()
      configs.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)
      configs.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "false")
      s.configure(configs, isKey)
      s
    }

    override def configure(configs: java.util.Map[String, _], isKey: Boolean): Unit = {}

    override def close(): Unit = deserializer.close()

    override def deserialize(topic: String, data: Array[Byte]): CC = {
      val record = deserializer.deserialize(topic, data).asInstanceOf[GenericRecord]
      format.from(record)
    }
  }

  private class CaseClassSerializer(schemaRegistryUrl: String = "http://localhost:8081", isKey: Boolean)(implicit format: RecordFormat[CC]) extends Serializer[CC] {
    private val serializer = {
      val s = new KafkaAvroSerializer()
      val configs = new java.util.HashMap[String, Any]()
      configs.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)
      s.configure(configs, isKey)
      s
    }

    override def configure(configs: java.util.Map[String, _], isKey: Boolean): Unit = {}

    override def close(): Unit = serializer.close()

    override def serialize(topic: String, data: CC): Array[Byte] = {
      val record = format.to(data)
      serializer.serialize(topic, record)
    }
  }

  override def deserializer(): Deserializer[CC] = new CaseClassDeserializer(schemaRegistryUrl, isKey)

  override def serializer(): Serializer[CC] = new CaseClassSerializer(schemaRegistryUrl, isKey)

  override def configure(configs: java.util.Map[String, _], isKey: Boolean): Unit = {}

  override def close(): Unit = {}
}
