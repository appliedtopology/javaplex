function [handle] = plot_barcodes(intervals, options)
% INPUT:
%   intervals - the barcode collection to draw
% OPTIONS:
%   min_filtration_value - the minimum filtration value (default value is 0)
%   max_filtration_value - the maximum filtration value (default value inferred from intervals)
%   min_dimension - the minimum dimension to draw intervals for (inclusive)
%   max_dimension - the maximum dimension to draw intervals for (inclusive)
%   filename - the filename to save to without the extension (equal to caption if not specified)
%   caption - the caption for the image (equal to filename if not specified)
%   file_format - the file type - png, eps, jpg, etc.
%   side_by_side - whether to stack the plots side-by-side or not (default is false)
%   line_width - the thickness of the barcodes (default value 0.5)
% OUTPUT:
%   This function produces a plot of the barcodes for dimensions 0, ...,
%   max_dimension, and displays it on screen. If a filename is specified,
%   it also saves it to a file. It also returns a handle to the figure drawn.
%
% henrya@math.stanford.edu and atausz@stanford.edu
    
    if (~exist('options', 'var'))
        options = struct;
    end

    if (isfield(options, 'min_filtration_value'))
        min_filtration_value = options.min_filtration_value;
    end
    
    if (isfield(options, 'max_filtration_value'))
        max_filtration_value = options.max_filtration_value;
    end
    
    if (isfield(options, 'min_dimension'))
        min_dimension = options.min_dimension;
    else
        min_dimension = 0;
    end
    
    if (isfield(options, 'max_dimension'))
        max_dimension = options.max_dimension;
    else
        max_dimension = length(intervals.getBettiSequence()) - 1;
    end
    
    if (isfield(options, 'filename'))
        filename = options.filename;
    else
        if (isfield(options, 'caption'))
            filename = options.caption;
        end
    end
    
    if (isfield(options, 'caption'))
        caption = options.caption;
    else
        if (isfield(options, 'filename'))
            caption = options.filename;
        end
    end
    
    if (isfield(options, 'file_format'))
        file_format = options.file_format;
    else
        file_format = 'png';
    end
    
    if (isfield(options, 'side_by_side'))
        side_by_side = options.side_by_side;
    else
        side_by_side = false;
    end
    
    if (isfield(options, 'line_width'))
        line_width = options.line_width;
    else
        line_width = 0.5;
    end

    import edu.stanford.math.plex4.*;
    
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
    
    handle = figure;
    hold on;
    
    if (exist('max_filtration_value', 'var'))
        x_max = max_filtration_value;
    elseif (right_infinite_interval_found)
        x_max = max_finite_endpoint + 0.2 * (max_finite_endpoint - min_finite_endpoint);
    else
        x_max = max_finite_endpoint;
    end
    
    if (exist('min_filtration_value', 'var'))
        x_min = min_filtration_value;
    elseif (left_infinite_interval_found)
        x_min = min_finite_endpoint - 0.2 * (max_finite_endpoint - min_finite_endpoint);
    else
        x_min = min_finite_endpoint;
    end
    
    point_width = 0.006 * (x_max - x_min);
    
    for dimension = min_dimension:max_dimension
        endpoints = homology.barcodes.BarcodeUtility.getEndpoints(intervals, dimension, false);
        num_intervals = size(endpoints, 1);
        
        if (side_by_side)
            subhandle = subplot(1, max_dimension + 1 - min_dimension, dimension + 1 - min_dimension);
        else
            subhandle = subplot(max_dimension + 1 - min_dimension, 1, dimension + 1 - min_dimension);
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
                line([start, x_max], [y, y], 'LineWidth', line_width);
                line([x_max, x_max], [y, y], 'Marker', '>', 'LineWidth', line_width);
            end
            
            if (finish < threshold && start <= -threshold)
                line([x_min, finish], [y, y], 'LineWidth', line_width);
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
        
        if (exist('caption', 'var'))
            title(sprintf('%s (dimension %d)', caption, dimension));
        else
            ylabel(sprintf('Dim %d', dimension));
        end
    end
    
    hold off;
    
    if (exist('filename', 'var'))
        saveas(handle, filename, file_format);
    end
end
