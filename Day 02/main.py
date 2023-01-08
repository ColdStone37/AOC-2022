array = []

with open("input.txt", 'r') as file:
    array = file.readlines()


score = 0
for line in array:
    #number
    enemyMove = ord(line[0])-65
    shouldWin = ord(line[2])-88

    myMove = 0

    if shouldWin == 0:
        myMove = (enemyMove+2) % 3
    if shouldWin == 1:
        myMove = enemyMove
    if shouldWin == 2:
        myMove = (enemyMove+1) % 3

    enemyMove = enemyMove + 1
    myMove+=1

    score += myMove

    if enemyMove == myMove:
        #draw
        score += 3
    else:
        if myMove == 2 and enemyMove == 1:
            score += 6
        if myMove == 3 and enemyMove == 2:
            score += 6
        if myMove == 1 and enemyMove == 3:
            score += 6

print(score)
