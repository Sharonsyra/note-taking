package com.sharonsyra.note

import java.time.Instant

import akka.actor.ActorSystem
import com.sharonsyra.protobuf.note.events._
import com.sharonsyra.protobuf.note.common._
import io.superflat.lagompb.EventHandler
import io.superflat.lagompb.protobuf.core.MetaData
import scalapb.GeneratedMessage

class NoteEventHandler(actorSystem: ActorSystem) extends EventHandler[Note](actorSystem) {

  import common.instantToTimestamp

  override def handle(event: GeneratedMessage, state: Note, metaData: MetaData): Note = {
    event match {
      case event: NoteCreated => handleNoteCreated(event, state)
      case _ => throw new NotImplementedError()
    }
  }

  private def handleNoteCreated(event: NoteCreated, state: Note): Note =
    state.update(
      _.noteUuid := event.noteUuid,
      _.noteTitle := event.noteTitle,
      _.noteContent := event.noteContent,
      _.createdAt := instantToTimestamp(Instant.now()),
      _.updatedAt := instantToTimestamp(Instant.now())
    )

}
