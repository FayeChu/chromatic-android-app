//package edu.uw.wuyiz.chromatic;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.support.annotation.ColorInt;
//import android.support.v7.widget.CardView;
//import android.util.AttributeSet;
//
//import static android.support.v7.cardview.R.color.cardview_dark_background;
//import static android.support.v7.cardview.R.color.cardview_light_background;
//
///**
// * Created by xfchu on 12/5/17.
// */
//
//public class SelectableCardView extends CardView {
//    private int unselectedBackgroundColor;
//    private int selectedBackgroundColor;
//    private boolean isSelectedble;
//
//    public SelectableCardView(Context context) {
//        this(context,null);
//    }
//
//    public SelectableCardView(Context context, AttributeSet attrs) {
//        this(context,attrs,0);
//    }
//
//    public SelectableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        final Resources res= context.getResources();
//        selectedBackgroundColor = cardview_dark_background;
//        unselectedBackgroundColor = cardview_light_background; // you could use cardview_light_background or cardview_dark_background for the default values here, depending on your theme
//    }
//
//    public void setSelectedBackgroundColor(@ColorInt int selectedBackgroundColor) {
//        this.selectedBackgroundColor = selectedBackgroundColor;
//    }
//
//    public void setUnselectedBackgroundColor(@ColorInt int unselectedBackgroundColor) {
//        this.unselectedBackgroundColor = unselectedBackgroundColor;
//    }
//
//    @Override
//    public void setSelected(final boolean selected) {
//        super.setSelected(selected);
//        if(isSelectedble)
//            setCardBackgroundColor(isSelected()? selectedBackgroundColor : unselectedBackgroundColor);
//    }
//
//    public void setSelectedble(final boolean selectedble) {
//        if(!selectedble) {
//            super.setSelected(false);
//            setCardBackgroundColor(unselectedBackgroundColor);
//        }
//        isSelectedble = selectedble;
//    }
//}
