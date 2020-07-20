import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.Service.restCall
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, ServiceCall}
import com.namely.protobuf.account.commands.{ChangeNote, CreateNote, DeleteNote, GetNote}
import com.namely.protobuf.account.common.{ListNotes, Note}
import io.superflat.lagompb.BaseService

trait NoteService extends BaseService {

  def createNote: ServiceCall[CreateNote, Note]

  def getNote(id: String): ServiceCall[GetNote, Note]

  def listNotes: ServiceCall[NotUsed, ListNotes]

  def editNote(id: String): ServiceCall[ChangeNote, Note]

  def deleteNote(id: String): ServiceCall[DeleteNote, Note]

  override val routes: Seq[Descriptor.Call[_, _]] = Seq(
    restCall(Method.POST, "/api/notes", createNote),
    restCall(Method.GET, "/api/notes/:id", getNote _),
    restCall(Method.GET, "/api/notes", listNotes),
    restCall(Method.PATCH, "/api/notes/:id", editNote _),
    restCall(Method.DELETE, "/api/notes/:id", deleteNote _)
  )
}
