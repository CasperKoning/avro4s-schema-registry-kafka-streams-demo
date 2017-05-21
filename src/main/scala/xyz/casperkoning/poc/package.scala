package xyz.casperkoning

import org.apache.kafka.streams._

package object poc {
  implicit def tupleToKeyValue[T1, T2](tuple: (T1, T2)): KeyValue[T1, T2] = new KeyValue[T1, T2](tuple._1, tuple._2)
}
