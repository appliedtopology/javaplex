function bottleneck_distances = pairwise_barcode_analysis(distance_matrices)
    
    matlabpool

    K = size(distance_matrices, 1);
    pctRunOnAll javaaddpath('../../for_distribution/lib/javaplex.jar');
    
    intervals = cell(K, 1);
    
    parfor k = 1:K
        mds_distances = distance_matrices{k};
        m_space = edu.stanford.math.plex4.metric.impl.ExplicitMetricSpace(mds_distances);
        max_dimension = 0;
        
        max_filtration_value = 4;

        stream = edu.stanford.math.plex4.api.Plex4.createVietorisRipsStream(m_space, max_dimension+1, max_filtration_value, 1000);
        stream.finalizeStream();

        persistence = edu.stanford.math.plex4.api.Plex4.getDefaultSimplicialAlgorithm(max_dimension+1);
        filtration_value_intervals = persistence.computeIntervals(stream);
        intervals{k} = filtration_value_intervals.getIntervalsAtDimension(0);
        intervals{k} = edu.stanford.math.plex4.bottleneck.BottleneckDistance.truncate(intervals{k}, 0, max_filtration_value);
        %intervals{k} = edu.stanford.math.plex4.bottleneck.BottleneckDistance.filterLargest(intervals{k}, N);
    end
    
    bottleneck_distances = zeros(K, K);
    
    for i = 1:K
        collection_1 = intervals{i};
        parfor j = (i+1):K
            collection_2 = intervals{j};
            bottleneck_distance = edu.stanford.math.plex4.bottleneck.BottleneckDistance.computeBottleneckDistance(collection_1, collection_2);
            bottleneck_distances(i, j) = bottleneck_distance;
            display(sprintf('pairwise_barcode_analysis: d(%d, %d) = %f', i, j, bottleneck_distance));
        end
    end
    
    
    for i = 1:K
        for j = (i+1):K
            bottleneck_distances(j, i) = bottleneck_distances(i, j);
        end
    end
    
    matlabpool close
end