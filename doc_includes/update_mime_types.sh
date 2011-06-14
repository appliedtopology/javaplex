#!/bin/sh

svn propset svn:mime-type text/html -R *.html
svn propset svn:mime-type text/html -R */*.html
svn propset svn:mime-type text/html -R */*/*.html
svn propset svn:mime-type text/html -R */*/*/*.html
svn propset svn:mime-type text/html -R */*/*/*/*.html
svn propset svn:mime-type text/html -R */*/*/*/*/*.html
svn propset svn:mime-type text/html -R */*/*/*/*/*/*.html
svn propset svn:mime-type text/html -R */*/*/*/*/*/*/*.html
svn propset svn:mime-type text/html -R */*/*/*/*/*/*/*/*.html
svn propset svn:mime-type text/html -R */*/*/*/*/*/*/*/*/*.html
svn propset svn:mime-type text/html -R */*/*/*/*/*/*/*/*/*/*.html

