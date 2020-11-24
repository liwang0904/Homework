import sys
import random

def parseGraph(filename):
    vertices = []
    edges = set([])

    for line in open(filename):
        fields = [int(f) for f in line.split()]
        vertex = fields.pop(0)
        incident = [tuple(sorted([vertex, f])) for f in fields]
        vertices.append(vertex)
        edges.update(incident)
        
    return vertices, list(edges)

def contract(vertices, edges):
    while len(vertices) > 2:
        rand = random.choice(edges)
        a, b = rand
        vertices.remove(b)
        new_edges = []
        for edge in edges:
            if edge == rand:
                continue
            if b in edge:
                if edge[0] == b:
                    temp = edge[1]
                if edge[1] == b:
                    temp = edge[0]
                edge = tuple(sorted([a, temp]))
            new_edges.append(edge)
        edges = new_edges
    return vertices, edges

vertices, edges = parseGraph("kargerMinCut.txt")
minimum = sys.maxsize
for i in range(1000):
    vertex, edge = contract(vertices[:], edges[:])
    if len(edge) < minimum:
        minimum = len(edge)
print (minimum)
