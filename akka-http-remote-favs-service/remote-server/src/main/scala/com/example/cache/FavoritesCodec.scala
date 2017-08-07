package com.example.cache

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, Closeable, ObjectOutputStream}

import shade.memcached.{Codec, GenericCodecObjectInputStream}

import scala.reflect.ClassTag
import scala.util.control.NonFatal

class FavoritesCodec extends Codec[Favorites] {

  def using[T <: Closeable, R](obj: T)(f: T => R): R =
    try
      f(obj)
    finally
      try obj.close() catch {
        case NonFatal(_) => // does nothing
      }

  def serialize(value: Favorites): Array[Byte] =
    using(new ByteArrayOutputStream()) { buf =>
      using(new ObjectOutputStream(buf)) { out =>
        out.writeObject(value)
        out.close()
        buf.toByteArray
      }
    }

  def deserialize(data: Array[Byte]): Favorites =
    using(new ByteArrayInputStream(data)) { buf =>
      val in = new GenericCodecObjectInputStream(ClassTag(classOf[Favorites]), buf)
      using(in) { inp =>
        inp.readObject().asInstanceOf[Favorites]
      }
    }
}
