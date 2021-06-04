cmake_minimum_required(VERSION 3.4.1)

#externalNativeBuild {
#cmake {
#cppFlags ""
#arguments "-DANDROID_STL=c++_shared"
#}
#}

set(SRC_LIST src/main/cpp/ecg.cpp src/main/cpp/ecg/rtGetInf.cpp src/main/cpp/ecg/rtGetNaN.cpp src/main/cpp/ecg/rt_nonfinite.cpp src/main/cpp/ecg/ecg_emxAPI.cpp src/main/cpp/ecg/ecg_emxutil.cpp src/main/cpp/ecg/filter.cpp src/main/cpp/ecg/sortIdx.cpp src/main/cpp/ecg/sort1.cpp src/main/cpp/ecg/eml_setop.cpp src/main/cpp/ecg/diff.cpp src/main/cpp/ecg/findpeaks.cpp src/main/cpp/ecg/imodwt.cpp src/main/cpp/ecg/ifft.cpp src/main/cpp/ecg/bluestein_setup.cpp src/main/cpp/ecg/fft.cpp src/main/cpp/ecg/modwt.cpp src/main/cpp/ecg/filter_ecg.cpp src/main/cpp/ecg/detect_ecg.cpp src/main/cpp/ecg/ecg_terminate.cpp src/main/cpp/ecg/ecg_initialize.cpp src/main/cpp/ecg/ecg_rtwutil.cpp)

add_library(
             ecg-lib
             SHARED
             src/main/cpp/ecg.cpp src/main/cpp/ecg/rtGetInf.cpp src/main/cpp/ecg/rtGetNaN.cpp src/main/cpp/ecg/rt_nonfinite.cpp src/main/cpp/ecg/ecg_emxAPI.cpp src/main/cpp/ecg/ecg_emxutil.cpp src/main/cpp/ecg/filter.cpp src/main/cpp/ecg/sortIdx.cpp src/main/cpp/ecg/sort1.cpp src/main/cpp/ecg/eml_setop.cpp src/main/cpp/ecg/diff.cpp src/main/cpp/ecg/findpeaks.cpp src/main/cpp/ecg/imodwt.cpp src/main/cpp/ecg/ifft.cpp src/main/cpp/ecg/bluestein_setup.cpp src/main/cpp/ecg/fft.cpp src/main/cpp/ecg/modwt.cpp src/main/cpp/ecg/filter_ecg.cpp src/main/cpp/ecg/detect_ecg.cpp src/main/cpp/ecg/ecg_terminate.cpp src/main/cpp/ecg/ecg_initialize.cpp src/main/cpp/ecg/ecg_rtwutil.cpp)



find_library( # Sets the name of the path variable.
              log-lib
              log )

target_link_libraries( # Specifies the target library.
                       ecg-lib
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )