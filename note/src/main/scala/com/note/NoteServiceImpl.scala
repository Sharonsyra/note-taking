package com.note

import java.util.UUID

import akka.NotUsed
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.namely.protobuf.account.commands.{ChangeNote, CreateNote, DeleteNote, GetNote}
import com.namely.protobuf.account.common.{ListNotes, Note}
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
      CreateNote()
          .withNoteId(entityId)
          .withNoteTitle(req.noteTitle)
          .withNoteContent(req.noteContent),
      Map.empty[String, String])
      .map(rst => rst.state)
  }

  override def getNote(id: String): ServiceCall[GetNote, Note] = ???

  override def listNotes(): ServiceCall[NotUsed, ListNotes] = ???

  override def editNote(id: String): ServiceCall[ChangeNote, Note] = ???

  override def deleteNote(id: String): ServiceCall[DeleteNote, Note] = ???

  override def aggregateStateCompanion: GeneratedMessageCompanion[_ <: scalapb.GeneratedMessage] = Note
}
