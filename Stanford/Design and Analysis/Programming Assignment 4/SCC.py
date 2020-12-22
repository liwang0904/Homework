class SCC(object):
    def __init__(self, input_file):
        self.scc_list = []
        with open(input_file) as file:
            self.finish_order = []
            self.graph = {}
            for line in file:
                (vertex1, vertex2) = tuple(number for number in line.split())
                self.add_edge(int(vertex1), int(vertex2))

    def add_edge(self, vertex1, vertex2):
        if vertex1 in self.graph:
            self.graph[vertex1].append(vertex2)
        else:
            self.graph[vertex1] = [vertex2]
        if vertex2 in self.graph:
            self.graph[vertex2].append(-vertex1)
        else:
            self.graph[vertex2] = [-vertex1]

    def compute_times(self):
        visited, finished = set(), set()
        for vertex in self.graph.keys():
            if vertex in visited:
                continue
            stack = [vertex]
            while stack:
                node = stack.pop()
                if node not in visited:
                    visited.add(node)
                    stack.append(node)
                    neighbors = (-edge for edge in self.graph[node] if edge < 0)
                    for neighbor in neighbors:
                        if neighbor not in visited:
                            stack.append(neighbor)
                            
                elif node not in finished:
                    self.finish_order.append(node)
                    finished.add(node)

    def compute_scc(self):
        visited = set()
        for i in reversed(self.finish_order):
            if i in visited:
                continue
            size = 0
            stack = [i]
            while stack:
                node = stack.pop()
                if node not in visited:
                    size += 1
                    visited.add(node)
                    stack.append(node)
                    neighbors = (edge for edge in self.graph[node] if edge > 0)
                    for neighbor in neighbors:
                        if neighbor not in visited:
                            stack.append(neighbor)
            self.scc_list.append(size)
        self.scc_list.sort(reverse = True)
        print(self.scc_list[:5])

scc = SCC('SCC.txt')
scc.compute_times()
scc.compute_scc()
