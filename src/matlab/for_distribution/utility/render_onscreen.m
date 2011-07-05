function [filename] = render_onscreen(stream, points)

    javaaddpath('../lib/plex-viewer.jar');
    import edu.stanford.math.plex_viewer.*;

    Api.drawSimplexStream(stream, points);
end

