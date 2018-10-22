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

//    /* Circle Progress Constant (s) */
    public static int NORMAL_TYPE = 0;
    public static int PROGRESS_TYPE = 1;
    public static int ANIMATION_TYPE = 2;

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
    private int startColor = -1;
    @Builder.Default
    private int endColor = -1;

    private int startImgColor;
    private int endImgColor;

    private int alpha;
    private int size;

    private ImageOnClick onClick;
}
