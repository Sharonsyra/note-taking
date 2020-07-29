package com.sharonsyra.note

import lagompb.io.superflat.lagompb.readside.utils.SlickPgRepository
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class NoteJournalMigrationRepository(database: Database)
  extends SlickPgRepository[JournalTable, JournalEntity](TableQuery[JournalTable], database) {

  override def createSchema(): PostgresProfile.api.DBIOAction[Unit, PostgresProfile.api.NoStream, Effect.Schema] =
    query.schema.createIfNotExists

  override def save(model: JournalEntity): Future[JournalEntity] = {
    val insert = query.returning(query) += model
    database.run(insert)
  }

  override def all(): Future[Seq[JournalEntity]] = database.run(query.result)

  override def delete(entityId: String): Future[Option[JournalEntity]] = ???

  override def read(entityId: String): Future[Option[JournalEntity]] = ???

  override def update(entityId: String, model: JournalEntity): Future[Int] = ???

}