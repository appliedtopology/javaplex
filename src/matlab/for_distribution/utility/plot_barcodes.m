function plot_barcodes(intervals, min_dimension, max_dimension, filename, side_by_side, line_width)
% INPUT:
%   intervals - the barcode collection to draw
%   min_dimension - the minimum dimension to draw intervals for (inclusive)
%   max_dimension - the maximum dimension to draw intervals for (inclusive)
%   filename - the png filename to save to (optional)
%   side_by_side - whether to stack the plots side-by-side or not (optional)
% OUTPUT:
%   This function produces a plot of the barcodes for dimensions 0, ...,
%   max_dimension, and displays it on screen. If a filename is specified,
%   it also saves it to a file.
%
% henrya@math.stanford.edu and atausz@stanford.edu

    import edu.stanford.math.plex4.*;

    if (~exist('side_by_side'))
        side_by_side = 0;
    end
    
    if (~exist('line_width'))
        line_width = 1;
    end
    
    threshold = 1e20;
    epsilon = 1e-6;
    
    max_finite_endpoint = -threshold;
    min_finite_endpoint = threshold;
        
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
            
            if (start < threshold && start > max_finite_endpoint)
                max_finite_endpoint = start;
            end
            
            if (start > -threshold && start < min_finite_endpoint)
                min_finite_endpoint = start;
            end
            
            if (finish > -threshold && finish < min_finite_endpoint)
                min_finite_endpoint = finish;
            end
        end
        
    end
    
    h = figure;
    hold on;

    if (right_infinite_interval_found)
        x_max = max_finite_endpoint + 0.2 * (max_finite_endpoint - min_finite_endpoint);
    else
        x_max = max_finite_endpoint;
    end
    
    if (left_infinite_interval_found)
        x_min = min_finite_endpoint - 0.2 * (max_finite_endpoint - min_finite_endpoint);
    else
        x_min = min_finite_endpoint;
    end
    
    point_width = 0.002 * (x_max - x_min);
    
    for dimension = min_dimension:max_dimension
        endpoints = homology.barcodes.BarcodeUtility.getEndpoints(intervals, dimension, false);
        num_intervals = size(endpoints, 1);
        
        if (side_by_side)
            subhandle = subplot(1, max_dimension + 1, dimension + 1);
        else
            subhandle = subplot(max_dimension + 1, 1, dimension + 1);
        end
        
        for i = 1:num_intervals
            start = endpoints(i, 1);
            finish = endpoints(i, 2);
            y = num_intervals - i + 1;
            
            if (finish >= threshold && start <= -threshold)
                line([x_min, x_max], [y, y], 'LineWidth', line_width);
                line([x_min, x_min], [y, y], 'Marker', '<', 'LineWidth', line_width);
                line([x_max, x_max], [y, y], 'Marker', '>', 'LineWidth', line_width);
            end
            
            if (finish >= threshold && start > -threshold)
                line([start, x_max], [y, y]);
                line([x_max, x_max], [y, y], 'Marker', '>', 'LineWidth', line_width);
            end
            
            if (finish < threshold && start <= -threshold)
                line([x_min, finish], [y, y]);
                line([x_min, x_min], [y, y], 'Marker', '<', 'LineWidth', line_width);
            end
            
            if (finish < threshold && start > -threshold)
                if (abs(finish - start) < epsilon)
                    line([start - 0.5 * point_width, finish + 0.5 * point_width], [y, y], 'LineWidth', line_width);
                else
                    line([start, finish], [y, y], 'LineWidth', line_width);
                end
            end
        end
        
        axis([x_min, x_max, 0, num_intervals + 1]);
        set(subhandle,'YTick',[]);
        set(subhandle,'XGrid','on','YGrid','on');
        
        if (exist('filename'))
            title(sprintf('%s (dimension %d)', filename, dimension));
        else
            ylabel(sprintf('Dim %d', dimension));
        end
    end
    
    hold off;
    
    if (exist('filename'))
        saveas(h, filename, 'png');
    end
end