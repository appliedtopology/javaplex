function adjacency_matrix = to_graph(stream)
    
    size = 0;
    
    iterator = stream.iterator();
    while (iterator.hasNext())
        simplex = iterator.next();
        if (stream.getDimension(simplex) == 0)
            size = size + 1;
        end
    end
    
    adjacency_matrix = zeros(size, size);
    
    iterator = stream.iterator();
    while (iterator.hasNext())
        simplex = iterator.next();
        if (stream.getDimension(simplex) == 1)
            vertices = simplex.getVertices();
            i = vertices(1) + 1;
            j = vertices(2) + 1;
            adjacency_matrix(i, j) = 1;
            adjacency_matrix(j, i) = 1;
        end
    end
end