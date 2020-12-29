import bisect

def two_sum(data):
    width = 10000
    result = set()
    for x in data:
        lower = bisect.bisect_left(data, -width - x)
        upper = bisect.bisect_right(data, width - x)
        for y in data[lower:upper]:
            if y != x:
                result.add(x + y)
    return result

data = []
with open('algo1-programming_prob-2sum.txt') as f:
    for line in f:
        num = int(line.strip())
        data.append(num)
data.sort()
print(len(two_sum(data)))
