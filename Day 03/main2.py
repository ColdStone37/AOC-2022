array = []

with open("input03.txt", 'r') as file:
    array = file.readlines()

prio = 0

for i in range(len(array)//3):
	l1 = array[i*3][0:-1]
	l2 = array[i*3+1][0:-1]
	l3 = array[i*3+2][0:-1]
	
	for c in l1:
		if c in l2 and c in l3:
			if ord(c) >= 97:
				prio += ord(c)-96
			else:
				prio += ord(c)-38
			break

print(prio)