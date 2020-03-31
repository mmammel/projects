%.class: %.java
	javac -classpath ./commons-math3-3.6.1.jar *.java
clean:
	rm *.class
runScore: Driver.class
	java -classpath .:./commons.math3-3.6.1.jar Driver $1 $2
runDensity: DensityFactory.class
	java -classpath .:./commons-math3-3.6.1.jar DensityFactory
