function [bottleneck_distances] = pairwise_graph_analysis(generators, K)
    
    matlabpool
    pctRunOnAll javaaddpath('../../for_distribution/lib/javaplex.jar');
    
    G = size(generators, 1);
    distance_matrices = cell(K * G, 1);

    parfor m = 0:(K*G - 1)
        g = floor(m / K) + 1;
        k = mod(m, K) + 1;
        graph = generators{g}.generate();
        distances = edu.stanford.math.plex4.graph.utility.GraphUtility.computeShortestPaths(graph);
        [Y] = cmdscale(distances);

        mds_distances = pairwise_row_distances(Y);

        mds_distances = mds_distances / max(max(mds_distances));

        distance_matrices{m + 1} = mds_distances;
    end

    bottleneck_distances = pairwise_barcode_analysis(distance_matrices, 50);

    matlabpool close
end