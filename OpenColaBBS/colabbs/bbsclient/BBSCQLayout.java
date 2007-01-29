
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.

package colabbs.bbsclient;

import java.awt.*;

public class BBSCQLayout extends FlowLayout
{

	public BBSCQLayout()
	{
  	super(FlowLayout.CENTER,0,0);
	}

  /**
   * Returns the preferred dimensions given the components
   * in the target container.
   * @param target the component to lay out
   */
  public Dimension preferredLayoutSize(Container target)
  {
    Dimension tarsiz = new Dimension(0, 0);

    for (int i = 0 ; i < target.getComponentCount(); i++) {
      Component m = target.getComponent(i);
      if (m.isVisible()) {
        Dimension d = m.getPreferredSize();
        tarsiz.width = Math.max(tarsiz.width, d.width);
        if (i > 0) {
          tarsiz.height += getVgap();
        }
        tarsiz.height += d.height;
      }
    }
    Insets insets = target.getInsets();
    tarsiz.width += insets.left + insets.right + getHgap()*2;
    tarsiz.height += insets.top + insets.bottom + getVgap()*2;
    return tarsiz;
//    return new Dimension(target.getBounds().width,target.getBounds().height);
  }

  /**
   * Returns the minimum size needed to layout the target container
   * @param target the component to lay out
   */
  public Dimension minimumLayoutSize(Container target) {
    Dimension tarsiz = new Dimension(0, 0);

    for (int i = 0 ; i < target.getComponentCount(); i++) {
      Component m = target.getComponent(i);
      if (m.isVisible()) {
          Dimension d = m.getMinimumSize();
          tarsiz.width = Math.max(tarsiz.width, d.width);
          if (i > 0) {
            tarsiz.height += getVgap();
          }
          tarsiz.height += d.height;
      }
    }
    Insets insets = target.getInsets();
    tarsiz.width += insets.left + insets.right + getHgap()*2;
    tarsiz.height += insets.top + insets.bottom + getVgap()*2;
    return tarsiz;
//    return new Dimension(target.getBounds().width,target.getBounds().height);
  }

  /**
   * places the components defined by first to last within the target
   * container using the bounds box defined
   * @param target the container
   * @param x the x coordinate of the area
   * @param y the y coordinate of the area
   * @param width the width of the area
   * @param height the height of the area
   * @param first the first component of the container to place
   * @param last the last component of the container to place
   */
  private void placethem(Container target, int x, int y, int width, int height,
                                       int first, int last) {
/*    int align = getAlignment();
    //if ( align == this.TOP )
    //  y = 0;
    Insets insets = target.getInsets();
    if ( align == this.MIDDLE )
      y += height  / 2;
    if ( align == this.BOTTOM )
      y += height;*/

    for (int i = first ; i < last ; i++) {
      Component m = target.getComponent(i);
        Dimension md = m.getSize();
      if (m.isVisible()) {
        int px = x + (width-md.width)/2;
        m.setLocation(px, y);
        y += getVgap() + md.height;
      }
    }
  }

  /**
   * Lays out the container.
   * @param target the container to lay out.
   */
  public void layoutContainer(Container target)
  {
  	System.out.println("layouting Container....");
    Insets insets = target.getInsets();
//    int maxheight = target.getSize().height - (insets.top + insets.bottom + getVgap()*2);
//    int maxwidth = target.getSize().width - (insets.left + insets.right + getHgap()*2);
    int maxheight = target.getBounds().height - (insets.top + insets.bottom + getVgap()*2);
    int maxwidth = target.getBounds().width - (insets.left + insets.right + getHgap()*2);
    int numcomp = target.getComponentCount();
    int x = insets.left + getHgap();
    int y = 0  ;
    int colw = 0, start = 0;

    for (int i = 0 ; i < numcomp ; i++)
    {
      Component m = target.getComponent(i);
      if (m.isVisible())
      {
        Dimension d = m.getPreferredSize();
        // fit componenent size to container width
        m.setSize(maxwidth, d.height);
        d.width = maxwidth;

        if ( y  + d.height > maxheight )
        {
          placethem(target, x, insets.top + getVgap(), colw, maxheight-y, start, i);
          y = d.height;
          x += getHgap() + colw;
          colw = d.width;
          start = i;
        }
        else
        {
          if ( y > 0 )
          	y += getVgap();
					y += d.height;
          colw = Math.max(colw, d.width);
        }
      }
    }
    placethem(target, x, insets.top + getVgap(), colw, maxheight - y, start, numcomp);
  }
}
