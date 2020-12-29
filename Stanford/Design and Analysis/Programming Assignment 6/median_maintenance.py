import heapq

heap_low = []
heap_high = []

def median_maintenance(num):
    global heap_low, heap_high

    if len(heap_low) == 0:
        heapq.heappush(heap_low, -num)
    else:
        if num > -heap_low[0]:
            heapq.heappush(heap_high, num)
            if len(heap_high) > len(heap_low):
                heapq.heappush(heap_low, -heapq.heappop(heap_high))
        else:
            heapq.heappush(heap_low, -num)
            if len(heap_low) - len(heap_high) > 1:
                heapq.heappush(heap_high, -heapq.heappop(heap_low))
                
    return -heap_low[0]

medians = []
with open('Median.txt') as f:
    for line in f:
        num = int(line.strip())
        medians.append(median_maintenance(num))
        
print(sum(medians) % 10000)
