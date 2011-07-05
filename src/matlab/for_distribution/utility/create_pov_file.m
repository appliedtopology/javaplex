function [filename] = draw_complex(stream, points, filename)

    javaaddpath('../lib/plex-viewer.jar');
    import edu.stanford.math.plex_viewer.*;

    pov_writer = pov.SimplexStreamPovWriter();
    pov_writer.writeToFile(stream, points, filename);
end

