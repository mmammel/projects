VALIDATOR Util
--------------

max.mammel@fadv.com

Purpose:

This utility was originally written to assist with the SOUKS integration, in which we found ourselves having to deal with large, unreliable data files that - if not properly formatted - could spell disaster in the production database.  The utility is designed to validate, auto-correct where possible, and convert to CSV any positional or CSV file fed to it, based on user defined properties.

Design:

The tool has a simple structure:

Driver - the keeper of the "main()" method
ValidatorFactory - given a properties file, return the Validator that must be used to process it.
RowResult - A holder of the results of a particular data row's validation/conversion
Validator - the base interface, containing the single method "validateFile"
  |
  -- AbstractValidator - the root class, contains most of the processing
       |
       -- CSVValidate - a CSV file validator/converter
       |
       -- PositionalValidate - a Positional file validator/converter

The core of the utility lies in the property files, here are some examples - one of a CSV file to be validated, and another of a positional file.

Example 1 - CSV file Validation
-------------------------------

The file to be validated has the following form:

<numeric ID>,<first name>,<last name>,<optional middle name>,<MMddyyy date stamp>,<answer1>,<answer2>,<answer3>

where the "answerX" field is an ABCDE answer for a multiple choice test.

e.g.

12345,Max,Mammel,,12142011,A,C,E

A property file that will define the validation for this file looks like this:

file.type=csv			// indicates this is a CSV file
row.numFields=8			// the number of fields in each line
fieldValidator1=[0-9]+		// field 1 must have one or more numbers in it
fieldValidator2-3=[A-Za-z]+	// fields 2 and 3 must have one or more letters
fieldValidator4=.*		// field 4 can have anything, and can be blank
fieldValidator5=^[0-9]{8}$	// field 5 must contain exactly 8 numeric characters
fieldValidator6-8=[ABCDE]	// fields 6 through 8 must be one of A,B,C,D or E

With these properties, the utility will simply reject a file that contains any rows that do not conform to these specs.  Additional properties can be added to allow for auto-correction of bad fields.  Say for instance, that any blank or otherwise invalid answers should default to "C", and if the date field is corrupt, either use the last valid date encountered in the file or today's date as a default if no valid fields exist.

fieldDefault5=last_valid,func:today(MMddyyyy)	// use the last valid field, or today's date in the speficied format
fieldDefault6-8=C				// use "C" for any invalid answers

With these additional properties any invalid date fields or answer fields will be defaulted accordingly, and the corrected rows will be output along with any originally valid rows at the end of execution.

e.g. a given file with some errors, run against the properties above:

1234,Joe,Smith,Michael,12 2011,A,X,B
12345,Tom,Anderson,,12132011,B,B,B

will output (with today's date being 12142011):

1234,Joe,Smith,Michael,12142011,A,C,B
12345,Tom,Anderson,,12132011,B,B,B

if the order of the rows was switched, the output would be:
 
12345,Tom,Anderson,,12132011,B,B,B
1234,Joe,Smith,Michael,12132011,A,C,B

because the bad date in the Tom Anderson row would be defaulted to the good date found in the first row.

NOTE: If no default for a field is provided and it is not valid against the validator defined for it, the entire file will fail.  Additionally, if a row does not contain enough fields, or enough characters to define a field (for positional) the entire file will fail regardless of defaults.

Example 2: Positional file validation
-------------------------------------

Suppose the same fields in the CSV example above are given in positional format, like so:

12345     Max            Mammel                        12142011ACE

Where the first field is 10 characters, the name fields are 15 characters, the date field is 8 characters, and each answer field is a single character.

The properties file for this file will be:

file.type=positional			// indicate that the file is positional
row.numFields=8				// Same number of fields
file.trimFields=true			// When converting to CSV, trim the fields.
fieldSpec1=1,10				// field one starts at 1, and is 10 characters long
fieldSpec2=11,15			// field 2 starts at 11, and is 15 characters
fieldSpec3=26,15			// field 3 starts at 26, and is 15 characters
fieldSpec4=41,15			// field 4 starts at 41, and is 15 characters
fieldSpec5=56,8				// date field 5 starts at 56 and is 8 characters
fieldSpec6-8=64,1			// fields 6-8 are all 1 character and the first field starts at 64.  This will automatically define fields 6,7, and 8 correctly.  Note that the same approach could have been taken for fields 2,3, and 4 with the property: fieldSpec2-4=11,15
fieldValidator1=[0-9]+		// field 1 must have one or more numbers in it
fieldValidator2-3=[A-Za-z]+	// fields 2 and 3 must have one or more letters
fieldValidator4=.*		// field 4 can have anything, and can be blank
fieldValidator5=^[0-9]{8}$	// field 5 must contain exactly 8 numeric characters
fieldValidator6-8=[ABCDE]	// fields 6 through 8 must be one of A,B,C,D or E
				// the validators are the same as the CSV example, as well as the defaults:

fieldDefault5=last_valid,func:today(MMddyyyy)	// use the last valid field, or today's date in the speficied format
fieldDefault6-8=C				// use "C" for any invalid answers

e.g. Given the following file:

12345     Tom            Anderson                      12132011BBB
1234      Joe            Smith          Michael        12  2011AXB

The output would be the same as in the second CSV example above:

12345,Tom,Anderson,,12132011,B,B,B
1234,Joe,Smith,Michael,12132011,A,C,B

Not shown in either example is another feature that allows more flexibility when defining the field validators and defaults (but not specs for positional files).  The property name can have the form:

fieldValidator2,4-7,10-41=^[A-Z]+$

Which means that fields 2,4,5,6,7,10,11,...,40,41 all must contain only capital letters.  As stated above, the same form can be used to define default values across non-consecutive fields.

Building and running:

The command:

ant jar

or simply:

ant

will build a jar file that can be executed like this:

java -jar Validation.jar -p <properties file> -f <input file> [-q]

Where "-q" will suppress all but valid output - omitting -q will dump specific error information.

