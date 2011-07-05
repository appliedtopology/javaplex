function result = render_pov_file(pov_file, width, height)
    if (~exist('width', 'var'))
        width = 1200;
    end    

    if (~exist('height', 'var'))
        height = 900;
    end
    
    quality = 3;
    
    command = sprintf('povray +I%s +W%d, +H%d +Q%d +A', pov_file, width, height, quality);
    
    result = system(command);
end
