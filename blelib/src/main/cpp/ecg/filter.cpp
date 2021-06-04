//
// File: filter.cpp
//
// MATLAB Coder version            : 3.2
// C/C++ source code generated on  : 18-Aug-2017 19:21:12
//

// Include Files
#include "rt_nonfinite.h"
#include "detect_ecg.h"
#include "filter_ecg.h"
#include "filter.h"
#include "ecg_emxutil.h"

// Function Definitions

//
// Arguments    : const double b[5]
//                const double a[5]
//                const emxArray_real_T *x
//                emxArray_real_T *y
// Return Type  : void
//
void filter(const double b[5], const double a[5], const emxArray_real_T *x,
            emxArray_real_T *y)
{
  int k;
  int nx;
  int naxpy;
  int j;
  double as;
  k = y->size[0] * y->size[1];
  y->size[0] = 1;
  y->size[1] = x->size[1];
  emxEnsureCapacity((emxArray__common *)y, k, (int)sizeof(double));
  nx = x->size[1];
  k = y->size[0] * y->size[1];
  y->size[0] = 1;
  emxEnsureCapacity((emxArray__common *)y, k, (int)sizeof(double));
  naxpy = y->size[1];
  for (k = 0; k < naxpy; k++) {
    y->data[y->size[0] * k] = 0.0;
  }

  for (k = 1; k <= nx; k++) {
    naxpy = (nx - k) + 1;
    if (!(naxpy <= 5)) {
      naxpy = 5;
    }

    for (j = -1; j + 2 <= naxpy; j++) {
      y->data[k + j] += x->data[k - 1] * b[j + 1];
    }

    naxpy = nx - k;
    if (!(naxpy <= 4)) {
      naxpy = 4;
    }

    as = -y->data[k - 1];
    for (j = 1; j <= naxpy; j++) {
      y->data[(k + j) - 1] += as * a[j];
    }
  }
}

//
// File trailer for filter.cpp
//
// [EOF]
//
