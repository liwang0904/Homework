import random

def find(parent, i):
    while i in parent:
        temp = parent[i]
        parent[i] = find(temp)
    return parent[i]

def union(parent, i, j):
    pi, pj = find(parent, i), find(parent, j)
    if pi != pj:
        parent[pi] = pj

def connected(parent, i, j):
    return find(parent, i) == find(parent, j)

def karger(n, edges):
    edges = list(edges)
    random.shuffle(edges)
    parent = {}
    for i, j in edges:
        if n <= 2:
            break
        if connected(parent, i, j):
            continue
        union(parent, i, j)
        n -= 1
    return sum(find(parent, i) != find(parent, j) for (i, j) in edges)

lines = list(open('kargerMinCut.txt'))
n = len(lines)
edges = set()
for line in lines:
    fields = iter(map(int, line.split()))
    field = next(fields)
    edges.update((min(field, vertex), max(field, vertex)) for vertex in fields)
    
print(min(karger(n, edges) for i in range(1000)))
