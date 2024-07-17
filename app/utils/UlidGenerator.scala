package utils

trait UlidGenerator {
  def generate(): String
  def isValid(ulid: String): Boolean
}
