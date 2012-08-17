import java.io.BufferedReader
import java.io.PrintWriter

def server = new ServerSocket(17290)
while(true) {
  server.accept() {socket ->

    if (socket.getInetAddress().toString().matches("/?127\\.0\\.0\\.1")) {
      socket.withStreams {input, output ->
        PrintWriter out = new PrintWriter( output, true )
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String r = reader.readLine();
        int returnCode = -1;

        println("Executing: \"${r}\"");

        try {

          def proc = "${r}".execute()

          proc.waitFor()

          returnCode = proc.exitValue()

          out.println( '' + returnCode )

          BufferedReader procIn;

          if( proc.exitValue() > 0 )
          {
            println "Error ${proc.exitValue()}"
            procIn = new BufferedReader( new InputStreamReader(proc.getErrorStream()))
          }
          else
          {
            println "Success"
            procIn = new BufferedReader( new InputStreamReader(proc.getInputStream()))
          }

          String tempStr = null;

          while( (tempStr = procIn.readLine()) != null )
          {
            out.println(tempStr)
          }
        }
        catch (IOException ioe) {
          retVal = "Error Executing command \"${r}\": ${ioe.toString()}"
          out.println('1')
          out.println( retVal )
        }

        out.close()
        reader.close()
        socket.close()
      }
    }
  }
}

