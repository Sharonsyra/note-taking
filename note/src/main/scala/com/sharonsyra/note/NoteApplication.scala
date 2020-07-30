package com.sharonsyra.note

import com.lightbend.lagom.scaladsl.akka.discovery.AkkaDiscoveryComponents
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.sharonsyra.note.api.NoteService
import com.sharonsyra.protobuf.note.common.Note
import com.softwaremill.macwire._
import io.superflat.lagompb.encryption.{NoEncryption, ProtoEncryption}
import io.superflat.lagompb.{AggregateRoot, BaseApplication, CommandHandler, EventHandler}

abstract class NoteApplication(context: LagomApplicationContext) extends BaseApplication(context) {

  lazy val eventHandler: EventHandler[Note] = wire[NoteEventHandler]
  lazy val commandHandler: CommandHandler[Note] = wire[NoteCommandHandler]
  lazy val aggregate: AggregateRoot[Note] = wire[NoteAggregate]
  lazy val encryptor: ProtoEncryption = NoEncryption

  override def aggregateRoot: AggregateRoot[_] = aggregate

  override def server: LagomServer =
    serverFor[NoteService](wire[NoteServiceImpl])

  // Let us hook in the readSide Processor
  lazy val journalMigrationRepository: NoteJournalMigrationRepository =
    wire[NoteJournalMigrationRepository]

  lazy val journalMigrationProcessor: NoteJournalMigrationProcessor = wire[NoteJournalMigrationProcessor]

  journalMigrationProcessor.init()

}

class NoteApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new NoteApplication(context) with AkkaDiscoveryComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new NoteApplication(context) with LagomDevModeComponents

  override def describeService: Option[Descriptor] = Some(readDescriptor[NoteService])
}
