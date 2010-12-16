function show_graph(adjacency_matrix, colors, set_size)

     % Generate the input to Graphviz
    writeDotFile(sprintf('t1.dot'), adjacency_matrix, colors, set_size);

    % Execute Graphviz
    system(sprintf('\"C:\\Program Files (x86)\\Graphviz2.22\\bin\\neato.exe\" -Tpng t1.dot -o t1.png'));
    figure;
    imshow('t1.png')
end