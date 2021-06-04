//
// File: fft.cpp
//
// MATLAB Coder version            : 3.2
// C/C++ source code generated on  : 18-Aug-2017 19:21:12
//

// Include Files
#include "rt_nonfinite.h"
#include "detect_ecg.h"
#include "filter_ecg.h"
#include "fft.h"
#include "ecg_emxutil.h"
#include "ifft.h"
#include "bluestein_setup.h"

// Function Declarations
static void b_eml_fft(const emxArray_real_T *x, int n, emxArray_creal_T *y);
static void eml_fft(const double x[8], int n, emxArray_creal_T *y);

// Function Definitions

//
// Arguments    : const emxArray_real_T *x
//                int n
//                emxArray_creal_T *y
// Return Type  : void
//
static void b_eml_fft(const emxArray_real_T *x, int n, emxArray_creal_T *y)
{
  emxArray_real_T *costab1q;
  boolean_T useRadix2;
  int j;
  int nd2;
  double e;
  int nRowsD4;
  int ju;
  emxArray_real_T *costab;
  emxArray_real_T *sintab;
  emxArray_real_T *sintabinv;
  int b_n;
  emxArray_creal_T *wwc;
  int ihi;
  int minNrowsNx;
  int nRowsD2;
  int i;
  double twid_im;
  emxArray_creal_T *fy;
  emxArray_creal_T *fv;
  double temp_re;
  double temp_im;
  double fv_re;
  double fv_im;
  double wwc_im;
  double b_fv_re;
  emxInit_real_T(&costab1q, 2);
  useRadix2 = ((n & (n - 1)) == 0);
  get_algo_sizes(n, useRadix2, &j, &nd2);
  e = 6.2831853071795862 / (double)nd2;
  nRowsD4 = nd2 / 2 / 2;
  ju = costab1q->size[0] * costab1q->size[1];
  costab1q->size[0] = 1;
  costab1q->size[1] = nRowsD4 + 1;
  emxEnsureCapacity((emxArray__common *)costab1q, ju, (int)sizeof(double));
  costab1q->data[0] = 1.0;
  nd2 = nRowsD4 / 2;
  for (ju = 1; ju <= nd2; ju++) {
    costab1q->data[ju] = std::cos(e * (double)ju);
  }

  for (ju = nd2 + 1; ju < nRowsD4; ju++) {
    costab1q->data[ju] = std::sin(e * (double)(nRowsD4 - ju));
  }

  costab1q->data[nRowsD4] = 0.0;
  emxInit_real_T(&costab, 2);
  emxInit_real_T(&sintab, 2);
  emxInit_real_T(&sintabinv, 2);
  if (!useRadix2) {
    b_n = costab1q->size[1] - 1;
    nd2 = (costab1q->size[1] - 1) << 1;
    ju = costab->size[0] * costab->size[1];
    costab->size[0] = 1;
    costab->size[1] = nd2 + 1;
    emxEnsureCapacity((emxArray__common *)costab, ju, (int)sizeof(double));
    ju = sintab->size[0] * sintab->size[1];
    sintab->size[0] = 1;
    sintab->size[1] = nd2 + 1;
    emxEnsureCapacity((emxArray__common *)sintab, ju, (int)sizeof(double));
    costab->data[0] = 1.0;
    sintab->data[0] = 0.0;
    ju = sintabinv->size[0] * sintabinv->size[1];
    sintabinv->size[0] = 1;
    sintabinv->size[1] = nd2 + 1;
    emxEnsureCapacity((emxArray__common *)sintabinv, ju, (int)sizeof(double));
    for (ju = 1; ju <= b_n; ju++) {
      sintabinv->data[ju] = costab1q->data[b_n - ju];
    }

    for (ju = costab1q->size[1]; ju <= nd2; ju++) {
      sintabinv->data[ju] = costab1q->data[ju - b_n];
    }

    for (ju = 1; ju <= b_n; ju++) {
      costab->data[ju] = costab1q->data[ju];
      sintab->data[ju] = -costab1q->data[b_n - ju];
    }

    for (ju = costab1q->size[1]; ju <= nd2; ju++) {
      costab->data[ju] = -costab1q->data[nd2 - ju];
      sintab->data[ju] = -costab1q->data[ju - b_n];
    }
  } else {
    b_n = costab1q->size[1] - 1;
    nd2 = (costab1q->size[1] - 1) << 1;
    ju = costab->size[0] * costab->size[1];
    costab->size[0] = 1;
    costab->size[1] = nd2 + 1;
    emxEnsureCapacity((emxArray__common *)costab, ju, (int)sizeof(double));
    ju = sintab->size[0] * sintab->size[1];
    sintab->size[0] = 1;
    sintab->size[1] = nd2 + 1;
    emxEnsureCapacity((emxArray__common *)sintab, ju, (int)sizeof(double));
    costab->data[0] = 1.0;
    sintab->data[0] = 0.0;
    for (ju = 1; ju <= b_n; ju++) {
      costab->data[ju] = costab1q->data[ju];
      sintab->data[ju] = -costab1q->data[b_n - ju];
    }

    for (ju = costab1q->size[1]; ju <= nd2; ju++) {
      costab->data[ju] = -costab1q->data[nd2 - ju];
      sintab->data[ju] = -costab1q->data[ju - b_n];
    }

    ju = sintabinv->size[0] * sintabinv->size[1];
    sintabinv->size[0] = 1;
    sintabinv->size[1] = 0;
    emxEnsureCapacity((emxArray__common *)sintabinv, ju, (int)sizeof(double));
  }

  emxFree_real_T(&costab1q);
  if (useRadix2) {
    ihi = x->size[0];
    if (ihi <= n) {
      ihi = x->size[0];
    } else {
      ihi = n;
    }

    nRowsD2 = n / 2;
    nRowsD4 = nRowsD2 / 2;
    ju = y->size[0];
    y->size[0] = n;
    emxEnsureCapacity((emxArray__common *)y, ju, (int)sizeof(creal_T));
    nd2 = x->size[0];
    if (n > nd2) {
      nd2 = y->size[0];
      ju = y->size[0];
      y->size[0] = nd2;
      emxEnsureCapacity((emxArray__common *)y, ju, (int)sizeof(creal_T));
      for (ju = 0; ju < nd2; ju++) {
        y->data[ju].re = 0.0;
        y->data[ju].im = 0.0;
      }
    }

    minNrowsNx = 0;
    ju = 0;
    nd2 = 0;
    for (i = 1; i < ihi; i++) {
      y->data[nd2].re = x->data[minNrowsNx];
      y->data[nd2].im = 0.0;
      b_n = n;
      useRadix2 = true;
      while (useRadix2) {
        b_n >>= 1;
        ju ^= b_n;
        useRadix2 = ((ju & b_n) == 0);
      }

      nd2 = ju;
      minNrowsNx++;
    }

    y->data[nd2].re = x->data[minNrowsNx];
    y->data[nd2].im = 0.0;
    for (i = 0; i <= n - 2; i += 2) {
      temp_re = y->data[i + 1].re;
      temp_im = y->data[i + 1].im;
      y->data[i + 1].re = y->data[i].re - y->data[i + 1].re;
      y->data[i + 1].im = y->data[i].im - y->data[i + 1].im;
      y->data[i].re += temp_re;
      y->data[i].im += temp_im;
    }

    nd2 = 2;
    minNrowsNx = 4;
    ju = 1 + ((nRowsD4 - 1) << 2);
    while (nRowsD4 > 0) {
      for (i = 0; i < ju; i += minNrowsNx) {
        temp_re = y->data[i + nd2].re;
        temp_im = y->data[i + nd2].im;
        y->data[i + nd2].re = y->data[i].re - temp_re;
        y->data[i + nd2].im = y->data[i].im - temp_im;
        y->data[i].re += temp_re;
        y->data[i].im += temp_im;
      }

      b_n = 1;
      for (j = nRowsD4; j < nRowsD2; j += nRowsD4) {
        e = costab->data[j];
        twid_im = sintab->data[j];
        i = b_n;
        ihi = b_n + ju;
        while (i < ihi) {
          temp_re = e * y->data[i + nd2].re - twid_im * y->data[i + nd2].im;
          temp_im = e * y->data[i + nd2].im + twid_im * y->data[i + nd2].re;
          y->data[i + nd2].re = y->data[i].re - temp_re;
          y->data[i + nd2].im = y->data[i].im - temp_im;
          y->data[i].re += temp_re;
          y->data[i].im += temp_im;
          i += minNrowsNx;
        }

        b_n++;
      }

      nRowsD4 /= 2;
      nd2 = minNrowsNx;
      minNrowsNx <<= 1;
      ju -= nd2;
    }
  } else {
    emxInit_creal_T1(&wwc, 1);
    bluestein_setup(n, wwc);
    ihi = x->size[0];
    if (n <= ihi) {
      minNrowsNx = n;
    } else {
      minNrowsNx = x->size[0];
    }

    ju = y->size[0];
    y->size[0] = n;
    emxEnsureCapacity((emxArray__common *)y, ju, (int)sizeof(creal_T));
    ihi = x->size[0];
    if (n > ihi) {
      nd2 = y->size[0];
      ju = y->size[0];
      y->size[0] = nd2;
      emxEnsureCapacity((emxArray__common *)y, ju, (int)sizeof(creal_T));
      for (ju = 0; ju < nd2; ju++) {
        y->data[ju].re = 0.0;
        y->data[ju].im = 0.0;
      }
    }

    nd2 = 0;
    for (ju = 0; ju + 1 <= minNrowsNx; ju++) {
      e = wwc->data[(n + ju) - 1].re;
      twid_im = wwc->data[(n + ju) - 1].im;
      y->data[ju].re = e * x->data[nd2];
      y->data[ju].im = twid_im * -x->data[nd2];
      nd2++;
    }

    while (minNrowsNx + 1 <= n) {
      y->data[minNrowsNx].re = 0.0;
      y->data[minNrowsNx].im = 0.0;
      minNrowsNx++;
    }

    emxInit_creal_T1(&fy, 1);
    emxInit_creal_T1(&fv, 1);
    r2br_r2dit_trig_impl(y, j, costab, sintab, fy);
    r2br_r2dit_trig(wwc, j, costab, sintab, fv);
    ju = fy->size[0];
    emxEnsureCapacity((emxArray__common *)fy, ju, (int)sizeof(creal_T));
    nd2 = fy->size[0];
    for (ju = 0; ju < nd2; ju++) {
      e = fy->data[ju].re;
      twid_im = fy->data[ju].im;
      fv_re = fv->data[ju].re;
      fv_im = fv->data[ju].im;
      fy->data[ju].re = e * fv_re - twid_im * fv_im;
      fy->data[ju].im = e * fv_im + twid_im * fv_re;
    }

    b_r2br_r2dit_trig(fy, j, costab, sintabinv, fv);
    nd2 = 0;
    ju = n - 1;
    emxFree_creal_T(&fy);
    while (ju + 1 <= wwc->size[0]) {
      e = wwc->data[ju].re;
      fv_re = fv->data[ju].re;
      twid_im = wwc->data[ju].im;
      fv_im = fv->data[ju].im;
      temp_re = wwc->data[ju].re;
      temp_im = fv->data[ju].im;
      wwc_im = wwc->data[ju].im;
      b_fv_re = fv->data[ju].re;
      y->data[nd2].re = e * fv_re + twid_im * fv_im;
      y->data[nd2].im = temp_re * temp_im - wwc_im * b_fv_re;
      nd2++;
      ju++;
    }

    emxFree_creal_T(&fv);
    emxFree_creal_T(&wwc);
  }

  emxFree_real_T(&sintabinv);
  emxFree_real_T(&sintab);
  emxFree_real_T(&costab);
}

//
// Arguments    : const double x[8]
//                int n
//                emxArray_creal_T *y
// Return Type  : void
//
static void eml_fft(const double x[8], int n, emxArray_creal_T *y)
{
  emxArray_real_T *costab1q;
  boolean_T useRadix2;
  int istart;
  int nd2;
  double e;
  int nRowsD4;
  int ju;
  emxArray_real_T *costab;
  emxArray_real_T *sintab;
  emxArray_real_T *sintabinv;
  int b_n;
  double b_x[8];
  emxArray_creal_T *wwc;
  int minNrowsNx;
  int nRowsD2;
  int i;
  double twid_im;
  emxArray_creal_T *fy;
  emxArray_creal_T *fv;
  double temp_re;
  double temp_im;
  double fv_re;
  double fv_im;
  int ihi;
  double wwc_im;
  double b_fv_re;
  emxInit_real_T(&costab1q, 2);
  useRadix2 = ((n & (n - 1)) == 0);
  get_algo_sizes(n, useRadix2, &istart, &nd2);
  e = 6.2831853071795862 / (double)nd2;
  nRowsD4 = nd2 / 2 / 2;
  ju = costab1q->size[0] * costab1q->size[1];
  costab1q->size[0] = 1;
  costab1q->size[1] = nRowsD4 + 1;
  emxEnsureCapacity((emxArray__common *)costab1q, ju, (int)sizeof(double));
  costab1q->data[0] = 1.0;
  nd2 = nRowsD4 / 2;
  for (ju = 1; ju <= nd2; ju++) {
    costab1q->data[ju] = std::cos(e * (double)ju);
  }

  for (ju = nd2 + 1; ju < nRowsD4; ju++) {
    costab1q->data[ju] = std::sin(e * (double)(nRowsD4 - ju));
  }

  costab1q->data[nRowsD4] = 0.0;
  emxInit_real_T(&costab, 2);
  emxInit_real_T(&sintab, 2);
  emxInit_real_T(&sintabinv, 2);
  if (!useRadix2) {
    b_n = costab1q->size[1] - 1;
    nd2 = (costab1q->size[1] - 1) << 1;
    ju = costab->size[0] * costab->size[1];
    costab->size[0] = 1;
    costab->size[1] = nd2 + 1;
    emxEnsureCapacity((emxArray__common *)costab, ju, (int)sizeof(double));
    ju = sintab->size[0] * sintab->size[1];
    sintab->size[0] = 1;
    sintab->size[1] = nd2 + 1;
    emxEnsureCapacity((emxArray__common *)sintab, ju, (int)sizeof(double));
    costab->data[0] = 1.0;
    sintab->data[0] = 0.0;
    ju = sintabinv->size[0] * sintabinv->size[1];
    sintabinv->size[0] = 1;
    sintabinv->size[1] = nd2 + 1;
    emxEnsureCapacity((emxArray__common *)sintabinv, ju, (int)sizeof(double));
    for (ju = 1; ju <= b_n; ju++) {
      sintabinv->data[ju] = costab1q->data[b_n - ju];
    }

    for (ju = costab1q->size[1]; ju <= nd2; ju++) {
      sintabinv->data[ju] = costab1q->data[ju - b_n];
    }

    for (ju = 1; ju <= b_n; ju++) {
      costab->data[ju] = costab1q->data[ju];
      sintab->data[ju] = -costab1q->data[b_n - ju];
    }

    for (ju = costab1q->size[1]; ju <= nd2; ju++) {
      costab->data[ju] = -costab1q->data[nd2 - ju];
      sintab->data[ju] = -costab1q->data[ju - b_n];
    }
  } else {
    b_n = costab1q->size[1] - 1;
    nd2 = (costab1q->size[1] - 1) << 1;
    ju = costab->size[0] * costab->size[1];
    costab->size[0] = 1;
    costab->size[1] = nd2 + 1;
    emxEnsureCapacity((emxArray__common *)costab, ju, (int)sizeof(double));
    ju = sintab->size[0] * sintab->size[1];
    sintab->size[0] = 1;
    sintab->size[1] = nd2 + 1;
    emxEnsureCapacity((emxArray__common *)sintab, ju, (int)sizeof(double));
    costab->data[0] = 1.0;
    sintab->data[0] = 0.0;
    for (ju = 1; ju <= b_n; ju++) {
      costab->data[ju] = costab1q->data[ju];
      sintab->data[ju] = -costab1q->data[b_n - ju];
    }

    for (ju = costab1q->size[1]; ju <= nd2; ju++) {
      costab->data[ju] = -costab1q->data[nd2 - ju];
      sintab->data[ju] = -costab1q->data[ju - b_n];
    }

    ju = sintabinv->size[0] * sintabinv->size[1];
    sintabinv->size[0] = 1;
    sintabinv->size[1] = 0;
    emxEnsureCapacity((emxArray__common *)sintabinv, ju, (int)sizeof(double));
  }

  emxFree_real_T(&costab1q);
  if (useRadix2) {
    memcpy(&b_x[0], &x[0], sizeof(double) << 3);
    if (8 <= n) {
      istart = 8;
    } else {
      istart = n;
    }

    nRowsD2 = n / 2;
    nRowsD4 = nRowsD2 / 2;
    ju = y->size[0];
    y->size[0] = n;
    emxEnsureCapacity((emxArray__common *)y, ju, (int)sizeof(creal_T));
    if (n > 8) {
      nd2 = y->size[0];
      ju = y->size[0];
      y->size[0] = nd2;
      emxEnsureCapacity((emxArray__common *)y, ju, (int)sizeof(creal_T));
      for (ju = 0; ju < nd2; ju++) {
        y->data[ju].re = 0.0;
        y->data[ju].im = 0.0;
      }
    }

    minNrowsNx = 0;
    ju = 0;
    nd2 = 0;
    for (i = 1; i < istart; i++) {
      y->data[nd2].re = b_x[minNrowsNx];
      y->data[nd2].im = 0.0;
      b_n = n;
      useRadix2 = true;
      while (useRadix2) {
        b_n >>= 1;
        ju ^= b_n;
        useRadix2 = ((ju & b_n) == 0);
      }

      nd2 = ju;
      minNrowsNx++;
    }

    y->data[nd2].re = b_x[minNrowsNx];
    y->data[nd2].im = 0.0;
    for (i = 0; i <= n - 2; i += 2) {
      temp_re = y->data[i + 1].re;
      temp_im = y->data[i + 1].im;
      y->data[i + 1].re = y->data[i].re - y->data[i + 1].re;
      y->data[i + 1].im = y->data[i].im - y->data[i + 1].im;
      y->data[i].re += temp_re;
      y->data[i].im += temp_im;
    }

    nd2 = 2;
    minNrowsNx = 4;
    ju = 1 + ((nRowsD4 - 1) << 2);
    while (nRowsD4 > 0) {
      for (i = 0; i < ju; i += minNrowsNx) {
        temp_re = y->data[i + nd2].re;
        temp_im = y->data[i + nd2].im;
        y->data[i + nd2].re = y->data[i].re - temp_re;
        y->data[i + nd2].im = y->data[i].im - temp_im;
        y->data[i].re += temp_re;
        y->data[i].im += temp_im;
      }

      istart = 1;
      for (b_n = nRowsD4; b_n < nRowsD2; b_n += nRowsD4) {
        e = costab->data[b_n];
        twid_im = sintab->data[b_n];
        i = istart;
        ihi = istart + ju;
        while (i < ihi) {
          temp_re = e * y->data[i + nd2].re - twid_im * y->data[i + nd2].im;
          temp_im = e * y->data[i + nd2].im + twid_im * y->data[i + nd2].re;
          y->data[i + nd2].re = y->data[i].re - temp_re;
          y->data[i + nd2].im = y->data[i].im - temp_im;
          y->data[i].re += temp_re;
          y->data[i].im += temp_im;
          i += minNrowsNx;
        }

        istart++;
      }

      nRowsD4 /= 2;
      nd2 = minNrowsNx;
      minNrowsNx <<= 1;
      ju -= nd2;
    }
  } else {
    memcpy(&b_x[0], &x[0], sizeof(double) << 3);
    emxInit_creal_T1(&wwc, 1);
    bluestein_setup(n, wwc);
    if (n <= 8) {
      minNrowsNx = n;
    } else {
      minNrowsNx = 8;
    }

    ju = y->size[0];
    y->size[0] = n;
    emxEnsureCapacity((emxArray__common *)y, ju, (int)sizeof(creal_T));
    if (n > 8) {
      nd2 = y->size[0];
      ju = y->size[0];
      y->size[0] = nd2;
      emxEnsureCapacity((emxArray__common *)y, ju, (int)sizeof(creal_T));
      for (ju = 0; ju < nd2; ju++) {
        y->data[ju].re = 0.0;
        y->data[ju].im = 0.0;
      }
    }

    nd2 = 0;
    for (ju = 0; ju + 1 <= minNrowsNx; ju++) {
      e = wwc->data[(n + ju) - 1].re;
      twid_im = wwc->data[(n + ju) - 1].im;
      y->data[ju].re = e * b_x[nd2];
      y->data[ju].im = twid_im * -b_x[nd2];
      nd2++;
    }

    while (minNrowsNx + 1 <= n) {
      y->data[minNrowsNx].re = 0.0;
      y->data[minNrowsNx].im = 0.0;
      minNrowsNx++;
    }

    emxInit_creal_T1(&fy, 1);
    emxInit_creal_T1(&fv, 1);
    r2br_r2dit_trig_impl(y, istart, costab, sintab, fy);
    r2br_r2dit_trig(wwc, istart, costab, sintab, fv);
    ju = fy->size[0];
    emxEnsureCapacity((emxArray__common *)fy, ju, (int)sizeof(creal_T));
    nd2 = fy->size[0];
    for (ju = 0; ju < nd2; ju++) {
      e = fy->data[ju].re;
      twid_im = fy->data[ju].im;
      fv_re = fv->data[ju].re;
      fv_im = fv->data[ju].im;
      fy->data[ju].re = e * fv_re - twid_im * fv_im;
      fy->data[ju].im = e * fv_im + twid_im * fv_re;
    }

    b_r2br_r2dit_trig(fy, istart, costab, sintabinv, fv);
    nd2 = 0;
    ju = n - 1;
    emxFree_creal_T(&fy);
    while (ju + 1 <= wwc->size[0]) {
      e = wwc->data[ju].re;
      fv_re = fv->data[ju].re;
      twid_im = wwc->data[ju].im;
      fv_im = fv->data[ju].im;
      temp_re = wwc->data[ju].re;
      temp_im = fv->data[ju].im;
      wwc_im = wwc->data[ju].im;
      b_fv_re = fv->data[ju].re;
      y->data[nd2].re = e * fv_re + twid_im * fv_im;
      y->data[nd2].im = temp_re * temp_im - wwc_im * b_fv_re;
      nd2++;
      ju++;
    }

    emxFree_creal_T(&fv);
    emxFree_creal_T(&wwc);
  }

  emxFree_real_T(&sintabinv);
  emxFree_real_T(&sintab);
  emxFree_real_T(&costab);
}

//
// Arguments    : const emxArray_real_T *x
//                emxArray_creal_T *y
// Return Type  : void
//
void b_fft(const emxArray_real_T *x, emxArray_creal_T *y)
{
  emxArray_creal_T *b_y1;
  int b_x[2];
  emxArray_real_T c_x;
  int i3;
  int loop_ub;
  emxInit_creal_T1(&b_y1, 1);
  b_x[0] = x->size[1];
  b_x[1] = 1;
  c_x = *x;
  c_x.size = (int *)&b_x;
  c_x.numDimensions = 1;
  b_eml_fft(&c_x, x->size[1], b_y1);
  i3 = y->size[0] * y->size[1];
  y->size[0] = 1;
  y->size[1] = x->size[1];
  emxEnsureCapacity((emxArray__common *)y, i3, (int)sizeof(creal_T));
  loop_ub = x->size[1];
  for (i3 = 0; i3 < loop_ub; i3++) {
    y->data[i3] = b_y1->data[i3];
  }

  emxFree_creal_T(&b_y1);
}

//
// Arguments    : double varargin_1
//                emxArray_creal_T *y
// Return Type  : void
//
void c_fft(double varargin_1, emxArray_creal_T *y)
{
  emxArray_real_T *costab;
  emxArray_real_T *sintab;
  emxArray_real_T *sintabinv;
  boolean_T useRadix2;
  int iheight;
  int xidx;
  emxArray_creal_T *b_y1;
  emxArray_creal_T *wwc;
  int minNrowsNx;
  int nRowsD2;
  int nRowsD4;
  int ix;
  int i;
  double twid_re;
  double twid_im;
  emxArray_creal_T *fy;
  static const double x[8] = { -0.053574450708941054, 0.020955482562526946,
    0.35186953432761287, -0.56832912170437477, 0.21061726710176826,
    0.070158812089422817, -0.0089123507208401943, -0.022785172947974931 };

  emxArray_creal_T *fv;
  double temp_re;
  double temp_im;
  double fv_re;
  double fv_im;
  int j;
  int ihi;
  double wwc_im;
  double b_fv_re;
  emxInit_real_T(&costab, 2);
  emxInit_real_T(&sintab, 2);
  emxInit_real_T(&sintabinv, 2);
  useRadix2 = (((int)varargin_1 & ((int)varargin_1 - 1)) == 0);
  get_algo_sizes((int)varargin_1, useRadix2, &iheight, &xidx);
  generate_twiddle_tables(xidx, useRadix2, costab, sintab, sintabinv);
  emxInit_creal_T1(&b_y1, 1);
  if (useRadix2) {
    if (8 <= (int)varargin_1) {
      iheight = 8;
    } else {
      iheight = (int)varargin_1;
    }

    nRowsD2 = (int)varargin_1 / 2;
    nRowsD4 = nRowsD2 / 2;
    ix = b_y1->size[0];
    b_y1->size[0] = (int)varargin_1;
    emxEnsureCapacity((emxArray__common *)b_y1, ix, (int)sizeof(creal_T));
    if ((int)varargin_1 > 8) {
      xidx = b_y1->size[0];
      ix = b_y1->size[0];
      b_y1->size[0] = xidx;
      emxEnsureCapacity((emxArray__common *)b_y1, ix, (int)sizeof(creal_T));
      for (ix = 0; ix < xidx; ix++) {
        b_y1->data[ix].re = 0.0;
        b_y1->data[ix].im = 0.0;
      }
    }

    ix = 0;
    minNrowsNx = 0;
    xidx = 0;
    for (i = 1; i < iheight; i++) {
      b_y1->data[xidx].re = x[ix];
      b_y1->data[xidx].im = 0.0;
      xidx = (int)varargin_1;
      useRadix2 = true;
      while (useRadix2) {
        xidx >>= 1;
        minNrowsNx ^= xidx;
        useRadix2 = ((minNrowsNx & xidx) == 0);
      }

      xidx = minNrowsNx;
      ix++;
    }

    b_y1->data[xidx].re = x[ix];
    b_y1->data[xidx].im = 0.0;
    for (i = 0; i <= (int)varargin_1 - 2; i += 2) {
      temp_re = b_y1->data[i + 1].re;
      temp_im = b_y1->data[i + 1].im;
      b_y1->data[i + 1].re = b_y1->data[i].re - b_y1->data[i + 1].re;
      b_y1->data[i + 1].im = b_y1->data[i].im - b_y1->data[i + 1].im;
      b_y1->data[i].re += temp_re;
      b_y1->data[i].im += temp_im;
    }

    xidx = 2;
    minNrowsNx = 4;
    iheight = 1 + ((nRowsD4 - 1) << 2);
    while (nRowsD4 > 0) {
      for (i = 0; i < iheight; i += minNrowsNx) {
        temp_re = b_y1->data[i + xidx].re;
        temp_im = b_y1->data[i + xidx].im;
        b_y1->data[i + xidx].re = b_y1->data[i].re - temp_re;
        b_y1->data[i + xidx].im = b_y1->data[i].im - temp_im;
        b_y1->data[i].re += temp_re;
        b_y1->data[i].im += temp_im;
      }

      ix = 1;
      for (j = nRowsD4; j < nRowsD2; j += nRowsD4) {
        twid_re = costab->data[j];
        twid_im = sintab->data[j];
        i = ix;
        ihi = ix + iheight;
        while (i < ihi) {
          temp_re = twid_re * b_y1->data[i + xidx].re - twid_im * b_y1->data[i +
            xidx].im;
          temp_im = twid_re * b_y1->data[i + xidx].im + twid_im * b_y1->data[i +
            xidx].re;
          b_y1->data[i + xidx].re = b_y1->data[i].re - temp_re;
          b_y1->data[i + xidx].im = b_y1->data[i].im - temp_im;
          b_y1->data[i].re += temp_re;
          b_y1->data[i].im += temp_im;
          i += minNrowsNx;
        }

        ix++;
      }

      nRowsD4 /= 2;
      xidx = minNrowsNx;
      minNrowsNx <<= 1;
      iheight -= xidx;
    }
  } else {
    emxInit_creal_T1(&wwc, 1);
    bluestein_setup((int)varargin_1, wwc);
    if ((int)varargin_1 <= 8) {
      minNrowsNx = (int)varargin_1;
    } else {
      minNrowsNx = 8;
    }

    ix = b_y1->size[0];
    b_y1->size[0] = (int)varargin_1;
    emxEnsureCapacity((emxArray__common *)b_y1, ix, (int)sizeof(creal_T));
    if ((int)varargin_1 > 8) {
      xidx = b_y1->size[0];
      ix = b_y1->size[0];
      b_y1->size[0] = xidx;
      emxEnsureCapacity((emxArray__common *)b_y1, ix, (int)sizeof(creal_T));
      for (ix = 0; ix < xidx; ix++) {
        b_y1->data[ix].re = 0.0;
        b_y1->data[ix].im = 0.0;
      }
    }

    xidx = 0;
    for (ix = 0; ix + 1 <= minNrowsNx; ix++) {
      twid_re = wwc->data[((int)varargin_1 + ix) - 1].re;
      twid_im = wwc->data[((int)varargin_1 + ix) - 1].im;
      b_y1->data[ix].re = twid_re * x[xidx];
      b_y1->data[ix].im = twid_im * -x[xidx];
      xidx++;
    }

    while (minNrowsNx + 1 <= (int)varargin_1) {
      b_y1->data[minNrowsNx].re = 0.0;
      b_y1->data[minNrowsNx].im = 0.0;
      minNrowsNx++;
    }

    emxInit_creal_T1(&fy, 1);
    emxInit_creal_T1(&fv, 1);
    r2br_r2dit_trig_impl(b_y1, iheight, costab, sintab, fy);
    r2br_r2dit_trig(wwc, iheight, costab, sintab, fv);
    ix = fy->size[0];
    emxEnsureCapacity((emxArray__common *)fy, ix, (int)sizeof(creal_T));
    xidx = fy->size[0];
    for (ix = 0; ix < xidx; ix++) {
      twid_re = fy->data[ix].re;
      twid_im = fy->data[ix].im;
      fv_re = fv->data[ix].re;
      fv_im = fv->data[ix].im;
      fy->data[ix].re = twid_re * fv_re - twid_im * fv_im;
      fy->data[ix].im = twid_re * fv_im + twid_im * fv_re;
    }

    b_r2br_r2dit_trig(fy, iheight, costab, sintabinv, fv);
    xidx = 0;
    ix = (int)varargin_1 - 1;
    emxFree_creal_T(&fy);
    while (ix + 1 <= wwc->size[0]) {
      twid_re = wwc->data[ix].re;
      fv_re = fv->data[ix].re;
      twid_im = wwc->data[ix].im;
      fv_im = fv->data[ix].im;
      temp_re = wwc->data[ix].re;
      temp_im = fv->data[ix].im;
      wwc_im = wwc->data[ix].im;
      b_fv_re = fv->data[ix].re;
      b_y1->data[xidx].re = twid_re * fv_re + twid_im * fv_im;
      b_y1->data[xidx].im = temp_re * temp_im - wwc_im * b_fv_re;
      xidx++;
      ix++;
    }

    emxFree_creal_T(&fv);
    emxFree_creal_T(&wwc);
  }

  emxFree_real_T(&sintabinv);
  emxFree_real_T(&sintab);
  emxFree_real_T(&costab);
  ix = y->size[0] * y->size[1];
  y->size[0] = 1;
  y->size[1] = (int)varargin_1;
  emxEnsureCapacity((emxArray__common *)y, ix, (int)sizeof(creal_T));
  xidx = (int)varargin_1;
  for (ix = 0; ix < xidx; ix++) {
    y->data[ix] = b_y1->data[ix];
  }

  emxFree_creal_T(&b_y1);
}

//
// Arguments    : const double x[8]
//                double varargin_1
//                emxArray_creal_T *y
// Return Type  : void
//
void fft(const double x[8], double varargin_1, emxArray_creal_T *y)
{
  emxArray_creal_T *b_y1;
  int i2;
  int loop_ub;
  emxInit_creal_T1(&b_y1, 1);
  eml_fft(x, (int)varargin_1, b_y1);
  i2 = y->size[0] * y->size[1];
  y->size[0] = 1;
  y->size[1] = (int)varargin_1;
  emxEnsureCapacity((emxArray__common *)y, i2, (int)sizeof(creal_T));
  loop_ub = (int)varargin_1;
  for (i2 = 0; i2 < loop_ub; i2++) {
    y->data[i2] = b_y1->data[i2];
  }

  emxFree_creal_T(&b_y1);
}

//
// Arguments    : int nRows
//                boolean_T useRadix2
//                emxArray_real_T *costab
//                emxArray_real_T *sintab
//                emxArray_real_T *sintabinv
// Return Type  : void
//
void generate_twiddle_tables(int nRows, boolean_T useRadix2, emxArray_real_T
  *costab, emxArray_real_T *sintab, emxArray_real_T *sintabinv)
{
  emxArray_real_T *costab1q;
  double e;
  int nRowsD4;
  int nd2;
  int k;
  int n2;
  emxInit_real_T(&costab1q, 2);
  e = 6.2831853071795862 / (double)nRows;
  nRowsD4 = nRows / 2 / 2;
  nd2 = costab1q->size[0] * costab1q->size[1];
  costab1q->size[0] = 1;
  costab1q->size[1] = nRowsD4 + 1;
  emxEnsureCapacity((emxArray__common *)costab1q, nd2, (int)sizeof(double));
  costab1q->data[0] = 1.0;
  nd2 = nRowsD4 / 2;
  for (k = 1; k <= nd2; k++) {
    costab1q->data[k] = std::cos(e * (double)k);
  }

  for (k = nd2 + 1; k < nRowsD4; k++) {
    costab1q->data[k] = std::sin(e * (double)(nRowsD4 - k));
  }

  costab1q->data[nRowsD4] = 0.0;
  if (!useRadix2) {
    nRowsD4 = costab1q->size[1] - 1;
    n2 = (costab1q->size[1] - 1) << 1;
    nd2 = costab->size[0] * costab->size[1];
    costab->size[0] = 1;
    costab->size[1] = n2 + 1;
    emxEnsureCapacity((emxArray__common *)costab, nd2, (int)sizeof(double));
    nd2 = sintab->size[0] * sintab->size[1];
    sintab->size[0] = 1;
    sintab->size[1] = n2 + 1;
    emxEnsureCapacity((emxArray__common *)sintab, nd2, (int)sizeof(double));
    costab->data[0] = 1.0;
    sintab->data[0] = 0.0;
    nd2 = sintabinv->size[0] * sintabinv->size[1];
    sintabinv->size[0] = 1;
    sintabinv->size[1] = n2 + 1;
    emxEnsureCapacity((emxArray__common *)sintabinv, nd2, (int)sizeof(double));
    for (k = 1; k <= nRowsD4; k++) {
      sintabinv->data[k] = costab1q->data[nRowsD4 - k];
    }

    for (k = costab1q->size[1]; k <= n2; k++) {
      sintabinv->data[k] = costab1q->data[k - nRowsD4];
    }

    for (k = 1; k <= nRowsD4; k++) {
      costab->data[k] = costab1q->data[k];
      sintab->data[k] = -costab1q->data[nRowsD4 - k];
    }

    for (k = costab1q->size[1]; k <= n2; k++) {
      costab->data[k] = -costab1q->data[n2 - k];
      sintab->data[k] = -costab1q->data[k - nRowsD4];
    }
  } else {
    nRowsD4 = costab1q->size[1] - 1;
    n2 = (costab1q->size[1] - 1) << 1;
    nd2 = costab->size[0] * costab->size[1];
    costab->size[0] = 1;
    costab->size[1] = n2 + 1;
    emxEnsureCapacity((emxArray__common *)costab, nd2, (int)sizeof(double));
    nd2 = sintab->size[0] * sintab->size[1];
    sintab->size[0] = 1;
    sintab->size[1] = n2 + 1;
    emxEnsureCapacity((emxArray__common *)sintab, nd2, (int)sizeof(double));
    costab->data[0] = 1.0;
    sintab->data[0] = 0.0;
    for (k = 1; k <= nRowsD4; k++) {
      costab->data[k] = costab1q->data[k];
      sintab->data[k] = -costab1q->data[nRowsD4 - k];
    }

    for (k = costab1q->size[1]; k <= n2; k++) {
      costab->data[k] = -costab1q->data[n2 - k];
      sintab->data[k] = -costab1q->data[k - nRowsD4];
    }

    nd2 = sintabinv->size[0] * sintabinv->size[1];
    sintabinv->size[0] = 1;
    sintabinv->size[1] = 0;
    emxEnsureCapacity((emxArray__common *)sintabinv, nd2, (int)sizeof(double));
  }

  emxFree_real_T(&costab1q);
}

//
// File trailer for fft.cpp
//
// [EOF]
//
