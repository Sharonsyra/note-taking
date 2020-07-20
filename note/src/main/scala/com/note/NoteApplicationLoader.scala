package com.note

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._

class HelloApplicationLoader extends LagomApplicationLoader {
  override def loadDevMode(context: LagomApplicationContext) =
    new NoteApplication(context) with LagomDevModeComponents

  override def load(context: LagomApplicationContext) =
    new NoteApplication(context) {
      override def serviceLocator = ServiceLocator.NoServiceLocator
    }

  override def describeService = Some(readDescriptor[NoteService])
}
