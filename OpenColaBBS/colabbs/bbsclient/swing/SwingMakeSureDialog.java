package colabbs.bbsclient.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class SwingMakeSureDialog extends JDialog
{
	JPanel panel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel panel2 = new JPanel();
	FlowLayout flowLayout1 = new FlowLayout();
	JButton button1 = new JButton();
	JButton button2 = new JButton();
	JPanel panel3 = new JPanel();
	BorderLayout borderLayout2 = new BorderLayout();
	JPanel panel4 = new JPanel();
	JPanel panel5 = new JPanel();
	JPanel panel6 = new JPanel();
	private boolean sure;
	JPanel panel7 = new JPanel();
	BorderLayout borderLayout3 = new BorderLayout();
	JPanel panel8 = new JPanel();
	FlowLayout flowLayout2 = new FlowLayout();
	JLabel label1 = new JLabel();
	private String message;

	public SwingMakeSureDialog(Frame frame, String title, boolean modal)
	{
		super(frame, title, modal);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try
		{
			jbInit();
			add(panel1);
	    Toolkit myToolkit=this.getToolkit();
		  setLocation((myToolkit.getScreenSize().width+getSize().width)/2,(myToolkit.getScreenSize().height+getSize().height)/2);
//			pack();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public SwingMakeSureDialog(Frame frame)
	{
		this(frame, "", false);
	}

	public SwingMakeSureDialog(Frame frame, boolean modal)
	{
		this(frame, "", modal);
	}

	public SwingMakeSureDialog(Frame frame, String title)
	{
		this(frame, title, true);
	}

	void jbInit() throws Exception
	{
		panel1.setLayout(borderLayout1);
		panel2.setLayout(flowLayout1);
		button1.setText("½T©w");
		button1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button1_actionPerformed(e);
			}
		});
		button2.setText("¨ú®ø");
		button2.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button2_actionPerformed(e);
			}
		});
		panel3.setLayout(borderLayout2);
		flowLayout1.setAlignment(FlowLayout.RIGHT);
		panel7.setLayout(borderLayout3);
		panel4.setLayout(flowLayout2);
		label1.setBackground(SystemColor.control);
		panel1.add(panel2, BorderLayout.SOUTH);
		panel2.add(button1, null);
		panel2.add(button2, null);
		panel1.add(panel4, BorderLayout.CENTER);
		panel4.add(label1, null);
		panel1.add(panel5, BorderLayout.WEST);
		panel1.add(panel3, BorderLayout.EAST);
		panel3.add(panel6, BorderLayout.EAST);
		panel3.add(panel7, BorderLayout.CENTER);
		panel1.add(panel8, BorderLayout.NORTH);
	}

	protected void processWindowEvent(WindowEvent e)
	{
		if(e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			cancel();
		}
		super.processWindowEvent(e);
	}

	void cancel()
	{
    sure=false;
		dispose();
	}

	public boolean isSure()
	{
		return sure;
	}

	public void setMessage(String newMessage)
	{
		message = newMessage;
    label1.setText(message);
    pack();
    doLayout();
	}

	void button1_actionPerformed(ActionEvent e)
	{
    sure=true;
		dispose();
	}

	void button2_actionPerformed(ActionEvent e)
	{
    cancel();
	}
}
