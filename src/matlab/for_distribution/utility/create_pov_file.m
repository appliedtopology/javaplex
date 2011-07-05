function [filename] = create_pov_file(stream, points, filename)

    javaaddpath('../lib/plex-viewer.jar');
    import edu.stanford.math.plex_viewer.*;

    pov_writer = pov.SimplexStreamPovWriter();
    pov_writer.writeToFile(stream, points, filename);
end

