package com.note

import com.lightbend.lagom.scaladsl.akka.discovery.AkkaDiscoveryComponents
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.namely.protobuf.account.common.Note
import com.softwaremill.macwire._
import io.superflat.lagompb.{AggregateRoot, BaseApplication, CommandHandler, EventHandler}

abstract class NoteApplication(context: LagomApplicationContext) extends BaseApplication(context) {

  lazy val eventHandler: EventHandler[Note] = wire[NoteEventHandler]
  lazy val commandHandler: CommandHandler[Note] = wire[NoteCommandHandler]
  lazy val aggregate: AggregateRoot[Note] = wire[NoteAggregate]

  override def aggregateRoot: AggregateRoot[_] = aggregate

  override def server: LagomServer =
    serverFor[NoteService](wire[NoteServiceImpl])

}

class NoteApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new NoteApplication(context) with AkkaDiscoveryComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new NoteApplication(context) with LagomDevModeComponents

  override def describeService: Option[Descriptor] = Some(readDescriptor[NoteService])
}
