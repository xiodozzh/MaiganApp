//
// File: filter.h
//
// MATLAB Coder version            : 3.2
// C/C++ source code generated on  : 18-Aug-2017 19:21:12
//
#ifndef FILTER_H
#define FILTER_H

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
extern void filter(const double b[5], const double a[5], const emxArray_real_T
                   *x, emxArray_real_T *y);

#endif

//
// File trailer for filter.h
//
// [EOF]
//
