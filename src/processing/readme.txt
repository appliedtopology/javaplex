This source code directory (src/processing) carries files relevant for using javaPlex
as a Processing library. The subdirectory javaplex has the raw templates for files
needed to build the Processing plugin library, which gets deposited in dist/Processing
as part of the build process. The subdirectory javaplexDemo is a Processing sketch
that relies on a working installation of the javaplex Processing plugin, and that 
demonstrates how to integrate the library in a sketch.

In particular, the javaplexDemo sketch allows the user to place points in a plane
and to step through the Vietoris-Rips complex on the placed points.

