function plot_graph(graph, options)

    if (~exist('options', 'var'))
        options = struct;
    end

    if (isfield(options, 'on_screen'))
        on_screen = options.on_screen;
    else
        on_screen = 1;
    end
    
    if (isfield(options, 'ray_trace'))
        ray_trace = options.ray_trace;
    else
        ray_trace = 1;
    end
    
    if (isfield(options, 'dimensions'))
        dimensions = options.dimensions;
    else
        dimensions = 3;
    end
    
    if (isfield(options, 'filename'))
        filename = options.filename;
    else
        filename = sprintf('graph_%d.pov', randi(100000, 1));
    end


    distances = edu.stanford.math.plex4.graph.utility.GraphUtility.computeShortestPaths(graph);
    distances = filter_distance_matrix(distances);
    
    stream = edu.stanford.math.plex4.graph.utility.GraphUtility.toSimplexStream(graph);

    [Y] = cmdscale(distances);
    points = [Y(:, 1:dimensions)];
    
    for i = 1:dimensions
        points(:, i) = points(:, i)  - mean(points(:, i));
    end
    
    for i = 1:dimensions
        points(:, i) = 2 * points(:, i) / max(abs(points(:, i)));
    end
    
    if (on_screen)
        render_onscreen(stream, points);
    end
    
    if (ray_trace)
        create_pov_file(stream, points, filename);
        render_pov_file(filename);
    end
end