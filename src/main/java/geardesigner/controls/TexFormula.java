package geardesigner.controls;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 将Latex公式解析为JavaFX格式的Image对象
 * @author SUPERSTATION
 */
public class TexFormula {
    private final String formulaPattern;
    /**
     * 设置公式样式，0-“显示”样式（默认）；2-“文本”样式；4-“脚本”样式；6-更小的“脚本”样式
     * If the given integer valuePane does not represent a valid TeX style, the default style TeXConstants.STYLE_DISPLAY will be used.
     */
    private final int style;
    /**
     * point-size字体大小
     */
    private final float fontSize;

    private Image imb ;
    /**
     * 构造Latex公式符号
     * @param p latex字符串
     */
    public TexFormula(String p){
        formulaPattern= p;
        fontSize = 12;
        style = 0;
    }

    public Image getPatternImage(){
        if (imb==null){
            parsePattern();
        }
        return imb;
    }

    private void parsePattern(){
        final TeXFormula txf = new TeXFormula(formulaPattern);
        /**
         * bg设定为null，启用ARGB，背景透明
         */
        final java.awt.Image bufferedImage = txf.createBufferedImage(style,fontSize, Color.BLACK, null);
        /**
         * wimg设置为null，使image对象不可变
         */
        imb=SwingFXUtils.toFXImage((BufferedImage) bufferedImage, null);
    }

    public String getFormula(){
        return formulaPattern;
    }
}
