package com.sharonsyra.note

import akka.Done
import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import com.sharonsyra.protobuf.note.common.Note
import io.superflat.lagompb.GlobalException
import io.superflat.lagompb.encryption.ProtoEncryption
import io.superflat.lagompb.readside.{ReadSideEvent, ReadSideProcessor}
import scalapb.{GeneratedMessage, GeneratedMessageCompanion}
import slick.dbio.{DBIO, DBIOAction}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext

/**
 * lagom-pb read side using akka projection
 *
 * @param actorSystem
 * @param repository
 * @param ec
 */
class NoteJournalMigrationProcessor(
  encryption: ProtoEncryption,
  actorSystem: ActorSystem,
  repository: NoteJournalMigrationRepository
)(
  implicit ec: ExecutionContext
) extends ReadSideProcessor[Note](encryption)(ec, actorSystem.toTyped) {

  val journalTable: TableQuery[JournalTable] = TableQuery[JournalTable]

  override def handle(readSideEvent: ReadSideEvent[Note]): DBIO[Done] = {
    val event = readSideEvent.event

    event match {
      case e: GeneratedMessage =>
        journalTable
          .insertOrUpdate(
            JournalEntity(
              ordering = None,
              persistenceId = persistenceId("Note", readSideEvent.metaData.entityId),
              sequenceNumber = readSideEvent.metaData.revisionNumber,
              deleted = false,
              tags = Some(readSideEvent.eventTag),
              message = readSideEvent.event.toByteArray
            )
          ).map(_ => Done)

      case _ =>
        DBIOAction.failed(throw new GlobalException(s" event ${event.companion.scalaDescriptor.fullName} not handled"))
    }

  }






  private def persistenceId(entityName: String, entityId: String): String =
    s"$entityName|$entityId"

  override def aggregateStateCompanion: GeneratedMessageCompanion[Note] = Note

  override def projectionName: String = "akka-journal-migration-projection"

}
