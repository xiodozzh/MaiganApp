//
// File: imodwt.cpp
//
// MATLAB Coder version            : 3.2
// C/C++ source code generated on  : 18-Aug-2017 19:21:12
//

// Include Files
#include "rt_nonfinite.h"
#include "detect_ecg.h"
#include "filter_ecg.h"
#include "imodwt.h"
#include "ecg_emxutil.h"
#include "ifft.h"
#include "fft.h"
#include "bluestein_setup.h"
#include "ecg_rtwutil.h"

// Function Definitions

//
// Arguments    : const emxArray_real_T *w
//                emxArray_real_T *xrec
// Return Type  : void
//
void imodwt(const emxArray_real_T *w, emxArray_real_T *xrec)
{
  int ncw;
  int Nrep;
  int ncopies;
  emxArray_real_T *ww;
  int i5;
  int xidx;
  int upfactor;
  int k;
  emxArray_real_T *vout;
  emxArray_real_T *sintab;
  emxArray_real_T *sintabinv;
  boolean_T useRadix2;
  int j;
  emxArray_creal_T *b_y1;
  double ww_data[6];
  emxArray_creal_T *wwc;
  int nRowsD2;
  int nRowsD4;
  double twid_re;
  double twid_im;
  emxArray_creal_T *fy;
  static const double x[8] = { 0.022785172947974931, -0.0089123507208401943,
    -0.070158812089422817, 0.21061726710176826, 0.56832912170437477,
    0.35186953432761287, -0.020955482562526946, -0.053574450708941054 };

  emxArray_creal_T *fv;
  int ihi;
  double temp_re;
  double temp_im;
  double fv_re;
  double fv_im;
  emxArray_creal_T *H;
  double wwc_im;
  double b_fv_re;
  emxArray_creal_T *Vhat;
  emxArray_creal_T *What;
  emxArray_real_T *b_ww;
  ncw = w->size[1];
  Nrep = w->size[1];
  if (w->size[1] < 8) {
    ncopies = 8 - w->size[1];
    Nrep = (9 - w->size[1]) * w->size[1];
  } else {
    ncopies = 0;
  }

  emxInit_real_T(&ww, 2);
  i5 = ww->size[0] * ww->size[1];
  ww->size[0] = 6;
  ww->size[1] = Nrep;
  emxEnsureCapacity((emxArray__common *)ww, i5, (int)sizeof(double));
  xidx = w->size[1];
  for (i5 = 0; i5 < xidx; i5++) {
    for (upfactor = 0; upfactor < 6; upfactor++) {
      ww->data[upfactor + ww->size[0] * i5] = w->data[upfactor + w->size[0] * i5];
    }
  }

  if (ncopies > 0) {
    for (k = 1; k <= ncopies; k++) {
      xidx = k * ncw;
      for (j = 0; j + 1 <= ncw; j++) {
        i5 = xidx + j;
        for (upfactor = 0; upfactor < 6; upfactor++) {
          ww_data[upfactor] = ww->data[upfactor + ww->size[0] * j];
        }

        for (upfactor = 0; upfactor < 6; upfactor++) {
          ww->data[upfactor + ww->size[0] * i5] = ww_data[upfactor];
        }
      }
    }
  }

  emxInit_real_T(&vout, 2);
  emxInit_real_T(&sintab, 2);
  emxInit_real_T(&sintabinv, 2);
  useRadix2 = ((Nrep & (Nrep - 1)) == 0);
  get_algo_sizes(Nrep, useRadix2, &ncopies, &xidx);
  generate_twiddle_tables(xidx, useRadix2, vout, sintab, sintabinv);
  emxInit_creal_T1(&b_y1, 1);
  if (useRadix2) {
    if (8 <= Nrep) {
      upfactor = 8;
    } else {
      upfactor = Nrep;
    }

    nRowsD2 = Nrep / 2;
    nRowsD4 = nRowsD2 / 2;
    i5 = b_y1->size[0];
    b_y1->size[0] = Nrep;
    emxEnsureCapacity((emxArray__common *)b_y1, i5, (int)sizeof(creal_T));
    if (Nrep > 8) {
      xidx = b_y1->size[0];
      i5 = b_y1->size[0];
      b_y1->size[0] = xidx;
      emxEnsureCapacity((emxArray__common *)b_y1, i5, (int)sizeof(creal_T));
      for (i5 = 0; i5 < xidx; i5++) {
        b_y1->data[i5].re = 0.0;
        b_y1->data[i5].im = 0.0;
      }
    }

    ncw = 0;
    ncopies = 0;
    xidx = 0;
    for (k = 1; k < upfactor; k++) {
      b_y1->data[xidx].re = x[ncw];
      b_y1->data[xidx].im = 0.0;
      ihi = Nrep;
      useRadix2 = true;
      while (useRadix2) {
        ihi >>= 1;
        ncopies ^= ihi;
        useRadix2 = ((ncopies & ihi) == 0);
      }

      xidx = ncopies;
      ncw++;
    }

    b_y1->data[xidx].re = x[ncw];
    b_y1->data[xidx].im = 0.0;
    for (k = 0; k <= Nrep - 2; k += 2) {
      temp_re = b_y1->data[k + 1].re;
      temp_im = b_y1->data[k + 1].im;
      b_y1->data[k + 1].re = b_y1->data[k].re - b_y1->data[k + 1].re;
      b_y1->data[k + 1].im = b_y1->data[k].im - b_y1->data[k + 1].im;
      b_y1->data[k].re += temp_re;
      b_y1->data[k].im += temp_im;
    }

    xidx = 2;
    ncw = 4;
    ncopies = 1 + ((nRowsD4 - 1) << 2);
    while (nRowsD4 > 0) {
      for (k = 0; k < ncopies; k += ncw) {
        temp_re = b_y1->data[k + xidx].re;
        temp_im = b_y1->data[k + xidx].im;
        b_y1->data[k + xidx].re = b_y1->data[k].re - temp_re;
        b_y1->data[k + xidx].im = b_y1->data[k].im - temp_im;
        b_y1->data[k].re += temp_re;
        b_y1->data[k].im += temp_im;
      }

      upfactor = 1;
      for (j = nRowsD4; j < nRowsD2; j += nRowsD4) {
        twid_re = vout->data[j];
        twid_im = sintab->data[j];
        k = upfactor;
        ihi = upfactor + ncopies;
        while (k < ihi) {
          temp_re = twid_re * b_y1->data[k + xidx].re - twid_im * b_y1->data[k +
            xidx].im;
          temp_im = twid_re * b_y1->data[k + xidx].im + twid_im * b_y1->data[k +
            xidx].re;
          b_y1->data[k + xidx].re = b_y1->data[k].re - temp_re;
          b_y1->data[k + xidx].im = b_y1->data[k].im - temp_im;
          b_y1->data[k].re += temp_re;
          b_y1->data[k].im += temp_im;
          k += ncw;
        }

        upfactor++;
      }

      nRowsD4 /= 2;
      xidx = ncw;
      ncw <<= 1;
      ncopies -= xidx;
    }
  } else {
    emxInit_creal_T1(&wwc, 1);
    bluestein_setup(Nrep, wwc);
    if (Nrep <= 8) {
      ncw = Nrep;
    } else {
      ncw = 8;
    }

    i5 = b_y1->size[0];
    b_y1->size[0] = Nrep;
    emxEnsureCapacity((emxArray__common *)b_y1, i5, (int)sizeof(creal_T));
    if (Nrep > 8) {
      xidx = b_y1->size[0];
      i5 = b_y1->size[0];
      b_y1->size[0] = xidx;
      emxEnsureCapacity((emxArray__common *)b_y1, i5, (int)sizeof(creal_T));
      for (i5 = 0; i5 < xidx; i5++) {
        b_y1->data[i5].re = 0.0;
        b_y1->data[i5].im = 0.0;
      }
    }

    xidx = 0;
    for (k = 0; k + 1 <= ncw; k++) {
      twid_re = wwc->data[(Nrep + k) - 1].re;
      twid_im = wwc->data[(Nrep + k) - 1].im;
      b_y1->data[k].re = twid_re * x[xidx];
      b_y1->data[k].im = twid_im * -x[xidx];
      xidx++;
    }

    while (ncw + 1 <= Nrep) {
      b_y1->data[ncw].re = 0.0;
      b_y1->data[ncw].im = 0.0;
      ncw++;
    }

    emxInit_creal_T1(&fy, 1);
    emxInit_creal_T1(&fv, 1);
    r2br_r2dit_trig_impl(b_y1, ncopies, vout, sintab, fy);
    r2br_r2dit_trig(wwc, ncopies, vout, sintab, fv);
    i5 = fy->size[0];
    emxEnsureCapacity((emxArray__common *)fy, i5, (int)sizeof(creal_T));
    xidx = fy->size[0];
    for (i5 = 0; i5 < xidx; i5++) {
      twid_re = fy->data[i5].re;
      twid_im = fy->data[i5].im;
      fv_re = fv->data[i5].re;
      fv_im = fv->data[i5].im;
      fy->data[i5].re = twid_re * fv_re - twid_im * fv_im;
      fy->data[i5].im = twid_re * fv_im + twid_im * fv_re;
    }

    b_r2br_r2dit_trig(fy, ncopies, vout, sintabinv, fv);
    xidx = 0;
    k = Nrep - 1;
    emxFree_creal_T(&fy);
    while (k + 1 <= wwc->size[0]) {
      twid_re = wwc->data[k].re;
      fv_re = fv->data[k].re;
      twid_im = wwc->data[k].im;
      fv_im = fv->data[k].im;
      temp_re = wwc->data[k].re;
      temp_im = fv->data[k].im;
      wwc_im = wwc->data[k].im;
      b_fv_re = fv->data[k].re;
      b_y1->data[xidx].re = twid_re * fv_re + twid_im * fv_im;
      b_y1->data[xidx].im = temp_re * temp_im - wwc_im * b_fv_re;
      xidx++;
      k++;
    }

    emxFree_creal_T(&fv);
    emxFree_creal_T(&wwc);
  }

  emxFree_real_T(&sintabinv);
  emxFree_real_T(&sintab);
  emxInit_creal_T(&H, 2);
  c_fft((double)Nrep, H);
  xidx = ww->size[1];
  i5 = vout->size[0] * vout->size[1];
  vout->size[0] = 1;
  vout->size[1] = xidx;
  emxEnsureCapacity((emxArray__common *)vout, i5, (int)sizeof(double));
  for (i5 = 0; i5 < xidx; i5++) {
    vout->data[vout->size[0] * i5] = ww->data[5 + ww->size[0] * i5];
  }

  emxInit_creal_T(&Vhat, 2);
  emxInit_creal_T(&What, 2);
  emxInit_real_T(&b_ww, 2);
  for (ncopies = 4; ncopies >= 0; ncopies += -1) {
    ihi = vout->size[1];
    upfactor = 1 << ncopies;
    b_fft(vout, Vhat);
    xidx = ww->size[1];
    i5 = b_ww->size[0] * b_ww->size[1];
    b_ww->size[0] = 1;
    b_ww->size[1] = xidx;
    emxEnsureCapacity((emxArray__common *)b_ww, i5, (int)sizeof(double));
    for (i5 = 0; i5 < xidx; i5++) {
      b_ww->data[b_ww->size[0] * i5] = ww->data[ncopies + ww->size[0] * i5];
    }

    b_fft(b_ww, What);
    for (k = 0; k + 1 <= ihi; k++) {
      xidx = upfactor * k;
      ncw = div_s32(xidx, vout->size[1]) * ihi;
      xidx -= ncw;
      twid_re = b_y1->data[xidx].re;
      temp_im = -b_y1->data[xidx].im;
      wwc_im = twid_re * Vhat->data[k].re - temp_im * Vhat->data[k].im;
      temp_im = twid_re * Vhat->data[k].im + temp_im * Vhat->data[k].re;
      twid_re = H->data[xidx].re;
      twid_im = -H->data[xidx].im;
      temp_re = twid_re * What->data[k].re - twid_im * What->data[k].im;
      twid_im = twid_re * What->data[k].im + twid_im * What->data[k].re;
      Vhat->data[k].re = wwc_im + temp_re;
      Vhat->data[k].im = temp_im + twid_im;
    }

    ifft(Vhat, What);
    i5 = vout->size[0] * vout->size[1];
    vout->size[0] = 1;
    vout->size[1] = What->size[1];
    emxEnsureCapacity((emxArray__common *)vout, i5, (int)sizeof(double));
    xidx = What->size[0] * What->size[1];
    for (i5 = 0; i5 < xidx; i5++) {
      vout->data[i5] = What->data[i5].re;
    }
  }

  emxFree_real_T(&b_ww);
  emxFree_creal_T(&What);
  emxFree_creal_T(&Vhat);
  emxFree_creal_T(&b_y1);
  emxFree_creal_T(&H);
  emxFree_real_T(&ww);
  xidx = w->size[1];
  i5 = xrec->size[0] * xrec->size[1];
  xrec->size[0] = 1;
  xrec->size[1] = xidx;
  emxEnsureCapacity((emxArray__common *)xrec, i5, (int)sizeof(double));
  for (i5 = 0; i5 < xidx; i5++) {
    xrec->data[xrec->size[0] * i5] = vout->data[i5];
  }

  emxFree_real_T(&vout);
}

//
// File trailer for imodwt.cpp
//
// [EOF]
//
