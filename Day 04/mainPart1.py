array = []

with open("input.txt", 'r') as file:
    array = file.readlines()

contained = 0

for line in array:
    parts = line[0:-1].split(',')
    elf1 = [int(x) for x in parts[0].split('-')]
    elf2 = [int(x) for x in parts[1].split('-')]
    
    if elf1[0] <= elf2[1] and elf2[0] <= elf1[1]:
        contained += 1

print(contained)