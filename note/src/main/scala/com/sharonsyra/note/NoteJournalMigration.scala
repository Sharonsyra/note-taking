package com.sharonsyra.note

import akka.Done
import akka.actor.typed.ActorSystem
import com.sharonsyra.protobuf.note.common.Note
import io.superflat.lagompb.GlobalException
import io.superflat.lagompb.encryption.ProtoEncryption
import io.superflat.lagompb.protobuf.core.MetaData
import io.superflat.lagompb.readside.ReadSideProcessor
import scalapb.{GeneratedMessage, GeneratedMessageCompanion}
import slick.dbio.{DBIO, DBIOAction}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * lagom-pb read side using akka projection
 *
 * @param actorSystem
 * @param repository
 * @param ec
 */
class NoteJournalMigration(encryption: ProtoEncryption, actorSystem: ActorSystem[_], repository: NoteJournalMigrationRepository)(
  implicit ec: ExecutionContext
) extends ReadSideProcessor[Note](encryption)(ec, actorSystem) {

  override def handle(event: scalapb.GeneratedMessage, state: Note, metaData: MetaData): DBIO[Done] = {
    case _ : GeneratedMessage =>
      Future {
        repository.save(
          JournalEntity(
            ordering = 1,
            persistenceId = "",
            sequenceNumber = 1,
            deleted = false,
            tags = Some(""),
            message = event.toByteArray
          )
        )
      }

//      onComplete {
//        case Success(value) => value
//        case Failure(exception) => exception.getMessage
//      }


    case _ =>
      DBIOAction.failed(throw new GlobalException(s" event ${event.companion.scalaDescriptor.fullName} not handled"))

  }

  override def aggregateStateCompanion: GeneratedMessageCompanion[Note] = Note

  override def projectionName: String = "akka-journal-migration-projection"
}