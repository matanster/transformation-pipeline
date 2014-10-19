import com.articlio.selfMonitor.{Monitor}

object Boot extends App {

  Monitor

  try {


    
    } finally {
        // closing stuff - to be moved to own function
        Monitor.shutdown
      }
}