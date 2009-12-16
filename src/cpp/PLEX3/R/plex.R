
`as.internal.distance.matrix` <- function(dmatrix)
{
	pdata.handle = .Call("make_internal_distance_array", as.matrix(dmatrix), as.integer(length(as.matrix(dmatrix))))
	attr(pdata.handle, 'class') <- 'pointdata'

	pdata.handle
}

`random.euclidean.array.data` <- function(count, dim)
{
	pdata.handle = .Call("random_euclidean_array_data", as.integer(count), as.integer(dim))
	attr(pdata.handle, 'class') <- 'pointdata'

	pdata.handle
}

`ensure.square` <- function(mat)
{
	if(	(is.null(mat) == FALSE) 
	   && (is.null(dim(mat)) == FALSE) 
	   && (dim(mat)[1] == dim(mat)[2]))
		TRUE
	else
		FALSE
}

`open.explicit.stream` <- function(ex.stream) 
{
	.Call("open_ex_stream", ex.stream)
	eval(eval(substitute(expression(attr(ex.stream,'is.open') <<- TRUE))))

	invisible()
}

`close.explicit.stream` <- function(ex.stream)
{
	if(is.null(.Call("close_ex_stream", ex.stream)) == FALSE)
		stop("Stream not in a valid state to be closed.")
	
	eval(eval(substitute(expression(attr(ex.stream,'is.open') <<- FALSE))))

	invisible()
}

`add.simplex` <- function(ex.stream, vertices, parameter)
{
	if(attr(ex.stream, 'is.open') == FALSE)
		stop("You must open the stream before adding or removing simplices.")

	.Call("add_simplex", ex.stream, as.integer(vertices), as.double(parameter))

	invisible()
}

`remove.simplex` <- function(ex.stream, vertices)
{
	if(attr(ex.stream, 'is.open') == FALSE)
		stop("You must open the stream before adding or removing simplices.")

	.Call("remove_simplex", ex.stream, as.integer(vertices))

	invisible()
}

`explicit.stream` <- function(x, ...) UseMethod("explicit.stream")

`explicit.stream.pointdata` <- function(dmatrix)
{
	stop("Simplicies must be added explicitly to an explicit stream.")
}

`explicit.stream.default` <- function()
{
	exstream = .Call("create_explicit_stream")
	attr(exstream, 'class') <- 'explicit.stream'
	attr(exstream, 'is.open') <- FALSE

	exstream
}


`rips.stream` <- function(x, ...) UseMethod("rips.stream")

`rips.stream.pointdata` <- function(pdata.handle, delta, max.edge, max.dim)
{
	rstream = .Call("create_rips_stream", pdata.handle, as.double(delta), as.double(max.edge), as.integer(max.dim))
	attr(rstream, 'class') <- 'rips.stream'

	rstream
}

`rips.stream.default` <- function(dmatrix, delta, max.edge, max.dim)
{
	# ensure the matrix is square and has values
	if(ensure.square(dmatrix))
	{
		i <- as.internal.distance.matrix(dmatrix)
		rips.stream(i, delta, max.edge, max.dim)
	}
	else
	{
		stop("This function cannot operate on non-square or null matrices.")
	}
}

`make.random.landmarks` <- function(x, ...) UseMethod("make.random.landmarks")

`make.random.landmarks.pointdata` <- function(pdata.handle, count)
{
	.Call("make_random_landmarks", pdata.handle, as.integer(count))
}

`make.random.landmarks.default` <- function(dmatrix, count)
{
	# ensure the matrix is square and has values
	if(ensure.square(dmatrix))
	{
		i <- as.internal.distance.matrix(dmatrix)
		make.random.landmarks(i,count)
	}
	else
	{
		stop("This function cannot operate on non-square or null matrices.")
	}
}

`estimate.rmax` <- function(x, ...) UseMethod("estimate.rmax")

`estimate.rmax.pointdata` <- function(pdata.handle, landmarks)
{
	.Call("estimate_r_max", pdata.handle, as.integer(landmarks))
}

`estimate.rmax.default` <- function(dmatrix, landmarks)
{
	# ensure the matrix is square and has values
	if(ensure.square(dmatrix))
	{
		i <- as.internal.distance.matrix(dmatrix)
		estimate.rmax(i,landmarks)
	}
	else
	{
		stop("This function cannot operate on non-square or null matrices.")
	}
}

`lazy.witness.stream` <- function(x, ...) UseMethod("lazy.witness.stream")

`lazy.witness.stream.pointdata` <- function(pdata.handle, granularity, max.dim, rmax, nu, landmarks)
{
	lwstream = .Call("create_lw_stream_from_handle", pdata.handle, 
			as.double(granularity), as.integer(max.dim), as.double(rmax), 
			as.integer(nu), as.vector(landmarks))
	attr(lwstream, 'class') <- 'lazy.witness.stream'

	lwstream
}

`lazy.witness.stream.default` <- function(dmatrix, granularity, max.dim, rmax, nu, landmarks)
{
	# ensure the matrix is square and has values
	if(ensure.square(dmatrix))
	{
		i <- as.internal.distance.matrix(dmatrix)
		lazy.witness.stream(i,granularity, max.dim, rmax, nu, landmarks)
	}
	else
	{
		stop("This function cannot operate on non-square or null matrices.")
	}
}

`persistence.intervals` <- function(x, ...) UseMethod("persistence.intervals")

`persistence.intervals.explicit.stream` <- function(exstream)
{
	close(exstream)
	persistence.intervals.from.stream(exstream)
}

`persistence.intervals.rips.stream` <- function(exstream)
{
	persistence.intervals.from.stream(exstream)
}

`persistence.intervals.lazy.witness.stream` <- function(exstream)
{
	persistence.intervals.from.stream(exstream)
}

`length.persistence.intervals` <- function(intervals)
{
	length(as.vector(intervals)) / 3
}

# this function should not be called directly
`persistence.intervals.from.stream` <- function(exstream)
{
	intervals = .Call("get_intervals_from_stream", exstream)
	attr(intervals, 'dim') <- c(3,length(intervals)/3)
	attr(intervals, 'class') <- 'persistence.intervals'
	intervals = t(intervals)
	
	intervals
}

`filter.infinite` <- function(intervals)
{
	.Call("filter_infinite", as.double(t(intervals)), as.integer(length(intervals)))
}
