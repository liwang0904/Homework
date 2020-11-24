def inversionsCount(alist):
    global count
    left = alist[:len(alist) // 2]
    right = alist[len(alist) // 2:]
    
    if len(alist) > 1:
        inversionsCount(left)
        inversionsCount(right)

        count1, count2 = 0, 0
        for i in range(len(left) + len(right) + 1):
            
            if left[count1] <= right[count2]:
                alist[i] = left[count1]
                count1 += 1
                
                if count1 == len(left) and count2 != len(right):
                    
                    while count2 != len(right):
                        i += 1
                        alist[i] = right[count2]
                        count2 += 1
                    break
                
            elif left[count1] > right[count2]:
                alist[i] = right[count2]
                count += (len(left) - count1)
                count2 += 1
                
                if count2 == len(right) and count1 != len(left):
                    
                    while count1 != len(left):
                        i += 1
                        alist[i] = left[count1]
                        count1 += 1
                    break
    return alist

inFile = open("IntegerArray.txt", 'r')

with inFile as f:
    numList = [int(integers.strip()) for integers in f.readlines()]
    
count = 0
inversionsCount(numList)
print (count)
