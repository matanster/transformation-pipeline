import com.articlio.selfMonitor.{Monitor}
import java.io.{File}
import java.nio.file.{Path, Paths, Files}
import org.apache.commons.io.FileUtils.{deleteDirectory}
import sys.process._ 

//
// Takes an input directory, and transforms each file into a new directory.
// The idea is that this can be extended to create a whole pipeline of transformation, 
// while making every step traceable - as each transformation's output has its own directory
// and the file name remains the same across all directories.
//

object Boot extends App {

  Monitor

  val sourceDirName = "elife-articles(XML)"
  val sourceDir = new File(sourceDirName)

  val targetDirName = "data"
  val targetDir = Paths.get(targetDirName)
  
  if (Files.exists(targetDir)) deleteDirectory(new File(targetDirName))
  Files.createDirectory(targetDir)

  try {

    val files = sourceDir.listFiles.filter(file => (file.isFile && file.getName.endsWith(".xml")))

    // running concurrently via .par - scala will employ some parallelism by multithreding, matching the number of free cores
    files.par.foreach (file => (s"xmllint --format $sourceDirName/${file.getName}" #> new File(s"$targetDirName/${file.getName}")).!)

    } finally {
        // closing stuff - to be moved to own function
        Monitor.shutdown
      }
}