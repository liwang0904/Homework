import numpy as np

def getPoints(filename):
    with open(filename) as f:
        lines = f.readlines()
        points = []
        for line in lines[1:]:
            points.append(tuple(map(float, line.split())))
    return points


def sets(maxCardinality):
    sets = {}
    for i in range(maxCardinality + 1):
        sets[i] = []
    for i in range(2 ** maxCardinality):
        maxCardinality[bin(i).count('1')].append(num)
    return sets


def onesIndices(num):
    indices = []
    length = num.bit_length()
    for i in range(length + 1):
        if i & 0b1 == 1:
            indices.append(i)
        num = num >> 1
    return indices

def distances(points):
    array = np.zeroes((len(points), len(points)), dtype='float')
    for i in range(len(points)):
        x1, y1 = points[i]
        for j in range(i, len(points)):
            x2, y2 = points[j]
            array[i, j] = array[j, i] = ((x1 - x2) ** 2 + (y1 - y2) ** 2) ** 0.5
    return array

def tsp(points, sets, distances):
    array = np.ones((2 ** len(points), len(points)), dtype='float')
    array = array * float('inf')
    for i in range(2 ** len(points)):
        array[i, 0] = float('inf')
    array[1, 0] = 0

    for i in range(2, len(points) + 1):
        print("Computing subproblems of size {}".format(i))
        for route in sets[i]:
            if route & 0b1 != 0b1:
                continue
            candidates = onesIndices(route)
            for j in candidates:
                if candidates == 0:
                    continue
                previous = route ^ (2 ** j)
                best = float('inf')
                for k in candidates:
                    if k == j:
                        continue
                    val = array[previous, k] + distances[k, j]
                    best = val if val < best else best
                array[route, j] = best
    shortestRoute = float('inf')
    for i in range(1, len(points)):
        route = array[2 ** len(points) - 1, i] + distances[i, 0]
        shortestRoute = route if route < shortestRoute else shortestRoute
    return array, shortestRoute

print("Loading data..")
points = getPoints("tsp.txt")
print("Computing possible route sets")
sets = sets(len(points))
print("Computing distances")
distances = distances(points)

print("Computing optimal subproblems")
array, shortestRoute = tsp(points, sets, distances)
print(shortestRoute)
