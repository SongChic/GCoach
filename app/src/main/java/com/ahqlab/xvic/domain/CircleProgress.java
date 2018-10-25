package com.ahqlab.xvic.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CircleProgress implements Serializable {

    public interface ImageOnClick {
        void onClick( boolean state );
    }
    public interface AngleCallback {
        void onAngleListner ( int percent );
    }

//    /* Circle Progress Constant (s) */
    public static int NORMAL_TYPE = 0;
    public static int PROGRESS_TYPE = 1;
    public static int ANIMATION_TYPE = 2;
    public static int TEXT_TYPE = 3;

    public static int NORMAL_SIZE = 1;
    public static int MIDDLE_SIZE = 2;
    public static int SMALL_SIZE = 3;
    public static int TINY_SIZE = 4;
//    /* Circle Progress Constant (e) */

    private int step;
    private int imageResorce;
    private int type;
    private int[] btnImageResource;
    @Builder.Default
    private int padding = -1;
    @Builder.Default
    private int startColor = -1;
    @Builder.Default
    private int endColor = -1;

    @Builder.Default
    private int startImgColor = -1;
    @Builder.Default
    private int endImgColor = -1;
    @Builder.Default
    private int alpha = 100;
    @Builder.Default
    private int imgAlpha = 100;

    @Builder.Default
    private int size = 0;

    /* text mode */
    private String text;
    @Builder.Default
    private boolean selected = false;

    private ImageOnClick onClick;
    private AngleCallback angleListner;
}
