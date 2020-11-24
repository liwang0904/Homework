def countComparisons(alist, pivotID):
    global count
    
    if len(alist) == 1 or len(alist) == 0:
        return alist
    
    elif pivotID == 'last': # counts number of comparisons with last element as pivot
        alist[0], alist[-1] = alist[-1], alist[0]
        
    elif pivotID == 'middle':  # counts number of comparisons with median-of-three element as pivot
        k = median_index(alist, 0, middle_index(alist), -1)
        if k != 0:
            alist[0], alist[k] = alist[k], alist[0]

    count += len(alist) - 1
    i = 0
    for j in range(len(alist) - 1):
        if alist[j + 1] < alist[0]:
            alist[j + 1], alist[i + 1] = alist[i + 1], alist[j + 1]
            i += 1

    alist[0], alist[i] = alist[i], alist[0]
    first_part = countComparisons(alist[:i], pivotID);
    second_part = countComparisons(alist[i + 1:], pivotID);
    first_part.append(alist[i])
    return first_part + second_part

def middle_index(alist):
    # returns the index of the middle element
    if len(alist) % 2 == 0:
        middle_index = len(alist)//2 - 1
    else:
        middle_index = len(alist)//2
    return middle_index

def median_index(alist, i, j, k):
    # returns the median index of three when passed an array and indices of any three elements of that array
    if (int(alist[i]) - int(alist[j])) * (int(alist[i]) - int(alist[k])) < 0:
        return i
    elif (int(alist[j]) - int(alist[i])) * (int(alist[j]) - int(alist[k])) < 0:
        return j
    else:
        return k

#################################################################

# read the contents into a list
inFile = open("QuickSort.txt", 'r')
with inFile as f:
    alist = [int(integers.strip()) for integers in f.readlines()]

count = 0 # initialize count
countComparisons(alist, 'first');
print (count)

#################################################################

# read the contents into a list
inFile = open("QuickSort.txt", 'r')
with inFile as f:
    alist = [int(integers.strip()) for integers in f.readlines()]

count = 0 # initialize count
countComparisons(alist, 'last');
print (count)

#################################################################

# read the contents into a list
inFile = open("QuickSort.txt", 'r')
with inFile as f:
    alist = [int(integers.strip()) for integers in f.readlines()]

count = 0 # initialize count
countComparisons(alist, 'middle');
print (count)

#################################################################
