//
// File: detect_ecg.cpp
//
// MATLAB Coder version            : 3.2
// C/C++ source code generated on  : 18-Aug-2017 19:21:12
//

// Include Files
#include "rt_nonfinite.h"
#include "detect_ecg.h"
#include "filter_ecg.h"
#include "ecg_emxutil.h"
#include "findpeaks.h"
#include "imodwt.h"
#include "modwt.h"
#include "ecg_rtwutil.h"

// Function Definitions

//
// 用sym4小波分解ECG波形
// Arguments    : const emxArray_real_T *Ecg
//                double Fs
//                double HrLo
//                double HrHi
//                double RRNum
//                double *err
//                double *rrNum
//                double *hrAvg
//                double *vMax
//                double *vMin
// Return Type  : void
//
void detect_ecg(const emxArray_real_T *Ecg, double Fs, double HrLo, double HrHi,
                double RRNum, double *err, double *rrNum, double *hrAvg, double *
                vMax, double *vMin)
{
  emxArray_real_T *wt;
  unsigned int uv0[2];
  int i0;
  emxArray_real_T *wtrec;
  int n;
  int i1;
  emxArray_real_T *y;
  int ixstart;
  double rrWidth;
  int ix;
  boolean_T exitg6;
  emxArray_real_T *unusedU0;
  emxArray_real_T *rLocs;
  double headLocs;
  double tailLocs;
  int exitg1;
  boolean_T guard1 = false;
  boolean_T exitg5;
  boolean_T exitg3;
  boolean_T exitg4;
  boolean_T exitg2;
  emxInit_real_T(&wt, 2);

  // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  //  识别心电信号
  //  根据R波识别心电信号
  // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  //  [in]Ecg：心电数据
  //  [in]Fs：心电数据采样率
  //  [in]HrLo：有效心率最小值（Cmd为1时有效，单位BPM）
  //  [in]HrHi：有效心率最大值（Cmd为1时有效，单位BPM）
  //  [in]RRNum：需要识别的连续R-R波数量
  //  [out]err：错误码
  //            0：识别成功，其它返回值有效
  //            -1：未识别出足够数量R-R波
  //  [out]rrNum：有效连续R-R波数量
  //  [out]hrAvg：平均心率
  //  [out]vMax：有效数据最大值
  //  [out]vMin：有效数据最小值
  modwt(Ecg, wt);

  //  只用小波系数为比例4、5的数据重组ECG频域波形，其中Scale 4：[11.25,22.5)Hz，Scale 5：[5.625,11.25)Hz 
  for (i0 = 0; i0 < 2; i0++) {
    uv0[i0] = (unsigned int)wt->size[i0];
  }

  emxInit_real_T(&wtrec, 2);
  i0 = wtrec->size[0] * wtrec->size[1];
  wtrec->size[0] = 6;
  wtrec->size[1] = (int)uv0[1];
  emxEnsureCapacity((emxArray__common *)wtrec, i0, (int)sizeof(double));
  n = 6 * (int)uv0[1];
  for (i0 = 0; i0 < n; i0++) {
    wtrec->data[i0] = 0.0;
  }

  n = wt->size[1] - 1;
  for (i0 = 0; i0 <= n; i0++) {
    for (i1 = 0; i1 < 2; i1++) {
      wtrec->data[(i1 + wtrec->size[0] * i0) + 3] = wt->data[(i1 + wt->size[0] *
        i0) + 3];
    }
  }

  emxFree_real_T(&wt);
  emxInit_real_T(&y, 2);
  imodwt(wtrec, y);

  //  用平方绝对值归一化结果定位R波
  //  y = abs(y).^2;    不能使用.^，否则生成C代码时需要openMP，IOS不支持
  i0 = y->size[1];
  n = 0;
  emxFree_real_T(&wtrec);
  while (n <= i0 - 1) {
    y->data[n] *= y->data[n];
    n++;
  }

  ixstart = 1;
  n = y->size[1];
  rrWidth = y->data[0];
  if (y->size[1] > 1) {
    if (rtIsNaN(y->data[0])) {
      ix = 2;
      exitg6 = false;
      while ((!exitg6) && (ix <= n)) {
        ixstart = ix;
        if (!rtIsNaN(y->data[ix - 1])) {
          rrWidth = y->data[ix - 1];
          exitg6 = true;
        } else {
          ix++;
        }
      }
    }

    if (ixstart < y->size[1]) {
      while (ixstart + 1 <= n) {
        if (y->data[ixstart] > rrWidth) {
          rrWidth = y->data[ixstart];
        }

        ixstart++;
      }
    }
  }

  i0 = y->size[0] * y->size[1];
  y->size[0] = 1;
  emxEnsureCapacity((emxArray__common *)y, i0, (int)sizeof(double));
  n = y->size[0];
  ixstart = y->size[1];
  n *= ixstart;
  for (i0 = 0; i0 < n; i0++) {
    y->data[i0] /= rrWidth;
  }

  emxInit_real_T(&unusedU0, 2);
  emxInit_real_T(&rLocs, 2);
  findpeaks(y, 0.2 * Fs, unusedU0, rLocs);

  //  初始化返回结果
  *err = -1.0;
  *rrNum = 0.0;
  *hrAvg = 0.0;
  *vMax = 0.0;
  *vMin = 0.0;

  //  根据心率范围判断
  emxFree_real_T(&unusedU0);
  emxFree_real_T(&y);
  if (rLocs->size[1] > 1) {
    headLocs = rLocs->data[0];
    tailLocs = 0.0;
    n = 1;
    do {
      exitg1 = 0;
      if (n - 1 <= rLocs->size[1] - 2) {
        rrWidth = rLocs->data[n] - rLocs->data[n - 1];
        guard1 = false;
        if ((rrWidth >= 60.0 * Fs / HrHi) && (rrWidth <= 60.0 * Fs / HrLo)) {
          (*rrNum)++;
          *hrAvg += rrWidth;
          tailLocs = rLocs->data[n];
          guard1 = true;
        } else {
          //  根据R-R计算其它参数
          if (*rrNum >= RRNum) {
            *err = 0.0;
            *hrAvg = rt_roundd_snf(60.0 * Fs / *hrAvg * *rrNum);
            if (headLocs > tailLocs) {
              i0 = 0;
              i1 = 1;
            } else {
              i0 = (int)headLocs - 1;
              i1 = (int)tailLocs + 1;
            }

            ixstart = 1;
            n = (i1 - i0) - 1;
            rrWidth = Ecg->data[i0];
            if ((i1 - i0) - 1 > 1) {
              if (rtIsNaN(rrWidth)) {
                ix = 2;
                exitg3 = false;
                while ((!exitg3) && (ix <= n)) {
                  ixstart = ix;
                  if (!rtIsNaN(Ecg->data[(i0 + ix) - 1])) {
                    rrWidth = Ecg->data[(i0 + ix) - 1];
                    exitg3 = true;
                  } else {
                    ix++;
                  }
                }
              }

              if (ixstart < (i1 - i0) - 1) {
                while (ixstart + 1 <= n) {
                  if (Ecg->data[i0 + ixstart] > rrWidth) {
                    rrWidth = Ecg->data[i0 + ixstart];
                  }

                  ixstart++;
                }
              }
            }

            *vMax = rt_roundd_snf(rrWidth);
            if (headLocs > tailLocs) {
              i0 = 0;
              i1 = 1;
            } else {
              i0 = (int)headLocs - 1;
              i1 = (int)tailLocs + 1;
            }

            ixstart = 1;
            n = (i1 - i0) - 1;
            rrWidth = Ecg->data[i0];
            if ((i1 - i0) - 1 > 1) {
              if (rtIsNaN(rrWidth)) {
                ix = 2;
                exitg2 = false;
                while ((!exitg2) && (ix <= n)) {
                  ixstart = ix;
                  if (!rtIsNaN(Ecg->data[(i0 + ix) - 1])) {
                    rrWidth = Ecg->data[(i0 + ix) - 1];
                    exitg2 = true;
                  } else {
                    ix++;
                  }
                }
              }

              if (ixstart < (i1 - i0) - 1) {
                while (ixstart + 1 <= n) {
                  if (Ecg->data[i0 + ixstart] < rrWidth) {
                    rrWidth = Ecg->data[i0 + ixstart];
                  }

                  ixstart++;
                }
              }
            }

            *vMin = rt_roundd_snf(rrWidth);
            exitg1 = 1;
          } else {
            *rrNum = 0.0;
            *hrAvg = 0.0;
            headLocs = rLocs->data[n];
            guard1 = true;
          }
        }

        if (guard1) {
          n++;
        }
      } else {
        //  根据R-R计算其它参数
        if (*rrNum > 0.0) {
          *hrAvg = rt_roundd_snf(60.0 * Fs / *hrAvg * *rrNum);
          if (headLocs > tailLocs) {
            i0 = 0;
            i1 = 1;
          } else {
            i0 = (int)headLocs - 1;
            i1 = (int)tailLocs + 1;
          }

          ixstart = 1;
          n = (i1 - i0) - 1;
          rrWidth = Ecg->data[i0];
          if ((i1 - i0) - 1 > 1) {
            if (rtIsNaN(rrWidth)) {
              ix = 2;
              exitg5 = false;
              while ((!exitg5) && (ix <= n)) {
                ixstart = ix;
                if (!rtIsNaN(Ecg->data[(i0 + ix) - 1])) {
                  rrWidth = Ecg->data[(i0 + ix) - 1];
                  exitg5 = true;
                } else {
                  ix++;
                }
              }
            }

            if (ixstart < (i1 - i0) - 1) {
              while (ixstart + 1 <= n) {
                if (Ecg->data[i0 + ixstart] > rrWidth) {
                  rrWidth = Ecg->data[i0 + ixstart];
                }

                ixstart++;
              }
            }
          }

          *vMax = rt_roundd_snf(rrWidth);
          if (headLocs > tailLocs) {
            i0 = 0;
            i1 = 1;
          } else {
            i0 = (int)headLocs - 1;
            i1 = (int)tailLocs + 1;
          }

          ixstart = 1;
          n = (i1 - i0) - 1;
          rrWidth = Ecg->data[i0];
          if ((i1 - i0) - 1 > 1) {
            if (rtIsNaN(rrWidth)) {
              ix = 2;
              exitg4 = false;
              while ((!exitg4) && (ix <= n)) {
                ixstart = ix;
                if (!rtIsNaN(Ecg->data[(i0 + ix) - 1])) {
                  rrWidth = Ecg->data[(i0 + ix) - 1];
                  exitg4 = true;
                } else {
                  ix++;
                }
              }
            }

            if (ixstart < (i1 - i0) - 1) {
              while (ixstart + 1 <= n) {
                if (Ecg->data[i0 + ixstart] < rrWidth) {
                  rrWidth = Ecg->data[i0 + ixstart];
                }

                ixstart++;
              }
            }
          }

          *vMin = rt_roundd_snf(rrWidth);
        }

        //  R-R数量满足要求
        if (*rrNum >= RRNum) {
          *err = 0.0;
        }

        exitg1 = 1;
      }
    } while (exitg1 == 0);
  }

  emxFree_real_T(&rLocs);
}

//
// File trailer for detect_ecg.cpp
//
// [EOF]
//
