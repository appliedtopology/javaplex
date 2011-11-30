function bottleneck_distances = comparative_barcode_analysis(distance_matrices1, distance_matrices2, max_metric_size)

    K1 = size(distance_matrices1, 1);
    K2 = size(distance_matrices2, 1);
    
    interval_collections1 = cell(K1, 1);
    interval_collections2 = cell(K2, 1);
    
    for k = 1:(K1 + K2)
        if (k <= K1)
            mds_distances = distance_matrices1{k};
        else
            mds_distances = distance_matrices2{k - K1};
        end
            
        m_space = edu.stanford.math.plex4.metric.impl.ExplicitMetricSpace(mds_distances);
        
        if (max_metric_size < m_space.size())
            m_space = edu.stanford.math.plex4.metric.landmark.MaxMinLandmarkSelector(m_space, max_metric_size);
        end
        
        max_dimension = 0;
        max_filtration_value = 4;

        stream = edu.stanford.math.plex4.api.Plex4.createVietorisRipsStream(m_space, max_dimension+1, max_filtration_value, 1000);
        stream.finalizeStream();

        persistence = edu.stanford.math.plex4.api.Plex4.getDefaultSimplicialAlgorithm(max_dimension+1);
        filtration_value_intervals = persistence.computeIntervals(stream);
        dim_0_intervals = filtration_value_intervals.getIntervalsAtDimension(0);
        dim_0_intervals = edu.stanford.math.plex4.bottleneck.BottleneckDistance.truncate(dim_0_intervals, 0, max_filtration_value);
        
        if (k <= K1)
            interval_collections1{k} = dim_0_intervals;
        else
            interval_collections2{k - K1} = dim_0_intervals;
        end
        
    end
    
    bottleneck_distances = zeros(K1 * K2, 1);
    
    parfor m = 0:(K1 * K2 - 1)
        k1 = floor(m / K2) + 1;
        k2 = mod(m, K2) + 1;
        
        bottleneck_distances(m + 1) = edu.stanford.math.plex4.bottleneck.BottleneckDistance.computeBottleneckDistance(interval_collections1{k1}, interval_collections2{k2});
        display(sprintf('pairwise_barcode_analysis: d(%d, %d) = %f', k1, k2, bottleneck_distances(m + 1)));
    end
    
    bottleneck_distances = reshape(bottleneck_distances, K2, K1)';
end