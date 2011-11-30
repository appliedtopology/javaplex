function [bottleneck_distances] = comparative_graph_analysis(generators, query_graphs, K)
    
    matlabpool
    pctRunOnAll javaaddpath('../../for_distribution/lib/javaplex.jar');

    G = size(generators, 1);
    distance_matrices1 = cell(K * G, 1);

    parfor m = 0:(K*G - 1)
        g = floor(m / K) + 1;
        k = mod(m, K) + 1;
        graph = generators{g}.generate();
        distances = edu.stanford.math.plex4.graph.utility.GraphUtility.computeShortestPaths(graph);
        distances = filter_distance_matrix(distances);
        [Y] = cmdscale(distances);

        mds_distances = pairwise_row_distances(Y);
        mds_distances = mds_distances / max(max(mds_distances));

        distance_matrices1{m + 1} = mds_distances;
    end
    
    K2 = size(query_graphs, 1);
    distance_matrices2 = cell(K2, 1);
    
    parfor m = 1:K2
        distances = edu.stanford.math.plex4.graph.utility.GraphUtility.computeShortestPaths(query_graphs{m});
        [Y] = cmdscale(distances);

        mds_distances = pairwise_row_distances(Y);
        mds_distances = mds_distances / max(max(mds_distances));

        query_graph_distances = mds_distances;
        distance_matrices2{m} = query_graph_distances;
    end
    

    bottleneck_distances = comparative_barcode_analysis(distance_matrices1, distance_matrices2, 50);

    matlabpool close
end