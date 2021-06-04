//
// File: fft.h
//
// MATLAB Coder version            : 3.2
// C/C++ source code generated on  : 18-Aug-2017 19:21:12
//
#ifndef FFT_H
#define FFT_H

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
extern void b_fft(const emxArray_real_T *x, emxArray_creal_T *y);
extern void c_fft(double varargin_1, emxArray_creal_T *y);
extern void fft(const double x[8], double varargin_1, emxArray_creal_T *y);
extern void generate_twiddle_tables(int nRows, boolean_T useRadix2,
  emxArray_real_T *costab, emxArray_real_T *sintab, emxArray_real_T *sintabinv);

#endif

//
// File trailer for fft.h
//
// [EOF]
//
