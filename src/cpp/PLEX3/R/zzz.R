`.First.lib` <-
function(libname,pkgname)
{
	library.dynam("PLEX3", pkgname, libname)
	.Call("init_java", jplex.path)
}

