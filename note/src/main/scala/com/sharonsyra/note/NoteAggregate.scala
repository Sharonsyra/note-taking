package com.sharonsyra.note

import akka.actor.ActorSystem
import com.sharonsyra.protobuf.note.common.Note
import io.superflat.lagompb.{AggregateRoot, CommandHandler, EventHandler}
import scalapb.GeneratedMessageCompanion

final class NoteAggregate(
  actorSystem: ActorSystem,
  commandHandler: CommandHandler[Note],
  eventHandler: EventHandler[Note]
) extends AggregateRoot[Note](actorSystem, commandHandler, eventHandler) {

  override def aggregateName: String = "Note"

  override def stateCompanion: GeneratedMessageCompanion[Note] = Note

}
