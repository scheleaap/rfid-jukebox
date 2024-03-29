package info.maaskant.jukebox.rfid

object ByteUtils {
  // Source: https://alvinalexander.com/source-code/scala-how-to-convert-array-bytes-to-hex-string
  def convertBytesToHex(bytes: Array[Byte]): String = {
    val sb = new StringBuilder
    for (b <- bytes) {
      sb.append(String.format("%02x", Byte.box(b)))
    }
    sb.toString
  }
}
