% This script prepares the javaplex library for use

clc; clear all; close all;
clear import;

javaaddpath('./../javaplex.jar');
javaaddpath('./../plex-viewer.jar');
java.lang.System.loadLibrary('jogl');

import edu.stanford.math.plex4.*;
import edu.stanford.math.plex_viewer.*;