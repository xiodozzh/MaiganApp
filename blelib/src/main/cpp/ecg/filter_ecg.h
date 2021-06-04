//
// File: filter_ecg.h
//
// MATLAB Coder version            : 3.2
// C/C++ source code generated on  : 18-Aug-2017 19:21:12
//
#ifndef FILTER_ECG_H
#define FILTER_ECG_H

// Include Files
#include <cmath>
#include <math.h>
#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include "rt_nonfinite.h"
#include "rtwtypes.h"
#include "ecg_types.h"

// Function Declarations
extern void filter_ecg(const emxArray_real_T *Ecg, double Fs, emxArray_real_T
  *ret);

#endif

//
// File trailer for filter_ecg.h
//
// [EOF]
//
