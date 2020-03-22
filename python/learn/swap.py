first = raw_input("Enter first name: " )
last = raw_input("Enter last name: " )

print( "Full name: " + first + " " + last )
temp = first
first = last
last = temp
print( "Citation: " + first + ", " + last )
