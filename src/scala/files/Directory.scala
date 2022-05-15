package scala.files

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry]) extends DirEntry(parentPath, name) {
  def findDescendant(path: List[String]): Directory = ???

  def getAllFoldersInPath: List[String] = {
    path.substring(1).split(Directory.SEPARATOR).toList
    // /a/b/c/d => List["a", "b", "c", "d"]
  }

  def hasEntry(name: String): Boolean = ???

  def addEntry(newEntry: DirEntry): Directory = ???

  def findEntry(entryName: String): DirEntry = ???

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory = ???

  def asDirectory: Directory = this
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory =
    Directory.empty("", "")

  def empty(parentPath: String, name: String): Directory =
    new Directory(parentPath, name, List())

}