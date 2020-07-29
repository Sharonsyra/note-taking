package com.sharonsyra.note

import lagompb.io.superflat.lagompb.readside.utils.SlickBasedTable
import slick.jdbc.PostgresProfile.api._

final case class JournalEntity (
  ordering: Long,
  persistenceId: String,
  sequenceNumber: Long,
  deleted: Boolean,
  tags: Option[String] = None,
  message: Array[Byte]
)

class JournalTable(tag: Tag) extends SlickBasedTable[JournalEntity](tag, None, tableName="new_journal") {
  override def * =
    (
    ordering,
    persistenceId,
    sequenceNumber,
    deleted,
    tags,
    message
    ) <> (JournalEntity.tupled, JournalEntity.unapply)

  val ordering: Rep[Long] = column[Long]("ordering", O.AutoInc)
  val persistenceId: Rep[String] =
    column[String]("persistence_id", O.Length(255, varying = true))
  val sequenceNumber: Rep[Long] = column[Long]("sequence_number")
  val deleted: Rep[Boolean] = column[Boolean]("deleted", O.Default(false))
  val tags: Rep[Option[String]] =
    column[Option[String]]("tags", O.Length(255, varying = true))
  val message: Rep[Array[Byte]] = column[Array[Byte]]("message")
  val pk = primaryKey(s"${tableName}_pk", (persistenceId, sequenceNumber))
  val orderingIdx = index(s"${tableName}_ordering_idx", ordering, unique = true)

}
