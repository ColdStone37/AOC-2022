import csv
with open('input01.txt', 'r') as fd:
    reader = csv.reader(fd)
    Sum = 0
    maxSum = 0
    sums=[];
    for row in reader:

        if len(row) == 0:
            print(Sum,maxSum)
            print()
            sums.append(Sum)
            if(Sum>maxSum):
                maxSum=Sum
            Sum=0
        else:
            print(row,int(row[0]))
            Sum=Sum+int(row[0])
    sums.sort()
    print(sums[-1])
    print(sums[-1]+sums[-2]+sums[-3])