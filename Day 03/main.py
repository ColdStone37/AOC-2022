array = []

with open("input03.txt", 'r') as file:
    array = file.readlines()

prio = 0

for line in array:
	print("l")
	l = line[0:-1]
	l_p1 = l[0:(len(l)//2)]
	l_p2 = l[len(l)//2:len(l)]
	
	for c in l_p1:
		if c in l_p2:
			print(l_p1,l_p2,c)
			if ord(c) >= 97:
				prio += ord(c)-96
			else:
				prio += ord(c)-38
			break

print(prio)