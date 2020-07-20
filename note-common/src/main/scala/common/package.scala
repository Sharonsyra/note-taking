import java.time.Instant

import com.namely.protobuf.account.common.Timestamp

package object common {

  def instantToTimestamp(instant: Instant): Timestamp = {
    Timestamp()
      .withNanos(instant.getNano)
      .withSeconds(instant.getEpochSecond)
  }
}
