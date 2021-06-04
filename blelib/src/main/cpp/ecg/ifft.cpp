//
// File: ifft.cpp
//
// MATLAB Coder version            : 3.2
// C/C++ source code generated on  : 18-Aug-2017 19:21:12
//

// Include Files
#include "rt_nonfinite.h"
#include "detect_ecg.h"
#include "filter_ecg.h"
#include "ifft.h"
#include "ecg_emxutil.h"

// Function Declarations
static void c_eml_fft(const emxArray_creal_T *x, int n, emxArray_creal_T *y);

// Function Definitions

//
// Arguments    : const emxArray_creal_T *x
//                int n
//                emxArray_creal_T *y
// Return Type  : void
//
static void c_eml_fft(const emxArray_creal_T *x, int n, emxArray_creal_T *y)
{
  emxArray_real_T *costab1q;
  boolean_T useRadix2;
  int N2blue;
  int nd2;
  double e;
  int nRowsD4;
  int nInt2;
  int k;
  emxArray_real_T *costab;
  emxArray_real_T *sintab;
  emxArray_real_T *sintabinv;
  emxArray_creal_T *wwc;
  int b_x[1];
  int nInt2m1;
  emxArray_creal_T c_x;
  int b_y;
  double denom_im;
  emxArray_creal_T *fy;
  double wwc_re;
  emxArray_creal_T *fv;
  double fv_im;
  double wwc_im;
  double fv_re;
  double b_fv_re;
  double b_fv_im;
  emxInit_real_T(&costab1q, 2);
  useRadix2 = ((n & (n - 1)) == 0);
  get_algo_sizes(n, useRadix2, &N2blue, &nd2);
  e = 6.2831853071795862 / (double)nd2;
  nRowsD4 = nd2 / 2 / 2;
  nInt2 = costab1q->size[0] * costab1q->size[1];
  costab1q->size[0] = 1;
  costab1q->size[1] = nRowsD4 + 1;
  emxEnsureCapacity((emxArray__common *)costab1q, nInt2, (int)sizeof(double));
  costab1q->data[0] = 1.0;
  nd2 = nRowsD4 / 2;
  for (k = 1; k <= nd2; k++) {
    costab1q->data[k] = std::cos(e * (double)k);
  }

  for (k = nd2 + 1; k < nRowsD4; k++) {
    costab1q->data[k] = std::sin(e * (double)(nRowsD4 - k));
  }

  costab1q->data[nRowsD4] = 0.0;
  emxInit_real_T(&costab, 2);
  emxInit_real_T(&sintab, 2);
  emxInit_real_T(&sintabinv, 2);
  if (!useRadix2) {
    nd2 = costab1q->size[1] - 1;
    nRowsD4 = (costab1q->size[1] - 1) << 1;
    nInt2 = costab->size[0] * costab->size[1];
    costab->size[0] = 1;
    costab->size[1] = nRowsD4 + 1;
    emxEnsureCapacity((emxArray__common *)costab, nInt2, (int)sizeof(double));
    nInt2 = sintab->size[0] * sintab->size[1];
    sintab->size[0] = 1;
    sintab->size[1] = nRowsD4 + 1;
    emxEnsureCapacity((emxArray__common *)sintab, nInt2, (int)sizeof(double));
    costab->data[0] = 1.0;
    sintab->data[0] = 0.0;
    nInt2 = sintabinv->size[0] * sintabinv->size[1];
    sintabinv->size[0] = 1;
    sintabinv->size[1] = nRowsD4 + 1;
    emxEnsureCapacity((emxArray__common *)sintabinv, nInt2, (int)sizeof(double));
    for (k = 1; k <= nd2; k++) {
      sintabinv->data[k] = costab1q->data[nd2 - k];
    }

    for (k = costab1q->size[1]; k <= nRowsD4; k++) {
      sintabinv->data[k] = costab1q->data[k - nd2];
    }

    for (k = 1; k <= nd2; k++) {
      costab->data[k] = costab1q->data[k];
      sintab->data[k] = -costab1q->data[nd2 - k];
    }

    for (k = costab1q->size[1]; k <= nRowsD4; k++) {
      costab->data[k] = -costab1q->data[nRowsD4 - k];
      sintab->data[k] = -costab1q->data[k - nd2];
    }
  } else {
    nd2 = costab1q->size[1] - 1;
    nRowsD4 = (costab1q->size[1] - 1) << 1;
    nInt2 = costab->size[0] * costab->size[1];
    costab->size[0] = 1;
    costab->size[1] = nRowsD4 + 1;
    emxEnsureCapacity((emxArray__common *)costab, nInt2, (int)sizeof(double));
    nInt2 = sintab->size[0] * sintab->size[1];
    sintab->size[0] = 1;
    sintab->size[1] = nRowsD4 + 1;
    emxEnsureCapacity((emxArray__common *)sintab, nInt2, (int)sizeof(double));
    costab->data[0] = 1.0;
    sintab->data[0] = 0.0;
    for (k = 1; k <= nd2; k++) {
      costab->data[k] = costab1q->data[k];
      sintab->data[k] = costab1q->data[nd2 - k];
    }

    for (k = costab1q->size[1]; k <= nRowsD4; k++) {
      costab->data[k] = -costab1q->data[nRowsD4 - k];
      sintab->data[k] = costab1q->data[k - nd2];
    }

    nInt2 = sintabinv->size[0] * sintabinv->size[1];
    sintabinv->size[0] = 1;
    sintabinv->size[1] = 0;
    emxEnsureCapacity((emxArray__common *)sintabinv, nInt2, (int)sizeof(double));
  }

  emxFree_real_T(&costab1q);
  if (useRadix2) {
    b_x[0] = x->size[0];
    c_x = *x;
    c_x.size = (int *)&b_x;
    c_x.numDimensions = 1;
    b_r2br_r2dit_trig(&c_x, n, costab, sintab, y);
  } else {
    emxInit_creal_T1(&wwc, 1);
    nInt2m1 = (n + n) - 1;
    nInt2 = wwc->size[0];
    wwc->size[0] = nInt2m1;
    emxEnsureCapacity((emxArray__common *)wwc, nInt2, (int)sizeof(creal_T));
    nd2 = n;
    nRowsD4 = 0;
    wwc->data[n - 1].re = 1.0;
    wwc->data[n - 1].im = 0.0;
    nInt2 = n << 1;
    for (k = 1; k < n; k++) {
      b_y = (k << 1) - 1;
      if (nInt2 - nRowsD4 <= b_y) {
        nRowsD4 += b_y - nInt2;
      } else {
        nRowsD4 += b_y;
      }

      denom_im = 3.1415926535897931 * (double)nRowsD4 / (double)n;
      if (denom_im == 0.0) {
        e = 1.0;
        denom_im = 0.0;
      } else {
        e = std::cos(denom_im);
        denom_im = std::sin(denom_im);
      }

      wwc->data[nd2 - 2].re = e;
      wwc->data[nd2 - 2].im = -denom_im;
      nd2--;
    }

    nd2 = 0;
    for (k = nInt2m1 - 1; k >= n; k--) {
      wwc->data[k] = wwc->data[nd2];
      nd2++;
    }

    nd2 = x->size[0];
    if (n <= nd2) {
      nRowsD4 = n;
    } else {
      nRowsD4 = x->size[0];
    }

    nInt2 = y->size[0];
    y->size[0] = n;
    emxEnsureCapacity((emxArray__common *)y, nInt2, (int)sizeof(creal_T));
    nd2 = x->size[0];
    if (n > nd2) {
      b_y = y->size[0];
      nInt2 = y->size[0];
      y->size[0] = b_y;
      emxEnsureCapacity((emxArray__common *)y, nInt2, (int)sizeof(creal_T));
      for (nInt2 = 0; nInt2 < b_y; nInt2++) {
        y->data[nInt2].re = 0.0;
        y->data[nInt2].im = 0.0;
      }
    }

    nd2 = 0;
    for (k = 0; k + 1 <= nRowsD4; k++) {
      e = wwc->data[(n + k) - 1].re;
      denom_im = wwc->data[(n + k) - 1].im;
      wwc_re = x->data[nd2].re;
      fv_im = x->data[nd2].im;
      wwc_im = x->data[nd2].im;
      fv_re = x->data[nd2].re;
      y->data[k].re = e * wwc_re + denom_im * fv_im;
      y->data[k].im = e * wwc_im - denom_im * fv_re;
      nd2++;
    }

    while (nRowsD4 + 1 <= n) {
      y->data[nRowsD4].re = 0.0;
      y->data[nRowsD4].im = 0.0;
      nRowsD4++;
    }

    emxInit_creal_T1(&fy, 1);
    emxInit_creal_T1(&fv, 1);
    r2br_r2dit_trig_impl(y, N2blue, costab, sintab, fy);
    r2br_r2dit_trig(wwc, N2blue, costab, sintab, fv);
    nInt2 = fy->size[0];
    emxEnsureCapacity((emxArray__common *)fy, nInt2, (int)sizeof(creal_T));
    nd2 = fy->size[0];
    for (nInt2 = 0; nInt2 < nd2; nInt2++) {
      e = fy->data[nInt2].re;
      denom_im = fy->data[nInt2].im;
      b_fv_re = fv->data[nInt2].re;
      b_fv_im = fv->data[nInt2].im;
      fy->data[nInt2].re = e * b_fv_re - denom_im * b_fv_im;
      fy->data[nInt2].im = e * b_fv_im + denom_im * b_fv_re;
    }

    b_r2br_r2dit_trig(fy, N2blue, costab, sintabinv, fv);
    nd2 = 0;
    k = n - 1;
    emxFree_creal_T(&fy);
    while (k + 1 <= wwc->size[0]) {
      e = wwc->data[k].re;
      b_fv_re = fv->data[k].re;
      denom_im = wwc->data[k].im;
      b_fv_im = fv->data[k].im;
      wwc_re = wwc->data[k].re;
      fv_im = fv->data[k].im;
      wwc_im = wwc->data[k].im;
      fv_re = fv->data[k].re;
      y->data[nd2].re = e * b_fv_re + denom_im * b_fv_im;
      y->data[nd2].im = wwc_re * fv_im - wwc_im * fv_re;
      e = wwc->data[k].re;
      b_fv_re = fv->data[k].re;
      denom_im = wwc->data[k].im;
      b_fv_im = fv->data[k].im;
      wwc_re = wwc->data[k].re;
      fv_im = fv->data[k].im;
      wwc_im = wwc->data[k].im;
      fv_re = fv->data[k].re;
      y->data[nd2].re = e * b_fv_re + denom_im * b_fv_im;
      y->data[nd2].im = wwc_re * fv_im - wwc_im * fv_re;
      e = y->data[nd2].re;
      denom_im = y->data[nd2].im;
      if (denom_im == 0.0) {
        y->data[nd2].re = e / (double)n;
        y->data[nd2].im = 0.0;
      } else if (e == 0.0) {
        y->data[nd2].re = 0.0;
        y->data[nd2].im = denom_im / (double)n;
      } else {
        y->data[nd2].re = e / (double)n;
        y->data[nd2].im = denom_im / (double)n;
      }

      nd2++;
      k++;
    }

    emxFree_creal_T(&fv);
    emxFree_creal_T(&wwc);
  }

  emxFree_real_T(&sintabinv);
  emxFree_real_T(&sintab);
  emxFree_real_T(&costab);
}

//
// Arguments    : const emxArray_creal_T *x
//                int n1_unsigned
//                const emxArray_real_T *costab
//                const emxArray_real_T *sintab
//                emxArray_creal_T *y
// Return Type  : void
//
void b_r2br_r2dit_trig(const emxArray_creal_T *x, int n1_unsigned, const
  emxArray_real_T *costab, const emxArray_real_T *sintab, emxArray_creal_T *y)
{
  int j;
  int nRowsD2;
  int nRowsD4;
  int iDelta2;
  int iy;
  int ix;
  int ju;
  int i;
  boolean_T tst;
  double temp_re;
  double temp_im;
  double r;
  double twid_im;
  int ihi;
  if (x->size[0] <= n1_unsigned) {
    j = x->size[0];
  } else {
    j = n1_unsigned;
  }

  nRowsD2 = n1_unsigned / 2;
  nRowsD4 = nRowsD2 / 2;
  iDelta2 = y->size[0];
  y->size[0] = n1_unsigned;
  emxEnsureCapacity((emxArray__common *)y, iDelta2, (int)sizeof(creal_T));
  if (n1_unsigned > x->size[0]) {
    iy = y->size[0];
    iDelta2 = y->size[0];
    y->size[0] = iy;
    emxEnsureCapacity((emxArray__common *)y, iDelta2, (int)sizeof(creal_T));
    for (iDelta2 = 0; iDelta2 < iy; iDelta2++) {
      y->data[iDelta2].re = 0.0;
      y->data[iDelta2].im = 0.0;
    }
  }

  ix = 0;
  ju = 0;
  iy = 0;
  for (i = 1; i < j; i++) {
    y->data[iy] = x->data[ix];
    iDelta2 = n1_unsigned;
    tst = true;
    while (tst) {
      iDelta2 >>= 1;
      ju ^= iDelta2;
      tst = ((ju & iDelta2) == 0);
    }

    iy = ju;
    ix++;
  }

  y->data[iy] = x->data[ix];
  if (n1_unsigned > 1) {
    for (i = 0; i <= n1_unsigned - 2; i += 2) {
      temp_re = y->data[i + 1].re;
      temp_im = y->data[i + 1].im;
      y->data[i + 1].re = y->data[i].re - y->data[i + 1].re;
      y->data[i + 1].im = y->data[i].im - y->data[i + 1].im;
      y->data[i].re += temp_re;
      y->data[i].im += temp_im;
    }
  }

  iy = 2;
  iDelta2 = 4;
  ix = 1 + ((nRowsD4 - 1) << 2);
  while (nRowsD4 > 0) {
    for (i = 0; i < ix; i += iDelta2) {
      temp_re = y->data[i + iy].re;
      temp_im = y->data[i + iy].im;
      y->data[i + iy].re = y->data[i].re - temp_re;
      y->data[i + iy].im = y->data[i].im - temp_im;
      y->data[i].re += temp_re;
      y->data[i].im += temp_im;
    }

    ju = 1;
    for (j = nRowsD4; j < nRowsD2; j += nRowsD4) {
      r = costab->data[j];
      twid_im = sintab->data[j];
      i = ju;
      ihi = ju + ix;
      while (i < ihi) {
        temp_re = r * y->data[i + iy].re - twid_im * y->data[i + iy].im;
        temp_im = r * y->data[i + iy].im + twid_im * y->data[i + iy].re;
        y->data[i + iy].re = y->data[i].re - temp_re;
        y->data[i + iy].im = y->data[i].im - temp_im;
        y->data[i].re += temp_re;
        y->data[i].im += temp_im;
        i += iDelta2;
      }

      ju++;
    }

    nRowsD4 /= 2;
    iy = iDelta2;
    iDelta2 <<= 1;
    ix -= iy;
  }

  if (y->size[0] > 1) {
    r = 1.0 / (double)y->size[0];
    iDelta2 = y->size[0];
    emxEnsureCapacity((emxArray__common *)y, iDelta2, (int)sizeof(creal_T));
    iy = y->size[0];
    for (iDelta2 = 0; iDelta2 < iy; iDelta2++) {
      y->data[iDelta2].re *= r;
      y->data[iDelta2].im *= r;
    }
  }
}

//
// Arguments    : int n1
//                boolean_T useRadix2
//                int *N2blue
//                int *nRows
// Return Type  : void
//
void get_algo_sizes(int n1, boolean_T useRadix2, int *N2blue, int *nRows)
{
  int nn1m1;
  int pmax;
  int pmin;
  boolean_T exitg1;
  int p;
  int pow2p;
  *N2blue = 1;
  if (useRadix2) {
    *nRows = n1;
  } else {
    nn1m1 = (n1 + n1) - 1;
    pmax = 31;
    pmin = 0;
    exitg1 = false;
    while ((!exitg1) && (pmax - pmin > 1)) {
      p = (pmin + pmax) >> 1;
      pow2p = 1 << p;
      if (pow2p == nn1m1) {
        pmax = p;
        exitg1 = true;
      } else if (pow2p > nn1m1) {
        pmax = p;
      } else {
        pmin = p;
      }
    }

    *N2blue = 1 << pmax;
    *nRows = *N2blue;
  }
}

//
// Arguments    : const emxArray_creal_T *x
//                emxArray_creal_T *y
// Return Type  : void
//
void ifft(const emxArray_creal_T *x, emxArray_creal_T *y)
{
  emxArray_creal_T *b_y1;
  int b_x[2];
  emxArray_creal_T c_x;
  int i4;
  int loop_ub;
  emxInit_creal_T1(&b_y1, 1);
  b_x[0] = x->size[1];
  b_x[1] = 1;
  c_x = *x;
  c_x.size = (int *)&b_x;
  c_x.numDimensions = 1;
  c_eml_fft(&c_x, x->size[1], b_y1);
  i4 = y->size[0] * y->size[1];
  y->size[0] = 1;
  y->size[1] = x->size[1];
  emxEnsureCapacity((emxArray__common *)y, i4, (int)sizeof(creal_T));
  loop_ub = x->size[1];
  for (i4 = 0; i4 < loop_ub; i4++) {
    y->data[i4] = b_y1->data[i4];
  }

  emxFree_creal_T(&b_y1);
}

//
// Arguments    : const emxArray_creal_T *x
//                int n1_unsigned
//                const emxArray_real_T *costab
//                const emxArray_real_T *sintab
//                emxArray_creal_T *y
// Return Type  : void
//
void r2br_r2dit_trig(const emxArray_creal_T *x, int n1_unsigned, const
                     emxArray_real_T *costab, const emxArray_real_T *sintab,
                     emxArray_creal_T *y)
{
  int j;
  int nRowsD2;
  int nRowsD4;
  int iy;
  int iDelta;
  int ix;
  int ju;
  int i;
  boolean_T tst;
  double temp_re;
  double temp_im;
  double twid_re;
  double twid_im;
  int ihi;
  if (x->size[0] <= n1_unsigned) {
    j = x->size[0];
  } else {
    j = n1_unsigned;
  }

  nRowsD2 = n1_unsigned / 2;
  nRowsD4 = nRowsD2 / 2;
  iy = y->size[0];
  y->size[0] = n1_unsigned;
  emxEnsureCapacity((emxArray__common *)y, iy, (int)sizeof(creal_T));
  if (n1_unsigned > x->size[0]) {
    iDelta = y->size[0];
    iy = y->size[0];
    y->size[0] = iDelta;
    emxEnsureCapacity((emxArray__common *)y, iy, (int)sizeof(creal_T));
    for (iy = 0; iy < iDelta; iy++) {
      y->data[iy].re = 0.0;
      y->data[iy].im = 0.0;
    }
  }

  ix = 0;
  ju = 0;
  iy = 0;
  for (i = 1; i < j; i++) {
    y->data[iy] = x->data[ix];
    iDelta = n1_unsigned;
    tst = true;
    while (tst) {
      iDelta >>= 1;
      ju ^= iDelta;
      tst = ((ju & iDelta) == 0);
    }

    iy = ju;
    ix++;
  }

  y->data[iy] = x->data[ix];
  if (n1_unsigned > 1) {
    for (i = 0; i <= n1_unsigned - 2; i += 2) {
      temp_re = y->data[i + 1].re;
      temp_im = y->data[i + 1].im;
      y->data[i + 1].re = y->data[i].re - y->data[i + 1].re;
      y->data[i + 1].im = y->data[i].im - y->data[i + 1].im;
      y->data[i].re += temp_re;
      y->data[i].im += temp_im;
    }
  }

  iDelta = 2;
  iy = 4;
  ix = 1 + ((nRowsD4 - 1) << 2);
  while (nRowsD4 > 0) {
    for (i = 0; i < ix; i += iy) {
      temp_re = y->data[i + iDelta].re;
      temp_im = y->data[i + iDelta].im;
      y->data[i + iDelta].re = y->data[i].re - temp_re;
      y->data[i + iDelta].im = y->data[i].im - temp_im;
      y->data[i].re += temp_re;
      y->data[i].im += temp_im;
    }

    ju = 1;
    for (j = nRowsD4; j < nRowsD2; j += nRowsD4) {
      twid_re = costab->data[j];
      twid_im = sintab->data[j];
      i = ju;
      ihi = ju + ix;
      while (i < ihi) {
        temp_re = twid_re * y->data[i + iDelta].re - twid_im * y->data[i +
          iDelta].im;
        temp_im = twid_re * y->data[i + iDelta].im + twid_im * y->data[i +
          iDelta].re;
        y->data[i + iDelta].re = y->data[i].re - temp_re;
        y->data[i + iDelta].im = y->data[i].im - temp_im;
        y->data[i].re += temp_re;
        y->data[i].im += temp_im;
        i += iy;
      }

      ju++;
    }

    nRowsD4 /= 2;
    iDelta = iy;
    iy <<= 1;
    ix -= iDelta;
  }
}

//
// Arguments    : const emxArray_creal_T *x
//                int unsigned_nRows
//                const emxArray_real_T *costab
//                const emxArray_real_T *sintab
//                emxArray_creal_T *y
// Return Type  : void
//
void r2br_r2dit_trig_impl(const emxArray_creal_T *x, int unsigned_nRows, const
  emxArray_real_T *costab, const emxArray_real_T *sintab, emxArray_creal_T *y)
{
  int j;
  int nRowsD2;
  int nRowsD4;
  int iy;
  int iDelta;
  int ix;
  int ju;
  int i;
  boolean_T tst;
  double temp_re;
  double temp_im;
  double twid_re;
  double twid_im;
  int ihi;
  if (x->size[0] <= unsigned_nRows) {
    j = x->size[0];
  } else {
    j = unsigned_nRows;
  }

  nRowsD2 = unsigned_nRows / 2;
  nRowsD4 = nRowsD2 / 2;
  iy = y->size[0];
  y->size[0] = unsigned_nRows;
  emxEnsureCapacity((emxArray__common *)y, iy, (int)sizeof(creal_T));
  iy = x->size[0];
  if (unsigned_nRows > iy) {
    iDelta = y->size[0];
    iy = y->size[0];
    y->size[0] = iDelta;
    emxEnsureCapacity((emxArray__common *)y, iy, (int)sizeof(creal_T));
    for (iy = 0; iy < iDelta; iy++) {
      y->data[iy].re = 0.0;
      y->data[iy].im = 0.0;
    }
  }

  ix = 0;
  ju = 0;
  iy = 0;
  for (i = 1; i < j; i++) {
    y->data[iy] = x->data[ix];
    iDelta = unsigned_nRows;
    tst = true;
    while (tst) {
      iDelta >>= 1;
      ju ^= iDelta;
      tst = ((ju & iDelta) == 0);
    }

    iy = ju;
    ix++;
  }

  y->data[iy] = x->data[ix];
  if (unsigned_nRows > 1) {
    for (i = 0; i <= unsigned_nRows - 2; i += 2) {
      temp_re = y->data[i + 1].re;
      temp_im = y->data[i + 1].im;
      y->data[i + 1].re = y->data[i].re - y->data[i + 1].re;
      y->data[i + 1].im = y->data[i].im - y->data[i + 1].im;
      y->data[i].re += temp_re;
      y->data[i].im += temp_im;
    }
  }

  iDelta = 2;
  iy = 4;
  ix = 1 + ((nRowsD4 - 1) << 2);
  while (nRowsD4 > 0) {
    for (i = 0; i < ix; i += iy) {
      temp_re = y->data[i + iDelta].re;
      temp_im = y->data[i + iDelta].im;
      y->data[i + iDelta].re = y->data[i].re - temp_re;
      y->data[i + iDelta].im = y->data[i].im - temp_im;
      y->data[i].re += temp_re;
      y->data[i].im += temp_im;
    }

    ju = 1;
    for (j = nRowsD4; j < nRowsD2; j += nRowsD4) {
      twid_re = costab->data[j];
      twid_im = sintab->data[j];
      i = ju;
      ihi = ju + ix;
      while (i < ihi) {
        temp_re = twid_re * y->data[i + iDelta].re - twid_im * y->data[i +
          iDelta].im;
        temp_im = twid_re * y->data[i + iDelta].im + twid_im * y->data[i +
          iDelta].re;
        y->data[i + iDelta].re = y->data[i].re - temp_re;
        y->data[i + iDelta].im = y->data[i].im - temp_im;
        y->data[i].re += temp_re;
        y->data[i].im += temp_im;
        i += iy;
      }

      ju++;
    }

    nRowsD4 /= 2;
    iDelta = iy;
    iy <<= 1;
    ix -= iDelta;
  }
}

//
// File trailer for ifft.cpp
//
// [EOF]
//
