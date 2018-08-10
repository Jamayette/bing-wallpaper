package com.jamayette.wallpaper.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

/**
 * 显示行号
 */
public class LineNumberHeaderView extends JComponent {

    private static final long serialVersionUID = 1L;
    private final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    public final Color DEFAULT_BACKGROUD = new Color(228, 228, 228);
    public final Color DEFAULT_FOREGROUD = Color.BLACK;
    public final int nHEIGHT = Integer.MAX_VALUE - 1000000;
    public final int MARGIN = 5;
    private int width = 50;
    private int limit = 9999;
    private int overTimes = 1;
    private int lineHeight;
    private int fontLineHeight;
    private int currentRowWidth;
    private FontMetrics fontMetrics;

    public LineNumberHeaderView() {
        this.setFont(DEFAULT_FONT);
        super.setForeground(DEFAULT_FOREGROUD);
        super.setBackground(DEFAULT_BACKGROUD);
        lineHeight = this.getLineHeight();
        fontLineHeight = (limit * lineHeight + 6);
        super.setPreferredSize(new Dimension(width, fontLineHeight));
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        fontMetrics = this.getFontMetrics(getFont());
        fontLineHeight = fontMetrics.getHeight();
    }

    public int getLineHeight() {
        return 17;
		/*
		ConsoleDialog.showDebug("lineHeight: " + lineHeight + " - fontLineHeight: " + fontLineHeight);
		if (lineHeight == 0) {
			return fontLineHeight;
		}
		return lineHeight;
		*/
    }

    public void setLineHeight(int lineHeight) {
        if (lineHeight > 0) {
            this.lineHeight = lineHeight;
        }
    }

    public int getStartOffset() {
        return 4;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // int startOffset = this.getStartOffset();
        Rectangle drawHere = g.getClipBounds();
        g.setColor(getBackground());
        g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);
        g.setColor(getForeground());
        g.setFont(DEFAULT_FONT);
        int startLineNum = (drawHere.y / lineHeight) + 1;
        int endLineNum = startLineNum + (drawHere.height / lineHeight);
        int start = (drawHere.y / lineHeight) * lineHeight + lineHeight;// - startOffset;
        for (int i = startLineNum; i <= endLineNum; ++i) {
            String lineNum = String.valueOf(i);
            // int width = fontMetrics.stringWidth(lineNum);
            // System.out.printf("%s - %d - %d\n", lineNum, start, nlineHeight);
            g.drawString(lineNum + " ", MARGIN + currentRowWidth, start);
            start += lineHeight;

            if ((limit * overTimes) == i) {
                // System.out.printf("limit=%d; overLimit=%d; i=%d;\n", limit, overTimes, i);
                overTimes += 1;
                super.setPreferredSize(new Dimension(width, (fontLineHeight * overTimes)));
            }
        }
    }
}
