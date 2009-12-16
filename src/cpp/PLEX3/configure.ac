AC_INIT([src/rbind_jni.cpp])

AC_PROG_CC
AC_PROG_CXX

AC_PROG_INSTALL

AC_PROG_GCC_TRADITIONAL
AC_FUNC_MEMCMP
AC_FUNC_MALLOC
AC_FUNC_STRTOD
AC_TYPE_SIGNAL
AC_FUNC_VPRINTF

AC_CANONICAL_HOST

AC_ARG_WITH([jplex],
		AC_HELP_STRING([--with-jplex=JPLEX_PATH],
			[path to the jplex.jar file]),
		[jplex_path=$withval])
if test [ -n "$jplex_path" ] ; then
JPLEX_PATH="${jplex_path}"
else
JPLEX_PATH="${HOME}/plex3/jplex.jar"
fi

dnl Write the config.R file

echo "\`jplex.path\` <- \"${JPLEX_PATH}\";" > R/config.R

dnl Now find the compiler and compiler flags to use
: ${R_HOME=`R RHOME`}
if test -z "${R_HOME}"; then
echo "could not determine R_HOME"
exit 1
fi

CC=`"${R_HOME}/bin/R" CMD config CC`
CFLAGS=`"${R_HOME}/bin/R" CMD config CFLAGS`
JAVA_HOME=`"${R_HOME}/bin/R" CMD config JAVA_HOME`
JAVA_LIBS=`"${R_HOME}/bin/R" CMD config JAVA_LIBS`
JAVA_CPPFLAGS=`"${R_HOME}/bin/R" CMD config JAVA_CPPFLAGS`

dnl The following line would be preferrable. However, R CMD config is broken.
dnl JAVA_LD_LIBRARY_PATH=`"${R_HOME}/bin/R" CMD config JAVA_LD_LIBRARY_PATH` 
dnl This is a workaround, looking at Makeconf directly:

JAVA_LD_LIBRARY_PATH=`grep -e "^JAVA_LD_LIBRARY_PATH = " ${R_HOME}/etc/Makeconf | awk '{print $3}'`

AC_SUBST([CPPFLAGS],["${JAVA_CPPFLAGS} ${CPPFLAGS}"])
AC_SUBST([LIBS],["${JAVA_LIBS} -Wl,-rpath,${JAVA_LD_LIBRARY_PATH} ${LIBS}"])

AC_SUBST(CPPFLAGS)
AC_SUBST(LIBS)

dnl Hopefully, R CMD javareconf has already been run and we have our variables.
dnl If not, print a message telling them to get their act together.

AC_CHECK_HEADERS(jni.h)
	if test "${ac_cv_header_jni_h}" = no ; then 
	AC_MSG_ERROR("Necessary JNI header jni.h not found. Did you remember to run R CMD javareconf? ")
	fi

AC_OUTPUT(src/Makevars)

