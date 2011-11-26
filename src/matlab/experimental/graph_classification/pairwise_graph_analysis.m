function [bottleneck_distances] = pairwise_graph_analysis(generators, K)
    
G = size(generators, 1);
distance_matrices = cell(K * G, 1);



for g = 1:G
    for k = 1:K
        graph = generators{g}.generate();
        distances = edu.stanford.math.plex4.graph.utility.GraphUtility.computeShortestPaths(graph);
        [Y] = cmdscale(distances);
        %adjancency = edu.stanford.math.plex4.graph.utility.GraphUtility.getLaplacianMatrix(graph);
        %[Y, D] = eig(double(adjancency));
        
        mds_distances = pairwise_row_distances(Y);
        
        mds_distances = mds_distances / max(max(mds_distances));
        
        distance_matrices{k + (g-1) * K} = mds_distances;
        
    end
end

bottleneck_distances = pairwise_barcode_analysis(distance_matrices);


end