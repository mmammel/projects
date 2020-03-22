import getpass

try:
  passw = getpass.getpass()
except:
  print("Blew it!")


print(passw)
