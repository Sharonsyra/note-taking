import java.time.Instant

import com.sharonsyra.protobuf.note.common.Timestamp

package object common {

  def instantToTimestamp(instant: Instant): Timestamp = {
    Timestamp()
      .withNanos(instant.getNano)
      .withSeconds(instant.getEpochSecond)
  }
}
