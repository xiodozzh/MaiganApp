//
// File: detect_ecg.h
//
// MATLAB Coder version            : 3.2
// C/C++ source code generated on  : 18-Aug-2017 19:21:12
//
#ifndef DETECT_ECG_H
#define DETECT_ECG_H

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
extern void detect_ecg(const emxArray_real_T *Ecg, double Fs, double HrLo,
  double HrHi, double RRNum, double *err, double *rrNum, double *hrAvg, double
  *vMax, double *vMin);

#endif

//
// File trailer for detect_ecg.h
//
// [EOF]
//
