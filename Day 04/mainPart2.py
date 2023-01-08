array = []

with open("input04.txt", 'r') as file:
    array = file.readlines()

contained = 0

elves = []

for line in array:
    parts = line[0:-1].split(',')
    elf1 = [int(x) for x in parts[0].split('-')]
    elf2 = [int(x) for x in parts[1].split('-')]
    
    elves.append(elf1)
    elves.append(elf2)

contained = 0
for i, elf1 in enumerate(elves):
    for j, elf2 in enumerate(elves[i+1::]):
        
        if (elf1[0] >= elf2[0] and elf1[1] <= elf2[1]) or (elf2[0] >= elf1[0] and elf2[1] <= elf1[1]):
            print(i,j)
            contained += 1

print(contained)