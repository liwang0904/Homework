def get_graph(filename):
    graph = []
    lines = open(filename).read().splitlines()
    for line in lines:
        graph.append([])
        nums = line.split()
        vertex = int(nums[0]) - 1
        for tup in nums[1:]:
            t, w = tup.split(',')
            graph[vertex].append((int(t) - 1, int(w)))
    return graph

def extract_min(priorityQueue, distances):
    i = 0
    j = 1
    minimum = distances[priorityQueue[0]]
    while j < len(priorityQueue):
        if distances[priorityQueue[j]] < minimum:
            i = j
            minimum = distances[priorityQueue[j]]
        j += 1
    result = priorityQueue[i]
    priorityQueue[i] = priorityQueue[-1]
    priorityQueue.pop()
    return result

def dijkstra(graph, start):
    distances = [float("inf")] * len(graph)
    distances[start] = 0
    priorityQueue = [i for i in range(len(graph))]
    visited = [False] * len(graph)
    while len(priorityQueue) > 0:
        minimum = extract_min(priorityQueue, distances)
        visited[minimum] = True
        for vertex, adjacent in graph[minimum]:
            if not visited[vertex]:
                distances[vertex] = min(distances[vertex], distances[minimum] + adjacent)
    return distances

endings = [7, 37, 59, 82, 99, 115, 133, 165, 188, 197]
graph = get_graph('dijkstraData.txt')
distances = dijkstra(graph, 0)
result = []
for ending in endings:
    result.append(distances[ending - 1])
print(result)
