JAVAC_FLAGS = -classpath .:./xercesImpl.jar:./xalan.jar

PARSER_FILES = BasicErrorHandler.java AbstractErrorHandler.java PrintingContentHandler.java AbstractContentHandler.java GenericParser.java

XPATH_FILES = XPathArray.java XPathStack.java StringArrayComparator.java

all:
	javac ${JAVAC_FLAGS} ${PARSER_FILES} ${XPATH_FILES}
parser:
	javac ${JAVAC_FLAGS} ${PARSER_FILES}
xpath:
	javac ${JAVAC_FLAGS} ${XPATH_FILES}
instr:
	"${TFJ_JAVAC}" ${JAVAC_FLAGS} ${PARSER_FILES} ${XPATH_FILES}
	"${TFJ_INSTR}" -full *.class
clean:
	rm -rf *.class core &> /dev/null
