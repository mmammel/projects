height = float(0.0)
weight = float(0.0)


def get_bmi(height,weight):
  bmi = weight / (height / 100.0 )**2
  return bmi

while True:
  height = float(raw_input("Enter your height in centimeters: "))
  weight = float(raw_input("Enter your weight in kilograms: "))
  bmi = get_bmi(height,weight)
  print("Your BMI is: " + str(bmi) )
  if bmi < 18.5:
    print("Nice work, you're skinny")
  elif bmi >= 18.5 and bmi <= 25:
    print("Middle of the road")
  else:
    print("You could stand to lose a few pounds")

  proceed = raw_input("Press enter to re-calculate, or type \"quit\" to exit: ")
  if proceed == 'quit':
    break

print("Goodbye!")
