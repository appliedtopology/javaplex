function [edge_list] = get_edges(stream)
    num_edges = 0;
    iterator = stream.iterator();
    while (iterator.hasNext())
        simplex = iterator.next();
        if (simplex.getDimension() == 1)
            num_edges = num_edges + 1;
        end
    end
    
    edge_list = zeros(num_edges, 2);
    count = 1;
    iterator = stream.iterator();
    while (iterator.hasNext())
        simplex = iterator.next();
        if (simplex.getDimension() == 1)
            edge_list(count, :) = simplex.getVertices();
            count = count + 1;
        end
    end
end