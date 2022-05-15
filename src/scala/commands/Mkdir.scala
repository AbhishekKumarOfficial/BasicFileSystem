package scala.commands
import scala.files.{DirEntry, Directory}
import scala.filesystem.State

class Mkdir(name: String) extends Command {

  def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
    if (path.isEmpty) currentDirectory.addEntry(newEntry)
    else {
      val oldEntry = currentDirectory.findEntry(path.head).asDirectory
      currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))

    }
  }

  def doMkdir(state: State, name: String): State = {
    val wd = state.wd

    // 1. all the directories in the full path
    val allDirsInPath = wd.getAllFoldersInPath

    // 2. create a new directory entry in working directory
    val newDir = Directory.empty(wd.path, name)

    // 3. update whole directory structure starting from root
    // (directory structure is IMMUTABLE)
    val newRoot = updateStructure(state.root, allDirsInPath, newDir)

    // 4. find new working directory INSTANCE given wd's full path, in the NEW directory structure
    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
  }

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }

  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(name)) {
      state.setMessage("Entry: " + name + " already exists!")
    } else if (name.contains(Directory.SEPARATOR)) {
      state.setMessage(name + " must not contain separators")
    } else if (checkIllegal(name)) {
      state.setMessage(name + " illegal entry name!")
    } else {
      doMkdir(state, name)
    }
  }
}
