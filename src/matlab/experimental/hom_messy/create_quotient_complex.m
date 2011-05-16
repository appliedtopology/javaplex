function [stream,collapsed_adjacency_matrix] = create_quotient_complex(adjacency_matrix, filter_values)
    import edu.stanford.math.plex4.*;
    stream = api.Plex4.createExplicitSimplexStream();
    [m, n] = size(adjacency_matrix);
    maxima = [];
    for i = 1:m
        [r, c] = find(adjacency_matrix(i, :));
        candidates = filter_values(c);
        if (length(candidates) == 1 && filter_values(i) >= candidates(1))
            maxima = [maxima i]
        end
        if (filter_values(i) > max(candidates))
            %maxima = [maxima i]
        end
    end
    
    % add quotient vertex
    stream.addVertex(0);
    
    vertex_index = 1;
    vertex_mapping = zeros(m, 1);
    collapsed_filter_values = [];
    for i = 1:m
        c = find(maxima == i);
        if (isempty(c))
            vertex_mapping(i) = vertex_index;
            stream.addVertex(vertex_index);
            collapsed_filter_values(vertex_index + 1) = filter_values(vertex_index);
            vertex_index = vertex_index + 1;
        else
            
        end
    end
    
    num_vertices = vertex_index;
    collapsed_adjacency_matrix = zeros(num_vertices, num_vertices);
    
    for i = 1:m
        [r, c] = find(adjacency_matrix(i, :));
        vertex_i = vertex_mapping(i);
        for j = c
            if (j > i)
                vertex_j = vertex_mapping(j);
                if (vertex_i ~= 0 || vertex_j ~= 0)
                    stream.addElement([vertex_i, vertex_j]);
                    collapsed_adjacency_matrix(vertex_i, vertex_j) = 1;
                    collapsed_adjacency_matrix(vertex_j, vertex_i) = 1;
                end
            end
        end
    end
    
    
end