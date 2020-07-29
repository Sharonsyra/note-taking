package com.sharonsyra.note

import java.util.UUID

import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.sharonsyra.protobuf.note.commands._
import com.sharonsyra.protobuf.note.common._
import com.sharonsyra.note.api.NoteService
import io.superflat.lagompb.{AggregateRoot, BaseServiceImpl}
import scalapb.GeneratedMessageCompanion

import scala.concurrent.ExecutionContext

class NoteServiceImpl(
  clusterSharding: ClusterSharding,
  persistentEntityRegistry: PersistentEntityRegistry,
  aggregate: AggregateRoot[Note]
)(
  implicit ec: ExecutionContext
) extends BaseServiceImpl(clusterSharding, persistentEntityRegistry, aggregate) with NoteService {

  override def createNote: ServiceCall[CreateNote, Note] = req => {

    val entityId: String = UUID.randomUUID().toString

    sendCommand[CreateNote, Note](
      entityId,
      CreateNote()
          .withNoteUuid(entityId)
          .withNoteTitle(req.noteTitle)
          .withNoteContent(req.noteContent),
      Map.empty[String, String])
      .map(rst => rst.state)
  }

  override def getNote(id: String): ServiceCall[GetNote, Note] = { _ =>
    sendCommand[GetNote, Note](
      GetNote()
        .withNoteUuid(id),
      Map.empty[String, String])
      .map(rst => rst.state)
  }

//  override def listNotes(): ServiceCall[NotUsed, ListNotes] = { _ =>
//    sendCommand[GetNote, Note](
//      GetNote(),
//      Map.empty[String, String])
//      .map(rst => rst.state)
//  }

  override def editNote(id: String): ServiceCall[ChangeNote, Note] = { _ =>
    sendCommand[ChangeNote, Note](
      ChangeNote()
        .withNoteUuid(id),
      Map.empty[String, String])
      .map(rst => rst.state)
  }

  override def deleteNote(id: String): ServiceCall[DeleteNote, Note] = { _ =>
    sendCommand[DeleteNote, Note](
      DeleteNote()
        .withNoteUuid(id),
      Map.empty[String, String])
      .map(rst => rst.state)
  }

  override def aggregateStateCompanion: GeneratedMessageCompanion[_ <: scalapb.GeneratedMessage] = Note
}
