function [hm_handle, mds_handle] = visualize_comparison_distances(distances, generators, query_labels, label)

G = size(generators, 1);

K = size(distances, 1) / G;

groups = cell(K * G, 1);

similarity_matrix = -distances + mean(mean(distances));
labels = cell(K * G, 1);

for g = 1:G
    for k = 1:K
        groups{k + (g-1) * K} = char(generators{g}.toString());
        
        if (k == 1)
            labels{k + (g-1) * K} = char(generators{g}.toString());
        else
            labels{k + (g-1) * K} = '';
        end
        
    end
end

hm_handle = figure;
heatmap(distances, query_labels, labels);
colorbar
title('Bottleneck Distances between Graph Samples and Query Graph');
print(hm_handle, strcat(label, '_hm', '.eps'));

end