package com.note

import akka.actor.ActorSystem
import com.google.protobuf.any.Any
import com.namely.protobuf.account.commands.{CreateNote, GetNote}
import com.namely.protobuf.account.common.Note
import com.namely.protobuf.account.events.NoteCreated
import io.superflat.lagompb.protobuf.core._
import io.superflat.lagompb.{Command, CommandHandler}

import scala.util.{Failure, Success, Try}

class NoteCommandHandler(actorSystem: ActorSystem) extends CommandHandler[Note](actorSystem) {
  override def handle(command: Command, currentState: Note, currentMetaData: MetaData): Try[CommandHandlerResponse] = {
    command.command match {
      case command: CreateNote => Try(handleCreateNote(command, currentState))
      case command: GetNote => Try(handleGetNote(command, currentState))
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
                      .withNoteId(command.noteId)
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
    Try(require(state.noteId.equals(command.noteId), s"invalid note id ${command.noteId} sent")) match {
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
//message ChangeNote {
//  int32 note_id = 1;
//  string note_title = 2;
//  string note_content = 3;
//}
//
//message DeleteNote {
//  int32 note_id = 1;
//}