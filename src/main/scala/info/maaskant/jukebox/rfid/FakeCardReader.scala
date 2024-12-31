//package info.maaskant.jukebox.rfid
//
//import cats.effect.std.Random
//import cats.effect.{IO, Resource}
//import com.typesafe.scalalogging.StrictLogging
//
//class FixedUidReader(items: IndexedSeq[Option[Uid]]) extends CardReader[Unit] with StrictLogging {
//  def read(): Option[Card] =
//    Random[IO].shuffleList(items.toList)
//      .from(items)
//      .map { _.map(Card.apply(_, "Fake Card")) }
//
//  override def resource(): Resource[IO, Unit] = Resource.unit
//
//  override def read(resources: T): Option[Card] = ???
//}
//
//class TimeBasedReader extends CardReader with StrictLogging {
//  def read(): Observable[Option[Card]] =
//    Observable.repeatEval {
//      val uid = System.currentTimeMillis / 1000 / 5
//      Some(Card(Uid(s"fake-$uid"), "Fake Card"))
//    }
//}
