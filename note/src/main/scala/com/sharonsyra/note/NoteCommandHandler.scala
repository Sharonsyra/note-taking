package com.sharonsyra.note

import akka.actor.ActorSystem
import com.google.protobuf.any.Any
import com.sharonsyra.protobuf.note.commands._
import com.sharonsyra.protobuf.note.common._
import com.sharonsyra.protobuf.note.events.NoteCreated
import io.superflat.lagompb.protobuf.core._
import io.superflat.lagompb.{Command, CommandHandler}

import scala.util.{Failure, Success, Try}

class NoteCommandHandler(actorSystem: ActorSystem) extends CommandHandler[Note](actorSystem) {
  override def handle(command: Command, state: Note, currentMetaData: MetaData): Try[CommandHandlerResponse] = {
    command.command match {
      case command: CreateNote => Try(handleCreateNote(command, state))
      case command: GetNote => Try(handleGetNote(command, state))
    }
  }

  private def handleCreateNote(command: CreateNote, state: Note): CommandHandlerResponse = {
    Try(require(command.noteTitle.nonEmpty, "Note title cannot be empty!"))
      .map(_ => require(command.noteContent.nonEmpty, "Note content cannot be empty!")) match {
      case Success(_) =>
        CommandHandlerResponse()
          .withSuccessResponse(
            SuccessCommandHandlerResponse()
              .withEvent(
                Any.pack(
                  NoteCreated()
                      .withNoteUuid(command.noteUuid)
                      .withNoteTitle(command.noteTitle)
                      .withNoteContent(command.noteContent)
                )
              )
          )
      case Failure(exception) =>
        CommandHandlerResponse()
          .withFailedResponse(
            FailedCommandHandlerResponse()
              .withCause(FailureCause.InternalError)
              .withReason(exception.getMessage.replace("requirement failed: ", ""))
          )
    }
  }

  private def handleGetNote(command: GetNote, state: Note): CommandHandlerResponse = {
    Try(require(state.noteUuid.equals(command.noteUuid), s"invalid note id ${command.noteUuid} sent")) match {
      case Success(_) =>
        CommandHandlerResponse()
          .withSuccessResponse(
            SuccessCommandHandlerResponse()
              .withNoEvent(com.google.protobuf.empty.Empty())
          )
      case Failure(exception) =>
        CommandHandlerResponse()
          .withFailedResponse(
            FailedCommandHandlerResponse()
              .withCause(FailureCause.InternalError)
              .withReason(exception.getMessage.replace("requirement failed: ", ""))
          )
    }
  }

}
