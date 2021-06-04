//
// File: findpeaks.cpp
//
// MATLAB Coder version            : 3.2
// C/C++ source code generated on  : 18-Aug-2017 19:21:12
//

// Include Files
#include "rt_nonfinite.h"
#include "detect_ecg.h"
#include "filter_ecg.h"
#include "findpeaks.h"
#include "ecg_emxutil.h"
#include "sort1.h"
#include "eml_setop.h"
#include "diff.h"

// Function Declarations
static void findLocalMaxima(emxArray_real_T *yTemp, emxArray_real_T *iPk,
  emxArray_real_T *iInflect);

// Function Definitions

//
// Arguments    : emxArray_real_T *yTemp
//                emxArray_real_T *iPk
//                emxArray_real_T *iInflect
// Return Type  : void
//
static void findLocalMaxima(emxArray_real_T *yTemp, emxArray_real_T *iPk,
  emxArray_real_T *iInflect)
{
  emxArray_real_T *r1;
  int i7;
  int loop_ub;
  emxArray_real_T *y;
  int idx;
  emxArray_real_T *iTemp;
  emxArray_boolean_T *yFinite;
  emxArray_boolean_T *x;
  emxArray_int32_T *ii;
  int nx;
  boolean_T exitg3;
  boolean_T guard3 = false;
  emxArray_int32_T *r2;
  emxArray_real_T *b_iTemp;
  emxArray_real_T *b_yTemp;
  emxArray_real_T *s;
  emxArray_real_T *r3;
  double b_x;
  boolean_T exitg2;
  boolean_T guard2 = false;
  emxArray_int32_T *b_ii;
  boolean_T exitg1;
  boolean_T guard1 = false;
  emxInit_real_T1(&r1, 1);
  i7 = r1->size[0];
  r1->size[0] = 2 + yTemp->size[0];
  emxEnsureCapacity((emxArray__common *)r1, i7, (int)sizeof(double));
  r1->data[0] = rtNaN;
  loop_ub = yTemp->size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    r1->data[i7 + 1] = yTemp->data[i7];
  }

  r1->data[1 + yTemp->size[0]] = rtNaN;
  i7 = yTemp->size[0];
  yTemp->size[0] = r1->size[0];
  emxEnsureCapacity((emxArray__common *)yTemp, i7, (int)sizeof(double));
  loop_ub = r1->size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    yTemp->data[i7] = r1->data[i7];
  }

  emxFree_real_T(&r1);
  emxInit_real_T(&y, 2);
  i7 = yTemp->size[0];
  idx = y->size[0] * y->size[1];
  y->size[0] = 1;
  y->size[1] = (int)((double)i7 - 1.0) + 1;
  emxEnsureCapacity((emxArray__common *)y, idx, (int)sizeof(double));
  loop_ub = (int)((double)i7 - 1.0);
  for (i7 = 0; i7 <= loop_ub; i7++) {
    y->data[y->size[0] * i7] = 1.0 + (double)i7;
  }

  emxInit_real_T1(&iTemp, 1);
  i7 = iTemp->size[0];
  iTemp->size[0] = y->size[1];
  emxEnsureCapacity((emxArray__common *)iTemp, i7, (int)sizeof(double));
  loop_ub = y->size[1];
  for (i7 = 0; i7 < loop_ub; i7++) {
    iTemp->data[i7] = y->data[y->size[0] * i7];
  }

  emxFree_real_T(&y);
  emxInit_boolean_T(&yFinite, 1);
  i7 = yFinite->size[0];
  yFinite->size[0] = yTemp->size[0];
  emxEnsureCapacity((emxArray__common *)yFinite, i7, (int)sizeof(boolean_T));
  loop_ub = yTemp->size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    yFinite->data[i7] = rtIsNaN(yTemp->data[i7]);
  }

  i7 = yFinite->size[0];
  emxEnsureCapacity((emxArray__common *)yFinite, i7, (int)sizeof(boolean_T));
  loop_ub = yFinite->size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    yFinite->data[i7] = !yFinite->data[i7];
  }

  emxInit_boolean_T(&x, 1);
  loop_ub = yTemp->size[0] - 2;
  i7 = x->size[0];
  x->size[0] = loop_ub + 1;
  emxEnsureCapacity((emxArray__common *)x, i7, (int)sizeof(boolean_T));
  for (i7 = 0; i7 <= loop_ub; i7++) {
    x->data[i7] = ((yTemp->data[i7] != yTemp->data[1 + i7]) && (yFinite->data[i7]
      || yFinite->data[1 + i7]));
  }

  emxFree_boolean_T(&yFinite);
  emxInit_int32_T(&ii, 1);
  nx = x->size[0];
  idx = 0;
  i7 = ii->size[0];
  ii->size[0] = x->size[0];
  emxEnsureCapacity((emxArray__common *)ii, i7, (int)sizeof(int));
  loop_ub = 1;
  exitg3 = false;
  while ((!exitg3) && (loop_ub <= nx)) {
    guard3 = false;
    if (x->data[loop_ub - 1]) {
      idx++;
      ii->data[idx - 1] = loop_ub;
      if (idx >= nx) {
        exitg3 = true;
      } else {
        guard3 = true;
      }
    } else {
      guard3 = true;
    }

    if (guard3) {
      loop_ub++;
    }
  }

  if (x->size[0] == 1) {
    if (idx == 0) {
      i7 = ii->size[0];
      ii->size[0] = 0;
      emxEnsureCapacity((emxArray__common *)ii, i7, (int)sizeof(int));
    }
  } else {
    i7 = ii->size[0];
    if (1 > idx) {
      ii->size[0] = 0;
    } else {
      ii->size[0] = idx;
    }

    emxEnsureCapacity((emxArray__common *)ii, i7, (int)sizeof(int));
  }

  emxInit_int32_T(&r2, 1);
  i7 = r2->size[0];
  r2->size[0] = 1 + ii->size[0];
  emxEnsureCapacity((emxArray__common *)r2, i7, (int)sizeof(int));
  r2->data[0] = 1;
  loop_ub = ii->size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    r2->data[i7 + 1] = ii->data[i7] + 1;
  }

  emxInit_real_T1(&b_iTemp, 1);
  i7 = b_iTemp->size[0];
  b_iTemp->size[0] = r2->size[0];
  emxEnsureCapacity((emxArray__common *)b_iTemp, i7, (int)sizeof(double));
  loop_ub = r2->size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    b_iTemp->data[i7] = iTemp->data[r2->data[i7] - 1];
  }

  emxFree_int32_T(&r2);
  i7 = iTemp->size[0];
  iTemp->size[0] = b_iTemp->size[0];
  emxEnsureCapacity((emxArray__common *)iTemp, i7, (int)sizeof(double));
  loop_ub = b_iTemp->size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    iTemp->data[i7] = b_iTemp->data[i7];
  }

  emxFree_real_T(&b_iTemp);
  emxInit_real_T1(&b_yTemp, 1);
  i7 = b_yTemp->size[0];
  b_yTemp->size[0] = iTemp->size[0];
  emxEnsureCapacity((emxArray__common *)b_yTemp, i7, (int)sizeof(double));
  loop_ub = iTemp->size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    b_yTemp->data[i7] = yTemp->data[(int)iTemp->data[i7] - 1];
  }

  emxInit_real_T1(&s, 1);
  diff(b_yTemp, s);
  nx = s->size[0];
  loop_ub = 0;
  emxFree_real_T(&b_yTemp);
  while (loop_ub + 1 <= nx) {
    if (s->data[loop_ub] < 0.0) {
      b_x = -1.0;
    } else if (s->data[loop_ub] > 0.0) {
      b_x = 1.0;
    } else if (s->data[loop_ub] == 0.0) {
      b_x = 0.0;
    } else {
      b_x = s->data[loop_ub];
    }

    s->data[loop_ub] = b_x;
    loop_ub++;
  }

  emxInit_real_T1(&r3, 1);
  diff(s, r3);
  i7 = x->size[0];
  x->size[0] = r3->size[0];
  emxEnsureCapacity((emxArray__common *)x, i7, (int)sizeof(boolean_T));
  loop_ub = r3->size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    x->data[i7] = (r3->data[i7] < 0.0);
  }

  emxFree_real_T(&r3);
  nx = x->size[0];
  idx = 0;
  i7 = ii->size[0];
  ii->size[0] = x->size[0];
  emxEnsureCapacity((emxArray__common *)ii, i7, (int)sizeof(int));
  loop_ub = 1;
  exitg2 = false;
  while ((!exitg2) && (loop_ub <= nx)) {
    guard2 = false;
    if (x->data[loop_ub - 1]) {
      idx++;
      ii->data[idx - 1] = loop_ub;
      if (idx >= nx) {
        exitg2 = true;
      } else {
        guard2 = true;
      }
    } else {
      guard2 = true;
    }

    if (guard2) {
      loop_ub++;
    }
  }

  if (x->size[0] == 1) {
    if (idx == 0) {
      i7 = ii->size[0];
      ii->size[0] = 0;
      emxEnsureCapacity((emxArray__common *)ii, i7, (int)sizeof(int));
    }
  } else {
    i7 = ii->size[0];
    if (1 > idx) {
      ii->size[0] = 0;
    } else {
      ii->size[0] = idx;
    }

    emxEnsureCapacity((emxArray__common *)ii, i7, (int)sizeof(int));
  }

  if (1 > s->size[0] - 1) {
    loop_ub = 0;
  } else {
    loop_ub = s->size[0] - 1;
  }

  i7 = !(2 > s->size[0]);
  idx = x->size[0];
  x->size[0] = loop_ub;
  emxEnsureCapacity((emxArray__common *)x, idx, (int)sizeof(boolean_T));
  for (idx = 0; idx < loop_ub; idx++) {
    x->data[idx] = (s->data[idx] != s->data[i7 + idx]);
  }

  emxFree_real_T(&s);
  emxInit_int32_T(&b_ii, 1);
  nx = x->size[0];
  idx = 0;
  i7 = b_ii->size[0];
  b_ii->size[0] = x->size[0];
  emxEnsureCapacity((emxArray__common *)b_ii, i7, (int)sizeof(int));
  loop_ub = 1;
  exitg1 = false;
  while ((!exitg1) && (loop_ub <= nx)) {
    guard1 = false;
    if (x->data[loop_ub - 1]) {
      idx++;
      b_ii->data[idx - 1] = loop_ub;
      if (idx >= nx) {
        exitg1 = true;
      } else {
        guard1 = true;
      }
    } else {
      guard1 = true;
    }

    if (guard1) {
      loop_ub++;
    }
  }

  if (x->size[0] == 1) {
    if (idx == 0) {
      i7 = b_ii->size[0];
      b_ii->size[0] = 0;
      emxEnsureCapacity((emxArray__common *)b_ii, i7, (int)sizeof(int));
    }
  } else {
    i7 = b_ii->size[0];
    if (1 > idx) {
      b_ii->size[0] = 0;
    } else {
      b_ii->size[0] = idx;
    }

    emxEnsureCapacity((emxArray__common *)b_ii, i7, (int)sizeof(int));
  }

  emxFree_boolean_T(&x);
  i7 = iInflect->size[0];
  iInflect->size[0] = b_ii->size[0];
  emxEnsureCapacity((emxArray__common *)iInflect, i7, (int)sizeof(double));
  loop_ub = b_ii->size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    iInflect->data[i7] = iTemp->data[b_ii->data[i7]] - 1.0;
  }

  emxFree_int32_T(&b_ii);
  i7 = iPk->size[0];
  iPk->size[0] = ii->size[0];
  emxEnsureCapacity((emxArray__common *)iPk, i7, (int)sizeof(double));
  loop_ub = ii->size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    iPk->data[i7] = iTemp->data[ii->data[i7]] - 1.0;
  }

  emxFree_int32_T(&ii);
  emxFree_real_T(&iTemp);
}

//
// Arguments    : const emxArray_real_T *Yin
//                double varargin_4
//                emxArray_real_T *Ypk
//                emxArray_real_T *Xpk
// Return Type  : void
//
void findpeaks(const emxArray_real_T *Yin, double varargin_4, emxArray_real_T
               *Ypk, emxArray_real_T *Xpk)
{
  int Yin_idx_0;
  emxArray_real_T *y;
  int i6;
  emxArray_real_T *x;
  emxArray_boolean_T *idelete;
  emxArray_int32_T *ii;
  int nx;
  int idx;
  boolean_T exitg1;
  boolean_T guard1 = false;
  emxArray_real_T *iInfite;
  emxArray_real_T *yTemp;
  emxArray_real_T *iPk;
  emxArray_real_T *locs;
  int i;
  double b_locs;
  emxArray_real_T *b_iPk;
  emxArray_int32_T *ib;
  emxArray_boolean_T *r0;
  unsigned int unnamed_idx_0;
  double c_locs;
  Yin_idx_0 = Yin->size[1];
  emxInit_real_T(&y, 2);
  if (Yin_idx_0 < 1) {
    i6 = y->size[0] * y->size[1];
    y->size[0] = 1;
    y->size[1] = 0;
    emxEnsureCapacity((emxArray__common *)y, i6, (int)sizeof(double));
  } else {
    Yin_idx_0 = Yin->size[1];
    i6 = y->size[0] * y->size[1];
    y->size[0] = 1;
    y->size[1] = (int)((double)Yin_idx_0 - 1.0) + 1;
    emxEnsureCapacity((emxArray__common *)y, i6, (int)sizeof(double));
    Yin_idx_0 = (int)((double)Yin_idx_0 - 1.0);
    for (i6 = 0; i6 <= Yin_idx_0; i6++) {
      y->data[y->size[0] * i6] = 1.0 + (double)i6;
    }
  }

  emxInit_real_T1(&x, 1);
  i6 = x->size[0];
  x->size[0] = y->size[1];
  emxEnsureCapacity((emxArray__common *)x, i6, (int)sizeof(double));
  Yin_idx_0 = y->size[1];
  for (i6 = 0; i6 < Yin_idx_0; i6++) {
    x->data[i6] = y->data[y->size[0] * i6];
  }

  emxInit_boolean_T(&idelete, 1);
  i6 = idelete->size[0];
  idelete->size[0] = Yin->size[1];
  emxEnsureCapacity((emxArray__common *)idelete, i6, (int)sizeof(boolean_T));
  Yin_idx_0 = Yin->size[1];
  for (i6 = 0; i6 < Yin_idx_0; i6++) {
    idelete->data[i6] = rtIsInf(Yin->data[i6]);
  }

  i6 = idelete->size[0];
  emxEnsureCapacity((emxArray__common *)idelete, i6, (int)sizeof(boolean_T));
  Yin_idx_0 = idelete->size[0];
  for (i6 = 0; i6 < Yin_idx_0; i6++) {
    idelete->data[i6] = (idelete->data[i6] && (Yin->data[i6] > 0.0));
  }

  emxInit_int32_T(&ii, 1);
  nx = idelete->size[0];
  idx = 0;
  i6 = ii->size[0];
  ii->size[0] = idelete->size[0];
  emxEnsureCapacity((emxArray__common *)ii, i6, (int)sizeof(int));
  Yin_idx_0 = 1;
  exitg1 = false;
  while ((!exitg1) && (Yin_idx_0 <= nx)) {
    guard1 = false;
    if (idelete->data[Yin_idx_0 - 1]) {
      idx++;
      ii->data[idx - 1] = Yin_idx_0;
      if (idx >= nx) {
        exitg1 = true;
      } else {
        guard1 = true;
      }
    } else {
      guard1 = true;
    }

    if (guard1) {
      Yin_idx_0++;
    }
  }

  if (idelete->size[0] == 1) {
    if (idx == 0) {
      i6 = ii->size[0];
      ii->size[0] = 0;
      emxEnsureCapacity((emxArray__common *)ii, i6, (int)sizeof(int));
    }
  } else {
    i6 = ii->size[0];
    if (1 > idx) {
      ii->size[0] = 0;
    } else {
      ii->size[0] = idx;
    }

    emxEnsureCapacity((emxArray__common *)ii, i6, (int)sizeof(int));
  }

  emxInit_real_T1(&iInfite, 1);
  i6 = iInfite->size[0];
  iInfite->size[0] = ii->size[0];
  emxEnsureCapacity((emxArray__common *)iInfite, i6, (int)sizeof(double));
  Yin_idx_0 = ii->size[0];
  for (i6 = 0; i6 < Yin_idx_0; i6++) {
    iInfite->data[i6] = ii->data[i6];
  }

  emxInit_real_T1(&yTemp, 1);
  i6 = yTemp->size[0];
  yTemp->size[0] = Yin->size[1];
  emxEnsureCapacity((emxArray__common *)yTemp, i6, (int)sizeof(double));
  Yin_idx_0 = Yin->size[1];
  for (i6 = 0; i6 < Yin_idx_0; i6++) {
    yTemp->data[i6] = Yin->data[i6];
  }

  i6 = ii->size[0];
  ii->size[0] = iInfite->size[0];
  emxEnsureCapacity((emxArray__common *)ii, i6, (int)sizeof(int));
  Yin_idx_0 = iInfite->size[0];
  for (i6 = 0; i6 < Yin_idx_0; i6++) {
    ii->data[i6] = (int)iInfite->data[i6];
  }

  Yin_idx_0 = ii->size[0];
  for (i6 = 0; i6 < Yin_idx_0; i6++) {
    yTemp->data[ii->data[i6] - 1] = rtNaN;
  }

  emxInit_real_T1(&iPk, 1);
  emxInit_real_T1(&locs, 1);
  findLocalMaxima(yTemp, iPk, locs);
  if (!(iPk->size[0] == 0)) {
    nx = iPk->size[0] - 1;
    idx = 0;
    for (i = 0; i <= nx; i++) {
      if (Yin->data[(int)iPk->data[i] - 1] > 0.35) {
        idx++;
      }
    }

    Yin_idx_0 = 0;
    for (i = 0; i <= nx; i++) {
      if (Yin->data[(int)iPk->data[i] - 1] > 0.35) {
        iPk->data[Yin_idx_0] = iPk->data[i];
        Yin_idx_0++;
      }
    }

    i6 = iPk->size[0];
    iPk->size[0] = idx;
    emxEnsureCapacity((emxArray__common *)iPk, i6, (int)sizeof(double));
  }

  nx = iPk->size[0];
  i6 = yTemp->size[0];
  yTemp->size[0] = nx;
  emxEnsureCapacity((emxArray__common *)yTemp, i6, (int)sizeof(double));
  for (Yin_idx_0 = 0; Yin_idx_0 + 1 <= nx; Yin_idx_0++) {
    if ((Yin->data[(int)(iPk->data[Yin_idx_0] - 1.0) - 1] >= Yin->data[(int)
         (iPk->data[Yin_idx_0] + 1.0) - 1]) || rtIsNaN(Yin->data[(int)(iPk->
          data[Yin_idx_0] + 1.0) - 1])) {
      b_locs = Yin->data[(int)(iPk->data[Yin_idx_0] - 1.0) - 1];
    } else {
      b_locs = Yin->data[(int)(iPk->data[Yin_idx_0] + 1.0) - 1];
    }

    yTemp->data[Yin_idx_0] = b_locs;
  }

  nx = iPk->size[0] - 1;
  idx = 0;
  for (i = 0; i <= nx; i++) {
    if (Yin->data[(int)iPk->data[i] - 1] - yTemp->data[i] >= 0.0) {
      idx++;
    }
  }

  Yin_idx_0 = 0;
  for (i = 0; i <= nx; i++) {
    if (Yin->data[(int)iPk->data[i] - 1] - yTemp->data[i] >= 0.0) {
      iPk->data[Yin_idx_0] = iPk->data[i];
      Yin_idx_0++;
    }
  }

  emxInit_real_T1(&b_iPk, 1);
  emxInit_int32_T(&ib, 1);
  i6 = iPk->size[0];
  iPk->size[0] = idx;
  emxEnsureCapacity((emxArray__common *)iPk, i6, (int)sizeof(double));
  do_vectors(iPk, iInfite, b_iPk, ii, ib);
  emxFree_int32_T(&ib);
  emxInit_boolean_T(&r0, 1);
  if ((b_iPk->size[0] == 0) || (varargin_4 == 0.0)) {
    if (b_iPk->size[0] < 1) {
      i6 = y->size[0] * y->size[1];
      y->size[0] = 1;
      y->size[1] = 0;
      emxEnsureCapacity((emxArray__common *)y, i6, (int)sizeof(double));
    } else {
      i6 = b_iPk->size[0];
      Yin_idx_0 = y->size[0] * y->size[1];
      y->size[0] = 1;
      y->size[1] = (int)((double)i6 - 1.0) + 1;
      emxEnsureCapacity((emxArray__common *)y, Yin_idx_0, (int)sizeof(double));
      Yin_idx_0 = (int)((double)i6 - 1.0);
      for (i6 = 0; i6 <= Yin_idx_0; i6++) {
        y->data[y->size[0] * i6] = 1.0 + (double)i6;
      }
    }

    i6 = yTemp->size[0];
    yTemp->size[0] = y->size[1];
    emxEnsureCapacity((emxArray__common *)yTemp, i6, (int)sizeof(double));
    Yin_idx_0 = y->size[1];
    for (i6 = 0; i6 < Yin_idx_0; i6++) {
      yTemp->data[i6] = y->data[y->size[0] * i6];
    }
  } else {
    i6 = locs->size[0];
    locs->size[0] = b_iPk->size[0];
    emxEnsureCapacity((emxArray__common *)locs, i6, (int)sizeof(double));
    Yin_idx_0 = b_iPk->size[0];
    for (i6 = 0; i6 < Yin_idx_0; i6++) {
      locs->data[i6] = x->data[(int)b_iPk->data[i6] - 1];
    }

    i6 = yTemp->size[0];
    yTemp->size[0] = b_iPk->size[0];
    emxEnsureCapacity((emxArray__common *)yTemp, i6, (int)sizeof(double));
    Yin_idx_0 = b_iPk->size[0];
    for (i6 = 0; i6 < Yin_idx_0; i6++) {
      yTemp->data[i6] = Yin->data[(int)b_iPk->data[i6] - 1];
    }

    sort(yTemp, ii);
    i6 = iInfite->size[0];
    iInfite->size[0] = ii->size[0];
    emxEnsureCapacity((emxArray__common *)iInfite, i6, (int)sizeof(double));
    Yin_idx_0 = ii->size[0];
    for (i6 = 0; i6 < Yin_idx_0; i6++) {
      iInfite->data[i6] = ii->data[i6];
    }

    i6 = yTemp->size[0];
    yTemp->size[0] = iInfite->size[0];
    emxEnsureCapacity((emxArray__common *)yTemp, i6, (int)sizeof(double));
    Yin_idx_0 = iInfite->size[0];
    for (i6 = 0; i6 < Yin_idx_0; i6++) {
      yTemp->data[i6] = locs->data[(int)iInfite->data[i6] - 1];
    }

    unnamed_idx_0 = (unsigned int)iInfite->size[0];
    i6 = idelete->size[0];
    idelete->size[0] = (int)unnamed_idx_0;
    emxEnsureCapacity((emxArray__common *)idelete, i6, (int)sizeof(boolean_T));
    Yin_idx_0 = (int)unnamed_idx_0;
    for (i6 = 0; i6 < Yin_idx_0; i6++) {
      idelete->data[i6] = false;
    }

    for (i = 0; i < iInfite->size[0]; i++) {
      if (!idelete->data[i]) {
        b_locs = locs->data[(int)iInfite->data[i] - 1] - varargin_4;
        c_locs = locs->data[(int)iInfite->data[i] - 1] + varargin_4;
        i6 = r0->size[0];
        r0->size[0] = yTemp->size[0];
        emxEnsureCapacity((emxArray__common *)r0, i6, (int)sizeof(boolean_T));
        Yin_idx_0 = yTemp->size[0];
        for (i6 = 0; i6 < Yin_idx_0; i6++) {
          r0->data[i6] = ((yTemp->data[i6] >= b_locs) && (yTemp->data[i6] <=
            c_locs));
        }

        i6 = idelete->size[0];
        emxEnsureCapacity((emxArray__common *)idelete, i6, (int)sizeof(boolean_T));
        Yin_idx_0 = idelete->size[0];
        for (i6 = 0; i6 < Yin_idx_0; i6++) {
          idelete->data[i6] = (idelete->data[i6] || r0->data[i6]);
        }

        idelete->data[i] = false;
      }
    }

    nx = idelete->size[0] - 1;
    idx = 0;
    for (i = 0; i <= nx; i++) {
      if (!idelete->data[i]) {
        idx++;
      }
    }

    i6 = ii->size[0];
    ii->size[0] = idx;
    emxEnsureCapacity((emxArray__common *)ii, i6, (int)sizeof(int));
    Yin_idx_0 = 0;
    for (i = 0; i <= nx; i++) {
      if (!idelete->data[i]) {
        ii->data[Yin_idx_0] = i + 1;
        Yin_idx_0++;
      }
    }

    i6 = yTemp->size[0];
    yTemp->size[0] = ii->size[0];
    emxEnsureCapacity((emxArray__common *)yTemp, i6, (int)sizeof(double));
    Yin_idx_0 = ii->size[0];
    for (i6 = 0; i6 < Yin_idx_0; i6++) {
      yTemp->data[i6] = iInfite->data[ii->data[i6] - 1];
    }

    c_sort(yTemp);
  }

  emxFree_boolean_T(&r0);
  emxFree_boolean_T(&idelete);
  emxFree_real_T(&locs);
  emxFree_int32_T(&ii);
  emxFree_real_T(&y);
  emxFree_real_T(&iInfite);
  Yin_idx_0 = Yin->size[1];
  if (yTemp->size[0] > Yin_idx_0) {
    Yin_idx_0 = Yin->size[1];
    i6 = yTemp->size[0];
    yTemp->size[0] = Yin_idx_0;
    emxEnsureCapacity((emxArray__common *)yTemp, i6, (int)sizeof(double));
  }

  i6 = iPk->size[0];
  iPk->size[0] = yTemp->size[0];
  emxEnsureCapacity((emxArray__common *)iPk, i6, (int)sizeof(double));
  Yin_idx_0 = yTemp->size[0];
  for (i6 = 0; i6 < Yin_idx_0; i6++) {
    iPk->data[i6] = b_iPk->data[(int)yTemp->data[i6] - 1];
  }

  emxFree_real_T(&yTemp);
  emxFree_real_T(&b_iPk);
  i6 = Ypk->size[0] * Ypk->size[1];
  Ypk->size[0] = 1;
  Ypk->size[1] = iPk->size[0];
  emxEnsureCapacity((emxArray__common *)Ypk, i6, (int)sizeof(double));
  Yin_idx_0 = iPk->size[0];
  for (i6 = 0; i6 < Yin_idx_0; i6++) {
    Ypk->data[Ypk->size[0] * i6] = Yin->data[(int)iPk->data[i6] - 1];
  }

  i6 = Xpk->size[0] * Xpk->size[1];
  Xpk->size[0] = 1;
  Xpk->size[1] = iPk->size[0];
  emxEnsureCapacity((emxArray__common *)Xpk, i6, (int)sizeof(double));
  Yin_idx_0 = iPk->size[0];
  for (i6 = 0; i6 < Yin_idx_0; i6++) {
    Xpk->data[Xpk->size[0] * i6] = x->data[(int)iPk->data[i6] - 1];
  }

  emxFree_real_T(&x);
  emxFree_real_T(&iPk);
}

//
// File trailer for findpeaks.cpp
//
// [EOF]
//
