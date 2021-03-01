JARS := $(shell  ls *.jar | sed -e '/.*/s/^/\.\//' | tr '\n' ':')
JAVAC_FLAGS = -cp ${JARS}

clean:
	rm -rf *.class
build:	clean
	javac ${JAVAC_FLAGS} *.java 
