#include <cstdio>
#include <cstdlib>
#include <cmath>

#include <iostream>
#include <string>

#include <map>

#include <jni.h>

#include <R.h>
#include <Rinternals.h>

// predicate for STL algorithms. cannot have C linkage, so outside extern "C"
template <typename T> bool intmap_keycompare(std::pair<int, T> a, std::pair<int, T> b)
{
	return (a.first < b.first);
}

extern "C" {

JavaVM *jvm;
JNIEnv* env;

std::map<std::string, jclass> cl;
std::map<std::string, jmethodID> fn;

int add_class(std::string classname, std::string classpath);
int add_method(std::string classname, std::string methodname, std::string methodsig);

// Global persistence object. No sense in having more than one of these around
jobject p;

// Keep track of the persistent stream & data Java objects. 
// These should all be global refs with finalizers. 
std::map<int, jobject> current_streams;
std::map<int, jobject> current_pointdata;

// Keep track of the distance matrices that need to be protected from the GC
std::map<int, SEXP> protected_objects;

SEXP init_java(SEXP classpath) {
	JavaVMInitArgs initArgs;
	initArgs.version = JNI_VERSION_1_4;

	JavaVMOption options[2];
	std::string classpath_optstr = "-Djava.class.path=";
	classpath_optstr += CHAR(STRING_ELT(classpath,0));

	// these lines require explicit casts to cast away const-ness. I'm trusting
	// Java won't be modifying the options strings.  
	options[0].optionString = (char*)classpath_optstr.c_str();
	options[1].optionString = (char*)"-Djava.version=1.5";
  
    	initArgs.options=options;
    	initArgs.nOptions = 2;

	//jint JNI_GetCreatedJavaVMs(JavaVM **vmBuf, jsize bufLen, jsize *nVMs);
	// See if there's a pre-existing VM, and use it if so. Only the first VM returned
	// is used.
	
	jsize numvms;
	JavaVM *existing_vm;
	JNI_GetCreatedJavaVMs(&existing_vm, 1, &numvms);

	if(numvms > 0)
		existing_vm->AttachCurrentThread((void**)&env, NULL);
	else
		JNI_CreateJavaVM(&jvm, (void**)&env, (void*)&initArgs);

	add_class("RDirectBufferData", "edu/stanford/math/plex/RDirectBufferData");
	add_method("RDirectBufferData", "<init>", "(ILjava/nio/ByteBuffer;)V");
	
	add_class("RipsStream", "edu/stanford/math/plex/RipsStream");
	add_method("RipsStream", "<init>", "(DDILedu/stanford/math/plex/PointData;)V");
	
	add_class("Persistence","edu/stanford/math/plex/Persistence");
	add_method("Persistence", "<init>", "()V");
	add_method("Persistence","computeIntervals", "(Ledu/stanford/math/plex/SimplexStream;)[Ledu/stanford/math/plex/PersistenceInterval$Float;");

	add_class("PersistenceInterval.Float","edu/stanford/math/plex/PersistenceInterval$Float");
	add_method("PersistenceInterval.Float", "<init>", "()V");
	add_method("PersistenceInterval.Float", "2<init>", "(IDD)V");

	add_class("LazyWitnessStream", "edu/stanford/math/plex/LazyWitnessStream");
	add_method("LazyWitnessStream", "<init>", "(DIDI[ILedu/stanford/math/plex/PointData;)V");

	add_class("WitnessStream", "edu/stanford/math/plex/WitnessStream");
	
	add_method("WitnessStream", "<s>makeRandomLandmarks", "(Ledu/stanford/math/plex/PointData;I)[I");
	add_method("WitnessStream", "<s>estimateRmax", "(Ledu/stanford/math/plex/PointData;[I)D");

	add_class("Plex", "edu/stanford/math/plex/Plex");
	add_method("Plex", "<s>FilterInfinite", 
		"([Ledu/stanford/math/plex/PersistenceInterval;)Ledu/stanford/math/plex/Plex$BettiNumbers;");

	add_class("Plex.BettiNumbers", "edu/stanford/math/plex/Plex$BettiNumbers");
	add_method("Plex.BettiNumbers", "toString", "()Ljava/lang/String;");

	add_class("ExplicitStream", "edu/stanford/math/plex/ExplicitStream");
	add_method("ExplicitStream", "<init>", "()V");
	add_method("ExplicitStream", "open", "()V");
	add_method("ExplicitStream", "close", "()V");
	add_method("ExplicitStream", "add", "([ID)V");
	add_method("ExplicitStream", "remove", "([I)V");

	add_class("EuclideanArrayData", "edu/stanford/math/plex/EuclideanArrayData");
	add_method("EuclideanArrayData", "<init>", "([DI)V");	
	add_method("EuclideanArrayData", "2<init>", "(II)V");	

	p = env->NewObject(cl["Persistence"], fn["Persistence.<init>"]);

	return(R_NilValue);
}

SEXP R_exception()
{
	env->ExceptionDescribe();
	env->ExceptionClear();

	SEXP retval;
	PROTECT(retval = allocVector(INTSXP, 1));

	INTEGER(retval)[0] = -1;

	UNPROTECT(1);

	return (retval);
}

static void matrix_finalizer(SEXP handle)
{
	R_ReleaseObject(protected_objects[*INTEGER(R_ExternalPtrTag(handle))]);
	std::cout << "R/PLEX: Distance matrix unprotected." << std::endl;
}

static void java_data_ref_finalizer(SEXP handle)
{
	// Release the JNI global reference. 
	env->DeleteGlobalRef(current_pointdata[*INTEGER(R_ExternalPtrTag(handle))]);
	std::cout << "R/PLEX: Java global ref to PointData deleted." << std::endl;
}

static void java_stream_ref_finalizer(SEXP handle)
{
	// Release the JNI global reference. 
	env->DeleteGlobalRef(current_streams[*INTEGER(R_ExternalPtrTag(handle))]);
	std::cout << "R/PLEX: Java global ref to SimplexStream deleted." << std::endl;
}

// internal use only
jobject create_direct_buffer(SEXP matrix, SEXP buflen)
{

	int d = *INTEGER(buflen);

	if(d < 0)
		return (NULL);

	d = sqrt(d);

	// We lose the handle to the DirectByteBuffer, but since it doesn't own the data itself,
	// this is trivial. Java may even clean it up for us? 

	jobject dbytebuffer = env->NewDirectByteBuffer(REAL(matrix), d*d*sizeof(double));

	if(env->ExceptionOccurred())
	{
		env->ExceptionDescribe();
		env->ExceptionClear();
		return NULL;
	}

	jobject distbuffer = env->NewObject(cl["RDirectBufferData"], fn["RDirectBufferData.<init>"], d, dbytebuffer);

	if(env->ExceptionOccurred())
	{
		env->ExceptionDescribe();
		env->ExceptionClear();
		return NULL;
	}

	// If all goes well, preserve the matrix memory for Java
	R_PreserveObject(matrix);

	return distbuffer;
}


// internal use only
SEXP register_stream(jobject stream)
{
	// get the max key and add one
	int streamkey = (*(std::max_element(current_streams.begin(), current_streams.end(), intmap_keycompare<jobject>))).first + 1;

	current_streams[streamkey] = stream;

	SEXP streamhandle;

	PROTECT(streamhandle = allocVector(INTSXP, 1));

	INTEGER(streamhandle)[0] = streamkey;
 
	UNPROTECT(1);

	// convert the local stream ref into a global stream ref
	// and assign an R finalizer to delete it when all R 
	// references disappear.
	
	env->NewGlobalRef(stream);

	SEXP nhandle;
	PROTECT(nhandle = R_MakeExternalPtr(NULL, streamhandle, R_NilValue));

	R_RegisterCFinalizer(nhandle, java_stream_ref_finalizer);

	UNPROTECT(1);

	return nhandle;; 
}

SEXP register_pointdata(jobject pdata)
{
	// get the max key and add one
	int datakey = (*(std::max_element(current_pointdata.begin(), current_pointdata.end(), intmap_keycompare<jobject>))).first + 1;

	current_pointdata[datakey] = pdata;

	SEXP datahandle;

	PROTECT(datahandle = allocVector(INTSXP, 1));

	INTEGER(datahandle)[0] = datakey;

	UNPROTECT(1);

	// convert the local data ref into a global ref
	// and assign an R finalizer to delete it when all R 
	// references disappear.
	
	env->NewGlobalRef(pdata);

	SEXP nhandle;
	PROTECT(nhandle = R_MakeExternalPtr(NULL, datahandle, R_NilValue));

	R_RegisterCFinalizer(nhandle, java_data_ref_finalizer);

	UNPROTECT(1);

	return nhandle;
}

SEXP make_internal_distance_array(SEXP dmatrix, SEXP buflen)
{
	if(buflen < 0)
		return(R_NilValue);

	jobject pdata = create_direct_buffer(dmatrix, buflen);

	SEXP nhandle = register_pointdata(pdata);	

	// Add finalizer to the handle to clean up the matrix memory, and
	// add matrix to the projected objects list.

	protected_objects[*INTEGER(R_ExternalPtrTag(nhandle))] = dmatrix;
	R_RegisterCFinalizer(nhandle, matrix_finalizer);

	return nhandle; 
}

SEXP random_euclidean_array_data(SEXP count, SEXP dim)
{
	if(*INTEGER(count) < 0 || *INTEGER(dim) < 0)
		return (R_NilValue);

	jobject arr = env->NewObject(cl["EuclideanArrayData"], fn["EuclideanArrayData.2<init>"], *INTEGER(count), *INTEGER(dim));

	if(env->ExceptionOccurred())
		return R_exception();

	return register_pointdata(arr);
}

// also include accessors for pointdata?

SEXP make_random_landmarks(SEXP pdata_handle, SEXP landmark_count)
{
	int sid = *INTEGER(R_ExternalPtrTag(pdata_handle));
	if(current_pointdata.find(sid) == current_pointdata.end())
		return (R_NilValue);

	jobject arr = env->CallStaticObjectMethod(cl["WitnessStream"], fn["WitnessStream.<s>makeRandomLandmarks"],
		current_pointdata[sid], *INTEGER(landmark_count));

	int len = env->GetArrayLength((jintArray)arr);

	SEXP landmarks_arr;

	PROTECT(landmarks_arr = allocVector(INTSXP, len));

	int *parr = env->GetIntArrayElements((jintArray)arr, NULL);
	memcpy(INTEGER(landmarks_arr), parr, len*sizeof(int));
	env->ReleaseIntArrayElements((jintArray)arr, parr, JNI_ABORT);

	UNPROTECT(1);

	if(env->ExceptionOccurred())
		return R_exception();

	return landmarks_arr;
}

SEXP estimate_r_max(SEXP pdata_handle, SEXP landmarks)
{
	// landmarks represented as an array of ints. translate this back into java 

	int sid = *INTEGER(R_ExternalPtrTag(pdata_handle));
	if(current_pointdata.find(sid) == current_pointdata.end())
		return (R_NilValue);

	int len = length(landmarks);

	jintArray landmarkarr = env->NewIntArray(len);
	int *parr = env->GetIntArrayElements(landmarkarr, NULL);

	memcpy(parr, INTEGER(landmarks), len*sizeof(int));
	env->ReleaseIntArrayElements(landmarkarr, parr, 0);

	jdouble rmax = env->CallStaticDoubleMethod(cl["WitnessStream"], fn["WitnessStream.<s>estimateRmax"],
		current_pointdata[sid], landmarkarr);

	SEXP r_rmax;

	PROTECT(r_rmax = allocVector(REALSXP, 1));

	REAL(r_rmax)[0] = rmax;

	UNPROTECT(1);

	if(env->ExceptionOccurred())
		return R_exception();

	return r_rmax;
}

SEXP filter_infinite(SEXP intervals, SEXP intervalcount)
{
	// repack interval vector (of reals) into PersistenceInterval.Floats
	jobjectArray intervalarr = env->NewObjectArray(*INTEGER(intervalcount), cl["PersistenceInterval.Float"], NULL);
	for(int i = 0; i < *INTEGER(intervalcount); i++)
	{
		jobject ival = env->NewObject(cl["PersistenceInterval.Float"], fn["PersistenceInterval.Float.2<init>"],
			(int)(REAL(intervals)[3*i+0] + 0.1), REAL(intervals)[3*i+1], REAL(intervals)[3*i+2]);
		env->SetObjectArrayElement(intervalarr, i, ival);
	}

	jobject bn = env->CallStaticObjectMethod(cl["Plex"], fn["Plex.<s>FilterInfinite"], intervalarr);

	jstring bn_str = (jstring)env->CallObjectMethod(bn, fn["Plex.BettiNumbers.toString"]); 

	SEXP r_bettinumbers;
	const char *sbettinumbers = env->GetStringUTFChars(bn_str, 0);

	PROTECT(r_bettinumbers = allocVector(STRSXP, 1));
	SET_STRING_ELT(r_bettinumbers, 0, mkChar(sbettinumbers));
	UNPROTECT(1);

	env->ReleaseStringUTFChars(bn_str, sbettinumbers);

	if(env->ExceptionOccurred())
		return R_exception();

	return r_bettinumbers;
}

SEXP create_lw_stream_from_handle(SEXP pdata_handle, SEXP granularity, SEXP max_d, SEXP rmax, SEXP nu, SEXP landmarks) 
{
	int sid = *INTEGER(R_ExternalPtrTag(pdata_handle));
	if(current_pointdata.find(sid) == current_pointdata.end())
		return (R_NilValue);

	int len = length(landmarks);

	jintArray landmarkarr = env->NewIntArray(len);
	int *parr = env->GetIntArrayElements(landmarkarr, NULL);

	memcpy(parr, INTEGER(landmarks), len*sizeof(int));
	env->ReleaseIntArrayElements(landmarkarr, parr, 0);

	jobject lw_stream = env->NewObject(cl["LazyWitnessStream"], fn["LazyWitnessStream.<init>"],
		*REAL(granularity), *INTEGER(max_d), *REAL(rmax), *INTEGER(nu), landmarkarr, current_pointdata[sid]);

	if(env->ExceptionOccurred())
		return R_exception();

	return register_stream(lw_stream);
}

SEXP create_rips_stream(SEXP pdata_handle, SEXP delta, SEXP max_len, SEXP max_d, SEXP mat_buflen)
{
	int sid = *INTEGER(R_ExternalPtrTag(pdata_handle));
	if(current_pointdata.find(sid) == current_pointdata.end())
		return (R_NilValue);

	jobject rips_str = env->NewObject(cl["RipsStream"], fn["RipsStream.<init>"], *REAL(delta), *REAL(max_len), *INTEGER(max_d), current_pointdata[sid]);

	if(env->ExceptionOccurred())
		return R_exception();

	return register_stream(rips_str);

}

SEXP create_explicit_stream()
{
	jobject ex_stream = env->NewObject(cl["ExplicitStream"], fn["ExplicitStream.<init>"]);

	if(env->ExceptionOccurred())
		return R_exception();

	return register_stream(ex_stream);
}

SEXP open_ex_stream(SEXP streamid)
{
	int sid = *INTEGER(R_ExternalPtrTag(streamid));
	if(current_streams.find(sid) == current_streams.end())
		return (R_NilValue);

	env->CallObjectMethod(current_streams[sid], fn["ExplicitStream.open"]); 

	if(env->ExceptionOccurred())
		return R_exception();

	return(R_NilValue);
}


SEXP close_ex_stream(SEXP streamid)
{
	int sid = *INTEGER(R_ExternalPtrTag(streamid));
	if(current_streams.find(sid) == current_streams.end())
		return (R_NilValue);

	env->CallObjectMethod(current_streams[sid], fn["ExplicitStream.close"]); 

	if(env->ExceptionOccurred())
		return R_exception();

	return(R_NilValue);
}

SEXP add_simplex(SEXP streamid, SEXP vertices, SEXP param)
{
	int sid = *INTEGER(R_ExternalPtrTag(streamid));
	if(current_streams.find(sid) == current_streams.end())
		return (R_NilValue);

	int len = length(vertices);

	jintArray vertices_arr = env->NewIntArray(len);
	int *parr = env->GetIntArrayElements(vertices_arr, NULL);

	memcpy(parr, INTEGER(vertices), len*sizeof(int));
	env->ReleaseIntArrayElements(vertices_arr, parr, 0);

	env->CallObjectMethod(current_streams[sid], fn["ExplicitStream.add"], vertices_arr, *REAL(param)); 

	if(env->ExceptionOccurred())
		return R_exception();

	return(R_NilValue);
}

SEXP remove_simplex(SEXP streamid, SEXP vertices)
{
	int sid = *INTEGER(R_ExternalPtrTag(streamid));
	if(current_streams.find(sid) == current_streams.end())
		return (R_NilValue);

	int len = length(vertices);

	jintArray vertices_arr = env->NewIntArray(len);
	int *parr = env->GetIntArrayElements(vertices_arr, NULL);

	memcpy(parr, INTEGER(vertices), len*sizeof(int));
	env->ReleaseIntArrayElements(vertices_arr, parr, 0);

	env->CallObjectMethod(current_streams[sid], fn["ExplicitStream.remove"], vertices_arr); 

	if(env->ExceptionOccurred())
		return R_exception();

	return(R_NilValue);
}

SEXP get_intervals_from_stream(SEXP streamid)
{
	int sid = *INTEGER(R_ExternalPtrTag(streamid));

	if(current_streams.find(sid) == current_streams.end())
		return (R_NilValue);

	jobject intervals = env->CallObjectMethod(p, fn["Persistence.computeIntervals"], current_streams[sid]);
	
	jfieldID dimension = env->GetFieldID(cl["PersistenceInterval.Float"], "dimension", "I");
	jfieldID start = env->GetFieldID(cl["PersistenceInterval.Float"], "start", "D");
	jfieldID end = env->GetFieldID(cl["PersistenceInterval.Float"], "end", "D");

	int len = env->GetArrayLength((jobjectArray)intervals);

	SEXP pintervals;

	PROTECT(pintervals = allocVector(REALSXP, 3*len));

	for(int i = 0; i < len; i++)
	{
		jobject firstint = env->GetObjectArrayElement((jobjectArray)intervals, i); 
		REAL(pintervals)[3*i + 0] = (double)env->GetIntField(firstint, dimension); 
		REAL(pintervals)[3*i + 1] = (double)env->GetDoubleField(firstint, start); 
		REAL(pintervals)[3*i + 2] = (double)env->GetDoubleField(firstint, end); 
	}

	UNPROTECT(1);

	if(env->ExceptionOccurred())
		return R_exception();

	return(pintervals);
} 

int add_class(std::string classname, std::string classpath) {
	if(!env)
		return -1;

	jclass t = env->FindClass(classpath.c_str());
	cl[classname] = t;

	if(env->ExceptionOccurred())
	{
		env->ExceptionDescribe();
		env->ExceptionClear();
		return -1;
	}

	return 0;
}

int add_method(std::string classname, std::string methodname, std::string methodsig)
{
	if(!env)
		return -1;

	jmethodID t;

	if(methodname.substr(0,3) == "<s>")
		t = env->GetStaticMethodID(cl[classname], methodname.substr(3).c_str(), methodsig.c_str());
	else if(std::isdigit(methodname.substr(0,1)[0]))
		t = env->GetMethodID(cl[classname], methodname.substr(1).c_str(), methodsig.c_str()); 
	else
		t = env->GetMethodID(cl[classname], methodname.c_str(), methodsig.c_str());

	fn[classname + "." + methodname] = t;

	if(env->ExceptionOccurred())
	{
		env->ExceptionDescribe();
		env->ExceptionClear();
		return -1;
	}
	return 0;
}
}

