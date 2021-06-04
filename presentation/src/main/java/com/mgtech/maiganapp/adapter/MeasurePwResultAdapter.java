package com.mgtech.maiganapp.adapter;

import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IIndicatorModel;
import com.mgtech.maiganapp.utils.IndicatorUtils;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.widget.IndicatorBarView;
import com.mgtech.maiganapp.widget.IndicatorBpBarView;
import com.mgtech.maiganapp.widget.MeasurePwResultImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author zhaixiang
 * 参数 adapter
 */

public class MeasurePwResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IIndicatorModel> itemList = new ArrayList<>();
    private static final int TYPE_TIME = IIndicatorModel.TYPE_TIME;
    private static final int TYPE_BP_HR = IIndicatorModel.TYPE_BP_HR;
    private static final int TYPE_TITLE_START = IIndicatorModel.TYPE_TITLE_START;
    private static final int TYPE_TITLE_MIDDLE = IIndicatorModel.TYPE_TITLE_MIDDLE;
    private static final int TYPE_TITLE_END = IIndicatorModel.TYPE_TITLE_END;
    private static final int TYPE_FOOT = IIndicatorModel.TYPE_FOOT;
    private static final int TYPE_MARK = IIndicatorModel.TYPE_MARK;
    private static final int TYPE_PW_IMAGE = IIndicatorModel.TYPE_PW_IMAGE;
    private static final int TYPE_TAG = IIndicatorModel.TYPE_TAG;

    private Bitmap highPic;
    private Bitmap normalPic;
    private Bitmap lowPic;
    private Bitmap openPic;
    private Bitmap closePic;
    private Bitmap rightArrow;
    private int extraTagColor;
    private RecyclerView recyclerView;
    private Callback callback;
    private String noDataString;
    private String autoMeasure;
    private String manualMeasure;

    public interface Callback {
        void onMarkClick();
    }

    public MeasurePwResultAdapter(RecyclerView recyclerView, Callback callback) {
        int bitmapSize = recyclerView.getResources().getDimensionPixelSize(R.dimen.medium_material);
        this.highPic = ViewUtils.decodeSampleBitmapFromResource(recyclerView.getResources(), R.drawable.indicator_level_high, bitmapSize, bitmapSize);
        this.normalPic = ViewUtils.decodeSampleBitmapFromResource(recyclerView.getResources(), R.drawable.indicator_level_normal, bitmapSize, bitmapSize);
        this.lowPic = ViewUtils.decodeSampleBitmapFromResource(recyclerView.getResources(), R.drawable.indicator_level_low, bitmapSize, bitmapSize);
        this.openPic = ViewUtils.decodeSampleBitmapFromResource(recyclerView.getResources(), R.drawable.indicator_result_open, bitmapSize, bitmapSize);
        this.closePic = ViewUtils.decodeSampleBitmapFromResource(recyclerView.getResources(), R.drawable.indicator_result_close, bitmapSize, bitmapSize);
        this.rightArrow = ViewUtils.decodeSampleBitmapFromResource(recyclerView.getResources(), R.drawable.indicator_result_right, bitmapSize, bitmapSize);
        this.extraTagColor = ContextCompat.getColor(recyclerView.getContext(), R.color.warningRed);
        this.recyclerView = recyclerView;
        this.callback = callback;
        this.noDataString = recyclerView.getContext().getResources().getString(R.string.measure_pw_result_click_and_add_remark);
        this.autoMeasure = recyclerView.getContext().getResources().getString(R.string.measure_pw_result_auto_measure);
        this.manualMeasure = recyclerView.getContext().getResources().getString(R.string.measure_pw_result_manual_measure);
    }

    public void setList(List<IIndicatorModel> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_TIME) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pw_result_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_TAG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pw_result_tag, parent, false);
            return new TagViewHolder(view);
        } else if (viewType == TYPE_TITLE_START) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pw_result_title_start, parent, false);
            TitleViewHolder holder = new TitleViewHolder(view);
            holder.root.setOnClickListener(onItemClick);
            return holder;
        } else if (viewType == TYPE_TITLE_MIDDLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pw_result_title_middle, parent, false);
            TitleViewHolder holder = new TitleViewHolder(view);
            holder.root.setOnClickListener(onItemClick);
            return holder;
        } else if (viewType == TYPE_TITLE_END) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pw_result_title_end, parent, false);
            TitleViewHolder holder = new TitleViewHolder(view);
            holder.root.setOnClickListener(onItemClick);
            return holder;
        } else if (viewType == TYPE_FOOT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pw_result_footer, parent, false);
            return new FootViewHolder(view);
        } else if (viewType == TYPE_BP_HR) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pw_result_bp_hr, parent, false);
            BpHrViewHolder holder = new BpHrViewHolder(view);
            holder.layoutBp.setOnClickListener(onBpClick);
            holder.layoutHr.setOnClickListener(onHrClick);
            holder.ivHrUpArrow.setOnClickListener(onBpHrUpClick);
            holder.ivBpUpArrow.setOnClickListener(onBpHrUpClick);
            return holder;
        } else if (viewType == TYPE_MARK) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pw_result_mark, parent, false);
            view.setOnClickListener(onMarkClick);
            return new MarkViewHolder(view);
        } else if (viewType == TYPE_PW_IMAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pw_result_pw_image, parent, false);
            PwImageViewHolder pwImageViewHolder = new PwImageViewHolder(view);
            pwImageViewHolder.layoutTitle.setOnClickListener(onImageTitleClick);
            return pwImageViewHolder;
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            IIndicatorModel.Time item = (IIndicatorModel.Time) itemList.get(position);
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.tv.setText(item.string);
            headerViewHolder.tvMeasureType.setText(item.isAuto ? autoMeasure : manualMeasure);
        } else if (holder instanceof TagViewHolder) {
            IIndicatorModel.TAG item = (IIndicatorModel.TAG) itemList.get(position);
            TagViewHolder tagViewHolder = (TagViewHolder) holder;
            tagViewHolder.tv.setText(item.string);
        } else if (holder instanceof TitleViewHolder) {
            IIndicatorModel.Title titleItem = (IIndicatorModel.Title) itemList.get(position);
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            SpannableString spannedTitle = new SpannableString(titleItem.name + titleItem.extraTag);
            spannedTitle.setSpan(new ForegroundColorSpan(extraTagColor), titleItem.name.length(),
                    titleItem.name.length() + titleItem.extraTag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleViewHolder.tvTitle.setText(spannedTitle);
            titleViewHolder.tvUnit.setText(titleItem.unit);
            titleViewHolder.tvValue.setText(IndicatorUtils.indicatorValueToString(titleItem.value));
            setLevelImage(titleItem.level, titleViewHolder.ivLevel);
            setOpenImage(titleItem.isOpen, titleViewHolder.ivOpen);
            titleViewHolder.layoutDesc.setVisibility(titleItem.isOpen && titleItem.descriptionItem != null ? View.VISIBLE : View.GONE);
            titleViewHolder.tvDesc.setText(titleItem.getDesc());
            titleViewHolder.barView.setValue(titleItem.value, titleItem.getLow(), titleItem.getHigh());
        } else if (holder instanceof FootViewHolder) {
            IIndicatorModel.Footer footItem = (IIndicatorModel.Footer) itemList.get(position);
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            footViewHolder.tv.setText(footItem.text);
        } else if (holder instanceof BpHrViewHolder) {
            IIndicatorModel.HrBp bpHrItem = (IIndicatorModel.HrBp) itemList.get(position);
            BpHrViewHolder bpHrViewHolder = (BpHrViewHolder) holder;
            bpHrViewHolder.tvValueBp.setText(Math.round(bpHrItem.valuePs) + "/" + Math.round(bpHrItem.valuePd));
            bpHrViewHolder.tvValueHr.setText(String.valueOf(Math.round(bpHrItem.valueHr)));
            setLevelImage(bpHrItem.getLevelBp(), bpHrViewHolder.ivLevelBp);
            setLevelImage(bpHrItem.levelHr, bpHrViewHolder.ivLevelHr);
            bpHrViewHolder.layoutDescBp.setVisibility(bpHrItem.isBpOpen ? View.VISIBLE : View.GONE);
            bpHrViewHolder.ivArrowBp.setVisibility(bpHrItem.isBpOpen ? View.GONE : View.VISIBLE);
            bpHrViewHolder.dividerBp.setVisibility(bpHrItem.isBpOpen ? View.GONE : View.VISIBLE);
            bpHrViewHolder.bvHr.setValue(bpHrItem.valueHr, bpHrItem.getHrLow(), bpHrItem.getHrHigh());
            bpHrViewHolder.bvBp.setValue(bpHrItem.valuePs, bpHrItem.valuePd);

            bpHrViewHolder.layoutDescHr.setVisibility(bpHrItem.isHrOpen ? View.VISIBLE : View.GONE);
            bpHrViewHolder.ivArrowHr.setVisibility(bpHrItem.isHrOpen ? View.GONE : View.VISIBLE);
            bpHrViewHolder.dividerHr.setVisibility(bpHrItem.isHrOpen ? View.GONE : View.VISIBLE);
        } else if (holder instanceof MarkViewHolder) {
            IIndicatorModel.Mark markItem = (IIndicatorModel.Mark) itemList.get(position);
            MarkViewHolder markViewHolder = (MarkViewHolder) holder;
            markViewHolder.tvTitle.setText(markItem.title);
            markViewHolder.iv.setVisibility(markItem.isUser ? View.VISIBLE : View.GONE);
            if (TextUtils.isEmpty(markItem.content)) {
                markItem.content = noDataString;
            }
            markViewHolder.tvContent.setText(markItem.content);
        } else if (holder instanceof PwImageViewHolder) {
            IIndicatorModel.Image imageItem = (IIndicatorModel.Image) itemList.get(position);
            PwImageViewHolder pwImageViewHolder = (PwImageViewHolder) holder;
            pwImageViewHolder.imageView.setData(imageItem.getData());
            pwImageViewHolder.layoutContent.setVisibility(imageItem.isOpen ? View.VISIBLE : View.GONE);
            setOpenImage(imageItem.isOpen, pwImageViewHolder.ivOpen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        IIndicatorModel item = itemList.get(position);
        return item.getType();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView tvMeasureType;

        HeaderViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            tvMeasureType = itemView.findViewById(R.id.tv_measure_type);
        }
    }

    private static class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        TagViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvValue;
        TextView tvUnit;
        ImageView ivLevel;
        ImageView ivOpen;
        View root;
        TextView tvDesc;
        IndicatorBarView barView;
        View layoutDesc;

        TitleViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
            tvValue = view.findViewById(R.id.tv_value);
            tvUnit = view.findViewById(R.id.tv_unit);
            ivLevel = view.findViewById(R.id.iv_level);
            ivOpen = view.findViewById(R.id.iv_open);
            root = view.findViewById(R.id.layout);
            tvDesc = itemView.findViewById(R.id.tv_content);
            barView = itemView.findViewById(R.id.barView);
            layoutDesc = view.findViewById(R.id.layout_desc);
        }
    }


    private static class FootViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        FootViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_content);
        }
    }

    private static class PwImageViewHolder extends RecyclerView.ViewHolder {
        MeasurePwResultImageView imageView;
        ImageView ivOpen;
        View layoutTitle;
        View layoutContent;


        PwImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.resultImageView);
            ivOpen = itemView.findViewById(R.id.iv_open);
            layoutTitle = itemView.findViewById(R.id.layout_title);
            layoutContent = itemView.findViewById(R.id.layout_content);
        }
    }

    private static class BpHrViewHolder extends RecyclerView.ViewHolder {
        TextView tvValueBp;
        TextView tvValueHr;
        ImageView ivLevelBp;
        ImageView ivLevelHr;
        IndicatorBarView bvHr;
        IndicatorBpBarView bvBp;
        View layoutDescHr;
        View layoutDescBp;
        View dividerHr;
        View dividerBp;
        View ivArrowHr;
        View ivArrowBp;
        View layoutHr;
        View layoutBp;
        ImageView ivHrUpArrow;
        ImageView ivBpUpArrow;

        BpHrViewHolder(View itemView) {
            super(itemView);
            tvValueBp = itemView.findViewById(R.id.tv_value_bp);
            tvValueHr = itemView.findViewById(R.id.tv_value_hr);
            ivLevelBp = itemView.findViewById(R.id.iv_mark_bp);
            ivLevelHr = itemView.findViewById(R.id.iv_mark_hr);
            bvHr = itemView.findViewById(R.id.barView_hr);
            bvBp = itemView.findViewById(R.id.barView_bp);
            layoutDescHr = itemView.findViewById(R.id.layout_hr_description);
            layoutDescBp = itemView.findViewById(R.id.layout_bp_description);
            dividerHr = itemView.findViewById(R.id.divider_hr);
            dividerBp = itemView.findViewById(R.id.divider_bp);
            ivArrowHr = itemView.findViewById(R.id.iv_hr_arrow);
            ivArrowBp = itemView.findViewById(R.id.iv_bp_arrow);
            layoutHr = itemView.findViewById(R.id.layout_hr);
            layoutBp = itemView.findViewById(R.id.layout_bp);
            ivHrUpArrow = itemView.findViewById(R.id.iv_hr_up_arrow);
            ivBpUpArrow = itemView.findViewById(R.id.iv_bp_up_arrow);
        }
    }

    private static class MarkViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        ImageView iv;

        MarkViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            iv = itemView.findViewById(R.id.iv);
        }
    }

    private View.OnClickListener onItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);
            IIndicatorModel model = itemList.get(position);
            if (model instanceof IIndicatorModel.Title) {
                IIndicatorModel.Title title = (IIndicatorModel.Title) model;
                title.isOpen = !title.isOpen;
                notifyItemChanged(position);
            }
        }
    };

    private View.OnClickListener onImageTitleClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = recyclerView.getChildLayoutPosition((View) view.getParent().getParent());
            IIndicatorModel model = itemList.get(position);
            if (model instanceof IIndicatorModel.Image) {
                IIndicatorModel.Image image = (IIndicatorModel.Image) model;
                image.isOpen = !image.isOpen;
                notifyItemChanged(position);
            }
        }
    };

    private View.OnClickListener onMarkClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.onMarkClick();
            }
        }
    };

    private View.OnClickListener onBpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (itemList.size() < 2) {
                return;
            }
            IIndicatorModel model = itemList.get(1);
            if (model instanceof IIndicatorModel.HrBp) {
                IIndicatorModel.HrBp hrBp = (IIndicatorModel.HrBp) model;
                hrBp.isBpOpen = !hrBp.isBpOpen;
                hrBp.isHrOpen = false;
                notifyItemChanged(1);
            }
        }
    };

    private View.OnClickListener onBpHrUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (itemList.size() < 2) {
                return;
            }
            IIndicatorModel model = itemList.get(1);
            if (model instanceof IIndicatorModel.HrBp) {
                IIndicatorModel.HrBp hrBp = (IIndicatorModel.HrBp) model;
                hrBp.isBpOpen = false;
                hrBp.isHrOpen = false;
                notifyItemChanged(1);
            }
        }
    };

    private View.OnClickListener onHrClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (itemList.size() < 2) {
                return;
            }
            IIndicatorModel model = itemList.get(1);
            if (model instanceof IIndicatorModel.HrBp) {
                IIndicatorModel.HrBp hrBp = (IIndicatorModel.HrBp) model;
                hrBp.isHrOpen = !hrBp.isHrOpen;
                hrBp.isBpOpen = false;
                notifyItemChanged(1);
            }
        }
    };


    private void setLevelImage(int level, ImageView iv) {
        switch (level) {
            case 0:
                iv.setImageBitmap(normalPic);
                break;
            case 1:
                iv.setImageBitmap(highPic);
                break;
            case 2:
                iv.setImageBitmap(lowPic);
                break;
            default:
        }
    }

    private void setOpenImage(boolean isOpen, ImageView iv) {
        iv.setImageBitmap(isOpen ? openPic : closePic);
    }
}
