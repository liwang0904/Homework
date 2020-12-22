import random

def find(parents, i):
    parent = i
    while parent in parents:
        parent = parents[parent]
    while i in parents:
        temp = parents[i]
        parents[i] = parent
        i = temp
    return i

def union(parent, i, j):
    parent_i, parent_j = find(parent, i), find(parent, j)
    if parent_i != parent_j:
        parent[parent_i] = parent_j

def connected(parent, i, j):
    return find(parent, i) == find(parent, j)

def karger(v, edges):
    edges = list(edges)
    random.shuffle(edges)
    parent = {}
    for i, j in edges:
        if v <= 2:
            break
        if connected(parent, i, j):
            continue
        union(parent, i, j)
        v -= 1
    return sum(find(parent, i) != find(parent, j) for (i, j) in edges)

lines = list(open('kargerMinCut.txt'))
v = len(lines)
edges = set()
for line in lines:
    fields = iter(map(int, line.split()))
    field = next(fields)
    edges.update((min(field, vertex), max(field, vertex)) for vertex in fields)
    
print(min(karger(v, edges) for i in range(1000)))
