function [stream, points] = random_geometric_graph(n, d, r)
    points = rand(n, d);
    stream = edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream();
    
    for i = 1:n
        stream.addVertex(i - 1);
    end
    
    for i = 1:n
        for j = (i + 1):n
            if (norm(points(i, :) - points(j, :)) <= r)
                stream.addElement([i - 1, j - 1]);
            end
        end
    end
end