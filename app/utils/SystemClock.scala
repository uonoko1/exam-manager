package utils

import java.time.ZonedDateTime

// The SystemClock trait and its implementation are defined in the same file.
// This is done solely to facilitate mocking the current time in test code.
// The purpose is not to separate logic or ensure loose coupling.

trait SystemClock {
  def now(): ZonedDateTime
}

class SystemClockImpl extends SystemClock {
  override def now(): ZonedDateTime = ZonedDateTime.now()
}
