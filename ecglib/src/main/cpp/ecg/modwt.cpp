//
// File: modwt.cpp
//
// MATLAB Coder version            : 3.2
// C/C++ source code generated on  : 18-Aug-2017 19:21:12
//

// Include Files
#include "rt_nonfinite.h"
#include "detect_ecg.h"
#include "filter_ecg.h"
#include "modwt.h"
#include "ecg_emxutil.h"
#include "imodwt.h"
#include "ifft.h"
#include "fft.h"
#include "ecg_rtwutil.h"

// Function Definitions

//
// Arguments    : const emxArray_real_T *x
//                emxArray_real_T *w
// Return Type  : void
//
void modwt(const emxArray_real_T *x, emxArray_real_T *w)
{
  int n;
  int offset;
  int eint;
  int ncopies;
  int Nrep;
  emxArray_real_T *xx;
  int k;
  emxArray_creal_T *G;
  emxArray_creal_T *H;
  emxArray_creal_T *Vhat;
  static const double Lo[8] = { 0.022785172947974931, -0.0089123507208401943,
    -0.070158812089422817, 0.21061726710176826, 0.56832912170437477,
    0.35186953432761287, -0.020955482562526946, -0.053574450708941054 };

  static const double Hi[8] = { -0.053574450708941054, 0.020955482562526946,
    0.35186953432761287, -0.56832912170437477, 0.21061726710176826,
    0.070158812089422817, -0.0089123507208401943, -0.022785172947974931 };

  emxArray_creal_T *b_Vhat;
  emxArray_creal_T *What;
  int jj;
  double G_re;
  double G_im;
  double Vhat_re;
  double Vhat_im;
  n = x->size[1];
  frexp((double)x->size[1], &offset);
  frexp((double)x->size[1], &eint);
  if (x->size[1] < 8) {
    ncopies = 8 - x->size[1];
    Nrep = (9 - x->size[1]) * x->size[1];
  } else {
    ncopies = 0;
    Nrep = x->size[1];
  }

  emxInit_real_T(&xx, 2);
  eint = xx->size[0] * xx->size[1];
  xx->size[0] = 1;
  xx->size[1] = Nrep;
  emxEnsureCapacity((emxArray__common *)xx, eint, (int)sizeof(double));
  offset = x->size[1];
  for (eint = 0; eint < offset; eint++) {
    xx->data[eint] = x->data[x->size[0] * eint];
  }

  if (ncopies > 0) {
    for (k = 1; k <= ncopies; k++) {
      offset = k * n;
      for (eint = 0; eint + 1 <= n; eint++) {
        xx->data[offset + eint] = xx->data[eint];
      }
    }
  }

  eint = w->size[0] * w->size[1];
  w->size[0] = 6;
  w->size[1] = x->size[1];
  emxEnsureCapacity((emxArray__common *)w, eint, (int)sizeof(double));
  offset = 6 * x->size[1];
  for (eint = 0; eint < offset; eint++) {
    w->data[eint] = 0.0;
  }

  emxInit_creal_T(&G, 2);
  emxInit_creal_T(&H, 2);
  emxInit_creal_T(&Vhat, 2);
  fft(Lo, (double)Nrep, G);
  fft(Hi, (double)Nrep, H);
  b_fft(xx, Vhat);
  emxInit_creal_T(&b_Vhat, 2);
  emxInit_creal_T(&What, 2);
  for (jj = 0; jj < 5; jj++) {
    ncopies = Vhat->size[1];
    Nrep = 1 << jj;
    for (eint = 0; eint < 2; eint++) {
      offset = b_Vhat->size[0] * b_Vhat->size[1];
      b_Vhat->size[eint] = Vhat->size[eint];
      emxEnsureCapacity((emxArray__common *)b_Vhat, offset, (int)sizeof(creal_T));
    }

    for (eint = 0; eint < 2; eint++) {
      offset = What->size[0] * What->size[1];
      What->size[eint] = Vhat->size[eint];
      emxEnsureCapacity((emxArray__common *)What, offset, (int)sizeof(creal_T));
    }

    for (k = 0; k + 1 <= ncopies; k++) {
      offset = Nrep * k;
      offset -= div_s32(offset, Vhat->size[1]) * ncopies;
      G_re = G->data[offset].re;
      G_im = G->data[offset].im;
      Vhat_re = Vhat->data[k].re;
      Vhat_im = Vhat->data[k].im;
      b_Vhat->data[k].re = G_re * Vhat_re - G_im * Vhat_im;
      b_Vhat->data[k].im = G_re * Vhat_im + G_im * Vhat_re;
      G_re = H->data[offset].re;
      G_im = H->data[offset].im;
      Vhat_re = Vhat->data[k].re;
      Vhat_im = Vhat->data[k].im;
      What->data[k].re = G_re * Vhat_re - G_im * Vhat_im;
      What->data[k].im = G_re * Vhat_im + G_im * Vhat_re;
    }

    eint = Vhat->size[0] * Vhat->size[1];
    Vhat->size[0] = 1;
    Vhat->size[1] = b_Vhat->size[1];
    emxEnsureCapacity((emxArray__common *)Vhat, eint, (int)sizeof(creal_T));
    offset = b_Vhat->size[0] * b_Vhat->size[1];
    for (eint = 0; eint < offset; eint++) {
      Vhat->data[eint] = b_Vhat->data[eint];
    }

    ifft(What, b_Vhat);
    eint = xx->size[0] * xx->size[1];
    xx->size[0] = 1;
    xx->size[1] = b_Vhat->size[1];
    emxEnsureCapacity((emxArray__common *)xx, eint, (int)sizeof(double));
    offset = b_Vhat->size[0] * b_Vhat->size[1];
    for (eint = 0; eint < offset; eint++) {
      xx->data[eint] = b_Vhat->data[eint].re;
    }

    for (eint = 0; eint < n; eint++) {
      w->data[jj + w->size[0] * eint] = xx->data[eint];
    }
  }

  emxFree_creal_T(&What);
  emxFree_creal_T(&H);
  emxFree_creal_T(&G);
  ifft(Vhat, b_Vhat);
  eint = xx->size[0] * xx->size[1];
  xx->size[0] = 1;
  xx->size[1] = b_Vhat->size[1];
  emxEnsureCapacity((emxArray__common *)xx, eint, (int)sizeof(double));
  offset = b_Vhat->size[0] * b_Vhat->size[1];
  emxFree_creal_T(&Vhat);
  for (eint = 0; eint < offset; eint++) {
    xx->data[eint] = b_Vhat->data[eint].re;
  }

  emxFree_creal_T(&b_Vhat);
  offset = x->size[1] - 1;
  for (eint = 0; eint <= offset; eint++) {
    w->data[5 + w->size[0] * eint] = xx->data[eint];
  }

  emxFree_real_T(&xx);
}

//
// File trailer for modwt.cpp
//
// [EOF]
//
