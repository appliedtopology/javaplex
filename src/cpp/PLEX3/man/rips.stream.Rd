\name{rips.stream}
\alias{rips.stream}
\title{Rips Stream}
\description{
	Construct a Rips Stream from either a distance matrix or a handle to a dataset made with
		\code{as.internal.distance.matrix}.
}
	\usage{
		rips.stream(dmatrix, delta, max.edge, max.dim)
	}
\arguments{
	\item{dmatrix}{A distance matrix}
	\item{delta}{ No clue }
	\item{max.edge}{ No clue }
	\item{max.dim}{ No clue }
}
\seealso{
	\code{\link{save}}.
}
\examples{
## Load point data from a CSV file
	pointdata <- read.table("plex_torus_data.txt")
## Create the RipsStream from the distance matrix
		stream <- rips.stream(pointdata)
## Calculate persistence intervals from the RipsStream
		intervals <- persistence.intervals(stream)
## Use FilterInfinite to get Betti numbers
		filter.infinite(intervals)

}
\keyword{stream}
