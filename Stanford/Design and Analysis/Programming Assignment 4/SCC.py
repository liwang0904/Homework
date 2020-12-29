import sys, threading

n = 875714

def get_graph(filename):
    f = open(filename)
    graph, graphReversed = {}, {}
    for i in range(1, n + 1):
        graph[i], graphReversed[i] = [], []
    for line in f:
        nums = line.split()
        tail = int(nums[0])
        head = int(nums[1])
        graph[tail].append(head)
        graphReversed[head].append(tail)
    f.close()
    return graph, graphReversed

def depth_first_search(graph, node):
    global processed
    explored[node] = True
    leader[node] = source
    for head in graph[node]:
        if not explored[head]:
            depth_first_search(graph, head)
    processed += 1
    finish[node] = processed

def depth_first_search_loop(graph):
    global processed, source, explored
    processed = 0
    source = 0
    explored = {}
    for i in range(1, n + 1):
        explored[i] = False
    for node in range(n, 0, -1):
        if not explored[node]:
            source = node
            depth_first_search(graph, node)

def kosaraju(graph, graphReversed):
    global leader, finish
    leader, finish = {}, {}
    depth_first_search_loop(graphReversed)
    graphReordered = {}
    values = list(graph.values())
    for i in range(1, n + 1):
        temp = values[i - 1]
        graphReordered[finish[i]] = [finish[x] for x in temp]
    print('start 2')
    depth_first_search_loop(graphReordered)
    return leader

def mostCommon(list, x):
    from collections import Counter
    result = []
    counter = Counter(list)
    for num, count in counter.most_common(x):
        result.append(count)
    return result

def main():
    print('start')
    graph, graphReversed = get_graph('SCC.txt')
    print('start 1')
    leader = kosaraju(graph, graphReversed)
    print(mostCommon(leader.values(), 5))

if __name__ == '__main__':
    threading.stack_size(67108864) 
    sys.setrecursionlimit(2 ** 20)
    thread = threading.Thread(target = main)
    thread.start() 
