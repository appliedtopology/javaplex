function [filename] = create_landmark_pov_file(landmark_selector, filename)

    javaaddpath('../lib/plex-viewer.jar');
    import edu.stanford.math.plex_viewer.*;

    pov_writer = pov.LandmarkSelectorPovWriter();
    pov_writer.writeToFile(landmark_selector, filename);
end

