function persistence_diagram(intervals, min_dimension, max_dimension, filename)
% INPUT:
%   intervals - the barcode collection to draw
%   min_dimension - the minimum dimension to draw persistence diagram for (inclusive)
%   max_dimension - the maximum dimension to draw persistence diagram for (inclusive)
%   filename - the png filename to save to (optional)
% OUTPUT:
%   This function produces a persistence diagram for dimensions 0, ...,
%   max_dimension, and displays it on screen. If a filename is specified,
%   it also saves it to a file.
%
% henry.adams@colostate.edu and atausz@stanford.edu

    import edu.stanford.math.plex4.*;

    threshold = 1e20;
    
     max_finite_endpoint = -threshold;
     min_finite_endpoint = threshold;
     
     max_finite_startpoint = -threshold;
     min_finite_startpoint = threshold;
        
     left_infinite_interval_found = 0;
     right_infinite_interval_found = 0;
        
    for dimension = min_dimension:max_dimension
    
        endpoints = homology.barcodes.BarcodeUtility.getEndpoints(intervals, dimension, false);

        num_intervals = size(endpoints, 1);
       
        
        for i = 1:num_intervals
            start = endpoints(i, 1);
            finish = endpoints(i, 2);
            
            if (finish >= threshold)
                right_infinite_interval_found = 1;
            end
            
            if (start <= -threshold)
                left_infinite_interval_found = 1;
            end
            
            if (finish < threshold && finish > max_finite_endpoint)
                max_finite_endpoint = finish;
            end
            
            if (start < threshold && start > max_finite_startpoint)
                max_finite_startpoint = start;
            end
            
            if (start > -threshold && start < min_finite_startpoint)
                min_finite_startpoint = start;
            end
            
            if (finish > -threshold && finish < min_finite_endpoint)
                min_finite_endpoint = finish;
            end
        end
        
    end
    
    h = figure;
    hold on;
    
    if (right_infinite_interval_found)
        y_max = max_finite_endpoint + 0.2 * (max_finite_endpoint - min_finite_endpoint);
    else
        y_max = max_finite_endpoint;
    end
    
    y_min = min_finite_endpoint;
    
    if (left_infinite_interval_found)
        x_min = min_finite_startpoint - 0.2 * (max_finite_startpoint - min_finite_startpoint);
    else
        x_min = min_finite_startpoint;
    end
    
    x_max = max_finite_startpoint;
    
    for dimension = min_dimension:max_dimension
        endpoints = homology.barcodes.BarcodeUtility.getEndpoints(intervals, dimension, false);
        num_intervals = size(endpoints, 1);
        
        subhandle = subplot(1, max_dimension + 1, dimension + 1);
        
        for i = 1:num_intervals
            start = endpoints(i, 1);
            finish = endpoints(i, 2);
            y = num_intervals - i + 1;
            
            if (finish >= threshold && start <= -threshold)
                line([x_min, x_min], [y_max, y_max], 'Marker', '<');
            end
            
            if (finish >= threshold && start > -threshold)
                line([start, start], [y_max, y_max], 'Marker', '^');
            end
            
            if (finish < threshold && start <= -threshold)
               line([x_min, x_min], [finish, finish], 'Marker', '<');
            end
            
            if (finish < threshold && start > -threshold)
                line([start, start], [finish, finish], 'Marker', 'o');
            end
        end
        
        
        axis([min(x_min, y_min), x_max, min(x_min, y_min), y_max]);
        title(sprintf('Dim %d', dimension));
        xlabel('Start');
        ylabel('End');
        %set(subhandle,'YTick',[]);
        set(subhandle,'XGrid','on','YGrid','on');
    end
    
    hold off;
    
    if (exist('filename'))
        saveas(h, filename, 'png');
    end
end